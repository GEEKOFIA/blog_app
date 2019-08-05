package in.geekofia.blog.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.geekofia.blog.models.Post;
import in.geekofia.blog.adapters.PostAdapter;
import in.geekofia.blog.R;
import in.geekofia.blog.utils.PostUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class PostsFragment extends Fragment {

    private PostAdapter mAdapter;
    private ArrayList<Post> mPostList, mPostListFull;
    private static final String API_ENDPOINT_POSTS = "https://blog.geekofia.in/api/v2/posts/";
    private RecyclerView mRecyclerView;
    private TextView mEmptyStateTextView;
    private ProgressBar mLoadingIndicator;
    private Button mRetryButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_posts, container, false);
        getActivity().setTitle(R.string.title_fragment_Posts);

        initializeViews(v);

        mRecyclerView = v.getRootView().findViewById(R.id.recycler_view_posts);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        loadLatestPosts();

        return v;
    }

    private void initializeViews(View view) {
        mEmptyStateTextView = view.getRootView().findViewById(R.id.empty_view);
        mRetryButton = view.getRootView().findViewById(R.id.retryButton);
        mLoadingIndicator = view.getRootView().findViewById(R.id.loading_indicator);

        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmptyStateTextView.setText("");
                mRetryButton.setVisibility(View.GONE);
                mLoadingIndicator.setVisibility(View.VISIBLE);
                loadLatestPosts();
            }
        });
    }

    private void loadLatestPosts() {
        if (isConnected()) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            FetchLatestPosts();
        } else {
            mLoadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
            mRetryButton.setVisibility(View.VISIBLE);
        }
    }

    private void FetchLatestPosts() {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(API_ENDPOINT_POSTS)
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

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final ArrayList<Post> posts = PostUtils.extractPosts(myResponse);
                            mPostList = posts;
                            mPostListFull = new ArrayList<>(mPostList);
                            mAdapter = new PostAdapter(getActivity(), posts);

                            mLoadingIndicator.setVisibility(View.GONE);
                            mEmptyStateTextView.setText("");

                            mRecyclerView.setAdapter(mAdapter);

                            mAdapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    String shareUrl = posts.get(position).getmPostUrl();
                                    // Convert the String URL into a URI object (to pass into the Intent constructor)
                                    Uri postUri = Uri.parse(shareUrl);

                                    // Create a new intent to view the earthquake URI
                                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, postUri);

                                    // Send the intent to launch a new activity
                                    startActivity(websiteIntent);
                                }

                                @Override
                                public void onShareClick(int position) {
                                    RecyclerView.ViewHolder viewMoreHolder = mRecyclerView.findViewHolderForLayoutPosition(position);
                                    View viewMore = viewMoreHolder.itemView.findViewById(R.id.post_more);
                                    showPopup(getActivity(), viewMore, posts, position);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    public void search(String queryText) {
        mAdapter.getFilter().filter(queryText);
    }


    public void showPopup(final Context context, View view, final ArrayList<Post> posts, final int position) {
        // Setup Popup Menu
        MenuBuilder menuBuilder = new MenuBuilder(context);
        MenuInflater inflater = new MenuInflater(context);
        inflater.inflate(R.menu.post_menu, menuBuilder);
        MenuPopupHelper optionsMenu = new MenuPopupHelper(context, menuBuilder, view);
        optionsMenu.setForceShowIcon(true);

        // setup click listener
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_copy_post:
                        // Gets a handle to the clipboard service.
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Copy Post URL", posts.get(position).getmPostUrl());
                        clipboard.setPrimaryClip(clip);
                        return true;
                    case R.id.action_share_post:
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, "Sharing Post");
                        i.putExtra(Intent.EXTRA_TEXT, "Read This Awesome Blog Post of Geekofia\n" + posts.get(position).getmPostUrl());
                        startActivity(Intent.createChooser(i, "Share " + posts.get(position).getmPostTitle()));
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {

            }
        });

        // show popup
        optionsMenu.show();
    }

    // check internet connectivity
    private boolean isConnected() {
        // Check for connectivity status
        ConnectivityManager connMgr = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isConnected()) {
            mRecyclerView.removeAllViewsInLayout();
            mEmptyStateTextView.setText(R.string.no_internet_connection);
            mRetryButton.setVisibility(View.VISIBLE);
        } else {
            mRetryButton.setVisibility(View.GONE);
            mLoadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText("");
            loadLatestPosts();
        }
    }
}