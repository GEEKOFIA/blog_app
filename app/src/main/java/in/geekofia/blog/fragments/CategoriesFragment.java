package in.geekofia.blog.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.geekofia.blog.adapters.CategoryAdapter;
import in.geekofia.blog.decorations.ItemOffsetDecoration;
import in.geekofia.blog.models.Category;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import in.geekofia.blog.R;
import in.geekofia.blog.utils.CategoryUtils;

public class CategoriesFragment extends Fragment {

    private CategoryAdapter mAdapter;
    private static final String API_ENDPOINT_CATEGORIES = "https://blog.geekofia.in/api/v2/categories/";
    private RecyclerView mRecyclerView;
    private static final int NUM_COLUMNS = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_categories, container, false);
        getActivity().setTitle(R.string.title_fragment_categories);
        mRecyclerView = v.getRootView().findViewById(R.id.recycler_view_categories);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), NUM_COLUMNS));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setHasFixedSize(true);
        FetchPostsByCategories();
        return v;
    }

    private void FetchPostsByCategories() {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(API_ENDPOINT_CATEGORIES)
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
                    final ArrayList<Category> categories = CategoryUtils.extractPosts(myResponse);
                    mAdapter = new CategoryAdapter(getActivity(), categories);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.setAdapter(mAdapter);

                            mAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    String categoryName = categories.get(position).getmCategoryName();
                                    // Convert the String URL into a URI object (to pass into the Intent constructor)
                                    Uri postUri = Uri.parse("https://blog.geekofia.in/categories/" + categoryName);


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
