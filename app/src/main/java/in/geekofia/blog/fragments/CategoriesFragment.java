package in.geekofia.blog.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

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
    private static final String API_ENDPOINT_CATEGORIES = "https://blog.geekofia.in/api/v3/categories/";
    private RecyclerView mRecyclerView;
    private static final int NUM_COLUMNS = 2;

    private ShimmerFrameLayout mShimmerViewContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_categories, container, false);
        getActivity().setTitle(R.string.title_fragment_categories);

        initShimmer(v);

        mRecyclerView = v.getRootView().findViewById(R.id.recycler_view_categories);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), NUM_COLUMNS));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setHasFixedSize(true);

        FetchPostsByCategories();

        return v;
    }

    private void initShimmer(View view) {
        mShimmerViewContainer = view.getRootView().findViewById(R.id.shimmer_category_container);
        mShimmerViewContainer.startShimmer();
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

                            mShimmerViewContainer.setVisibility(View.GONE);
                            mShimmerViewContainer.stopShimmer();

                            mAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    String categoryName = categories.get(position).getmCategoryName();

                                    // create new instance of posts fragment
                                    PostsFragment postsFragment = new PostsFragment();

                                    // Pass the post end point to posts fragment
                                    Bundle mPostEnd = new Bundle();
                                    mPostEnd.putString("Title", categoryName);
                                    mPostEnd.putString("PostEnd", "/categories/" + categoryName);
                                    postsFragment.setArguments(mPostEnd);

                                    // replace categories fragment with posts
                                    getActivity()
                                            .getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragment_container, postsFragment)
                                            .commit();
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}
