package in.geekofia.blog.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import in.geekofia.blog.R;

public class ReadingActivity extends AppCompatActivity {

    private TextView mPostTitleView, mPostDateView, mPostAuthorView, mPostDurationView, mPostContentView;
    private ImageView mPostFeaturedImageView;
    private String mPostURL, mPostFeaturedImageURL, mPostTitle, mPostDate, mPostAuthor, mPostDuration;

    private static final String PROTO_ONE = "https://", PROTO_TWO = "http://", DOMAIN_URL = "https://blog.geekofia.in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        initViews();
        Bundle b = getIntent().getExtras();
        mPostURL = b.getString("PostURL");

        new Thread(new Runnable() {
            public void run() {
                try {
                    final Document post;
                    post = Jsoup.connect(mPostURL).get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Post title
                            mPostTitle = post.select("h1").text();
                            // Post metadata
                            Elements postMetadata = post.select("ul.metadata.top");
                            // post date
                            mPostDate = postMetadata.select("li.post-date").text();
                            // post author
                            mPostAuthor = postMetadata.select("li.post-author").text();
                            // post duration
                            mPostDuration = postMetadata.select("li.post-duration").text();
                            // Image
                            Elements postFeaturedImage = post.select("img[alt$=featured-image]");
                            mPostFeaturedImageURL = postFeaturedImage.attr("src");

                            Elements mPostContents = post.select("div.post-contents");

                            mPostTitleView.setText(mPostTitle);
                            mPostDateView.setText(mPostDate);
                            mPostAuthorView.setText(mPostAuthor);
                            mPostDurationView.setText(mPostDuration);
                            if (mPostFeaturedImageURL != "") {
                                if (mPostFeaturedImageURL.toLowerCase().contains(PROTO_ONE) || mPostFeaturedImageURL.toLowerCase().contains(PROTO_TWO)) {
                                    Picasso.get().load(mPostFeaturedImageURL).into(mPostFeaturedImageView);
                                    Log.e("Featured Image Url", mPostFeaturedImageURL);
                                } else {
                                    mPostFeaturedImageURL = DOMAIN_URL + mPostFeaturedImageURL;
                                    Picasso.get().load(mPostFeaturedImageURL).into(mPostFeaturedImageView);
                                    Log.e("Featured Image Url", mPostFeaturedImageURL);
                                }
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                mPostContentView.setText(Html.fromHtml(String.valueOf(mPostContents), Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                mPostContentView.setText(Html.fromHtml(String.valueOf(mPostContents)));
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initViews() {
        mPostTitleView = findViewById(R.id.view_post_title);
        mPostDateView = findViewById(R.id.view_post_date);
        mPostAuthorView = findViewById(R.id.view_post_author);
        mPostDurationView = findViewById(R.id.view_post_duration);
        mPostFeaturedImageView = findViewById(R.id.view_post_featured_image);
        mPostContentView = findViewById(R.id.view_post_body);
    }

}
