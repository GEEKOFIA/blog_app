package in.geekofia.blog;

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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Context mContext;
    private ArrayList<Post> mPostList;
    private static final String PROTO_ONE = "https://", PROTO_TWO = "http://", DOMAIN_URL = "https://blog.geekofia.in";

    public PostAdapter(Context mContext, ArrayList<Post> mPostList) {
        this.mContext = mContext;
        this.mPostList = mPostList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post currentItem = mPostList.get(position);

        String title = currentItem.getmPostTitle();
        String desc = currentItem.getmPostDescription();
        String author = currentItem.getmAuthor();
        String date = currentItem.getmPostDate();
        String thumbnailUrl = currentItem.getmThumbnailUrl();

        holder.mTextViewTitle.setText(title);
        holder.mTextViewDescription.setText(desc);
        holder.mTextViewAuthor.setText(author);
        holder.mTextViewDate.setText(date);

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

    public class PostViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTextViewTitle, mTextViewDescription, mTextViewAuthor, mTextViewDate;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.post_thumbnail);
            mTextViewTitle = itemView.findViewById(R.id.post_title);
            mTextViewDescription = itemView.findViewById(R.id.post_description);
            mTextViewAuthor = itemView.findViewById(R.id.post_author);
            mTextViewDate = itemView.findViewById(R.id.post_date);

        }
    }

}
