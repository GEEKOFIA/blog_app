package in.geekofia.blog.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.geekofia.blog.models.Post;
import in.geekofia.blog.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private ArrayList<Post> mPostList;
    private static final String PROTO_ONE = "https://", PROTO_TWO = "http://", DOMAIN_URL = "https://blog.geekofia.in";

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onShareClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTextViewTitle, mTextViewDescription, mTextViewAuthor, mTextViewDate, mTextViewDuration, mTextViewShare;

        public PostViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.post_thumbnail);
            mTextViewTitle = itemView.findViewById(R.id.post_title);
            mTextViewDescription = itemView.findViewById(R.id.post_description);
            mTextViewAuthor = itemView.findViewById(R.id.post_author);
            mTextViewDate = itemView.findViewById(R.id.post_date);
            mTextViewDuration = itemView.findViewById(R.id.post_duration);
            mTextViewShare = itemView.findViewById(R.id.post_share);

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

            mTextViewShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onShareClick(position);
                        }
                    }
                }
            });

        }
    }

    public PostAdapter(Context mContext, ArrayList<Post> mPostList) {
        Context mContext1 = mContext;
        this.mPostList = mPostList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post currentItem = mPostList.get(position);

        String title = currentItem.getmPostTitle();
        String desc = currentItem.getmPostDescription();
        String author = currentItem.getmAuthor();
        String date = currentItem.getmPostDate();
        String duration = currentItem.getmPostDuration();
        String thumbnailUrl = currentItem.getmThumbnailUrl();

        holder.mTextViewTitle.setText(title);
        holder.mTextViewDescription.setText(desc);
        holder.mTextViewAuthor.setText(author);
        holder.mTextViewDate.setText(date);
        holder.mTextViewDuration.setText(duration);

        if (thumbnailUrl.toLowerCase().contains(PROTO_ONE) || thumbnailUrl.toLowerCase().contains(PROTO_TWO)){
            Picasso.get().load(thumbnailUrl).into(holder.mImageView);
        } else{
            thumbnailUrl = DOMAIN_URL + thumbnailUrl;
            Picasso.get().load(thumbnailUrl).into(holder.mImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

}
