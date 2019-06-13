package in.geekofia.blog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar mTopToolbar;
    private DrawerLayout mDrawer;
    private PostAdapter mAdapter;
    private ListView mLatestPostsListView;
    private Button mRetryButton;
    private ProgressBar mLoadingIndicator;
    private TextView mEmptyStateTextView, mDrawerAppVersion;

    private static final String GEEKOFIA_BLOG_ENDPOINT = "https://blog.geekofia.in/api/v1";
    private static final String LOG_TAG = MainActivity.class.getName();

    int versionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;


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

        mTopToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);

        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mTopToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);

        TextView appVersion = hView.findViewById(R.id.drawer_app_version);
        appVersion.setText("Version : " + versionName);

        TextView appDev = hView.findViewById(R.id.drawer_app_dev);
        appDev.setText("Developer : chankruze");

        // Latest Posts List
        mLatestPostsListView = findViewById(R.id.id_LatestPostsList);

        mLatestPostsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Post currentPost = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri postUri = Uri.parse(currentPost.getmPostUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, postUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void startLoader() {
        if (isConnected()) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            syncLatestPosts();
        } else {
            mLoadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
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
                            mEmptyStateTextView.setVisibility(View.GONE);
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

//        //noinspection SimplifiableIfStatement
//        if (id == R.id) {
//            Toast.makeText(MainActivity.this, "Thanks for your $100 donation !", Toast.LENGTH_LONG).show();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        // int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.nav_LatestPosts:
                mTopToolbar.setTitle(R.string.title_latest_posts);
                return true;
            case R.id.nav_Categories:
                mTopToolbar.setTitle(R.string.title_categories);
                return true;
            case R.id.nav_Tags:
                mTopToolbar.setTitle(R.string.title_tags);
                return true;
            case R.id.nav_Share:
                return true;
            case R.id.nav_Contact:
                return true;
            case R.id.nav_Author:
                return true;
            case R.id.nav_Sponsor:
                return true;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (!isConnected()){
            try{
                mAdapter.clear();
            }catch (Exception e){
                Toast.makeText(MainActivity.this, "Error Refreshing Posts!", Toast.LENGTH_SHORT).show();
            }
            startLoader();
        } else{
            mRetryButton.setVisibility(View.GONE);
            mEmptyStateTextView.setText("Refreshing Posts ...");
            syncLatestPosts();
        }

    }
}
