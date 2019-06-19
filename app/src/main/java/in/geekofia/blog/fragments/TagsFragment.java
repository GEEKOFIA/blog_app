package in.geekofia.blog.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.geekofia.blog.adapters.TagAdapter;
import in.geekofia.blog.decorations.ItemOffsetDecoration;
import in.geekofia.blog.models.Tag;
import in.geekofia.blog.utils.TagUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import in.geekofia.blog.R;

public class TagsFragment extends Fragment {
    private TagAdapter mAdapter;
    private static final String API_ENDPOINT_TAGS = "https://blog.geekofia.in/api/v2/tags/";
    private RecyclerView mRecyclerView;
    private static final int NUM_COLUMNS = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tags, container, false);
        getActivity().setTitle(R.string.title_fragment_tags);
        mRecyclerView = v.getRootView().findViewById(R.id.recycler_view_tags);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), NUM_COLUMNS));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setHasFixedSize(true);
        FetchPostsByTags();
        return v;
    }

    private void FetchPostsByTags() {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(API_ENDPOINT_TAGS)
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
                    final ArrayList<Tag> tags = TagUtils.extractPosts(myResponse);
                    mAdapter = new TagAdapter(getActivity(), tags);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.setAdapter(mAdapter);

                            mAdapter.setOnItemClickListener(new TagAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    String tagName = tags.get(position).getmTagName();
                                    // Convert the String URL into a URI object (to pass into the Intent constructor)
                                    Uri postUri = Uri.parse("https://blog.geekofia.in/tags/" + tagName);

                                    // Create a new intent to view the earthquake URI
                                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, postUri);

                                    // Send the intent to launch a new activity
                                    startActivity(websiteIntent);
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}
