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

import in.geekofia.blog.R;
import in.geekofia.blog.models.Tag;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {
    private Context mContext;
    private ArrayList<Tag> mTagList;
    private static final String PROTO_ONE = "https://", PROTO_TWO = "http://", DOMAIN_URL = "https://blog.geekofia.in";

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(TagAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTextViewTagName;

        public TagViewHolder(@NonNull View itemView, final TagAdapter.OnItemClickListener listener) {
            super(itemView);
            mTextViewTagName = itemView.findViewById(R.id.grid_item_name);

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

    public TagAdapter(Context mContext, ArrayList<Tag> mTagList) {
        this.mContext = mContext;
        this.mTagList = mTagList;
    }

    @NonNull
    @Override
    public TagAdapter.TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new TagViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TagAdapter.TagViewHolder holder, int position) {
        Tag currentItem = mTagList.get(position);
        String name = currentItem.getmTagName();
        holder.mTextViewTagName.setText(name);
    }

    @Override
    public int getItemCount() {
        return mTagList.size();
    }
}
