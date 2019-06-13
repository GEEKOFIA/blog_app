package in.geekofia.blog;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Toolbar mTopToolbar;
    private PostAdapter mAdapter;
    private ListView mLatestPostsListView;
    private Button mRetryButton;
    private ProgressBar mLoadingIndicator;
    private TextView mEmptyStateTextView;

    private static final String GEEKOFIA_BLOG_ENDPOINT = "https://blog.geekofia.in/api/v1";
    private static final String LOG_TAG = MainActivity.class.getName();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTopToolbar.setTitle(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTopToolbar.setTitle(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTopToolbar.setTitle(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI Elements
        initializeViews();

        // Send HTTP Request & Get Latest Posts
        startLoader();
    }

    private void initializeViews() {
        // Bottom Navigation Bar
        BottomNavigationView navigation = findViewById(R.id.id_BottomNavigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Toolbar
        mTopToolbar = findViewById(R.id.id_Toolbar);
        setSupportActionBar(mTopToolbar);

        // Latest Posts List
        mLatestPostsListView = findViewById(R.id.id_LatestPostsList);

        // Loading Indicator
        mLoadingIndicator = findViewById(R.id.id_LoadingIndicator);

        mEmptyStateTextView = findViewById(R.id.id_EmptyView);

        // Retry Button
        mRetryButton = findViewById(R.id.id_RetryButton);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmptyStateTextView.setText("");
                mRetryButton.setVisibility(View.GONE);
                mLoadingIndicator.setVisibility(View.VISIBLE);
                mRetryButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startLoader();
                    }
                }, 1000);
            }
        });

    }

    private void startLoader() {
        if (isConnected()) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            syncLatestPosts();
        } else {
            mLoadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
            mRetryButton.setVisibility(View.VISIBLE);
        }
    }

    private void syncLatestPosts() {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(GEEKOFIA_BLOG_ENDPOINT)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse;
                    myResponse = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<Post> posts = PostUtils.extractPosts(myResponse);
                            mAdapter = new PostAdapter(MainActivity.this, posts);
                            mLoadingIndicator.setVisibility(View.GONE);
                            mLatestPostsListView.setAdapter(mAdapter);
                        }
                    });
                }
            }
        });
    }

    private boolean isConnected() {
        // Check for connectivity status
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            Toast.makeText(MainActivity.this, "Thanks for your $100 donation !", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
