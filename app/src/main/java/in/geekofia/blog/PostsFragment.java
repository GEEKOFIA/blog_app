package in.geekofia.blog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.geekofia.blog.utils.PostUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import static androidx.core.content.ContextCompat.startActivity;

public class PostsFragment extends Fragment {

    private PostAdapter mAdapter;
    private static final String GEEKOFIA_BLOG_ENDPOINT = "https://blog.geekofia.in/api/v2/posts/";
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_posts, container, false);
        mRecyclerView = v.getRootView().findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        FetchLatestPosts();
        return v;
    }

    private void FetchLatestPosts() {
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
                    final ArrayList<Post> posts = PostUtils.extractPosts(myResponse);
                    mAdapter = new PostAdapter(getActivity(), posts);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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
                                    Intent i = new Intent(Intent.ACTION_SEND);
                                    i.setType("text/plain");
                                    i.putExtra(Intent.EXTRA_SUBJECT, "Sharing Post");
                                    i.putExtra(Intent.EXTRA_TEXT, "Read This Awesome Blog Post of Geekofia" + posts.get(position).getmPostUrl());
                                    startActivity(Intent.createChooser(i, "Share " + posts.get(position).getmPostTitle()));
                                }
                            });
                        }
                    });
                }
            }
        });
    }

}