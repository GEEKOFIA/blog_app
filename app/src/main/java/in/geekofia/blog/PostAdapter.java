package in.geekofia.blog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class PostAdapter extends ArrayAdapter<Post> {

    private TextView mPostTitleField, mPostDateField, mPostDescriptionField, mPostUrlField;

    public PostAdapter(Activity context, ArrayList<Post> posts) {
        super(context, 0, posts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        Post currentPost = getItem(position);

        mPostTitleField = listItemView.findViewById(R.id.post_title);
        String title = Objects.requireNonNull(currentPost).getmPostTitle();
        mPostTitleField.setText(title);

        mPostDescriptionField = listItemView.findViewById(R.id.post_description);
        mPostDescriptionField.setText(currentPost.getmPostDescription());

//        mPostUrlField = listItemView.findViewById(R.id.post_url);
//        mPostUrlField.setText((CharSequence) currentPost.getmPostUrl());

        mPostDateField = listItemView.findViewById(R.id.post_date);
        mPostDateField.setText(currentPost.getmPostDate());

        return listItemView;
    }
}
