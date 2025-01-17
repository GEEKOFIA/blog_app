package in.geekofia.blog.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import in.geekofia.blog.R;
import in.geekofia.blog.models.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context mContext;
    private ArrayList<Category> mCategoryList;
    private static final String PROTO_ONE = "https://", PROTO_TWO = "http://", DOMAIN_URL = "https://blog.geekofia.in";

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(CategoryAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        public ImageView mCategoryLogoView;
        public TextView mCategoryNameView;

        public CategoryViewHolder(@NonNull View itemView, final CategoryAdapter.OnItemClickListener listener) {
            super(itemView);
            mCategoryNameView = itemView.findViewById(R.id.grid_category_name);
            mCategoryLogoView = itemView.findViewById(R.id.grid_category_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public CategoryAdapter(Context mContext, ArrayList<Category> mCategoryList) {
        this.mContext = mContext;
        this.mCategoryList = mCategoryList;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new CategoryViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category currentItem = mCategoryList.get(position);
        String categoryName = currentItem.getmCategoryName();
        String logoUrl = currentItem.getmCategoryLogoUrl();

        holder.mCategoryNameView.setText(categoryName);

        if (logoUrl.toLowerCase().contains(PROTO_ONE) || logoUrl.toLowerCase().contains(PROTO_TWO)){
            Picasso.get().load(logoUrl).into(holder.mCategoryLogoView);
        } else{
            logoUrl = DOMAIN_URL + logoUrl;
            Picasso.get().load(logoUrl).into(holder.mCategoryLogoView);
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }
}
