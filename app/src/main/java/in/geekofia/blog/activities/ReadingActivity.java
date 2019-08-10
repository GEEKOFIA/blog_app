package in.geekofia.blog.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import in.geekofia.blog.R;

public class ReadingActivity extends AppCompatActivity {

    private TextView mPostTitleView, mPostDateView, mPostAuthorView, mPostDurationView, mPostContentView;
    private ImageView mPostFeaturedImageView;
    private String mPostURL, mPostFeaturedImageURL, mPostTitle, mPostDate, mPostAuthor, mPostDuration;
    private ShimmerFrameLayout mShimmerViewContainer;

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
                            post.select("div.post-contents").select("p").attr("style", "color: #000000;");
//                            Elements links = post.select("div.post-contents a");
//                            links.attr("style", "color: #00FF00;");
//                            post.select("pre").attr("color: #00FF00; background: #000000");
//                            post.select("div.post-contents ul").attr("style", "list-style: none;");
//                            post.select("div.post-contents li").attr("style", "color: #0000FF;");
//                            Elements headings = post.select("h4");
//                            for (Element h4 : headings){
//                                h4.attr("style", "color: #FF0000");
//                                Log.i("TAG", h4.attributes().toString());
//                            }
//                            Log.i("TAG",post.select("div.post-contents").select("p").attr("style", "color: #000000;").toString());
                            // Post title
                            mPostTitle = post.select("h1").text();
                            // Post metadata
                            Elements postMetadata = post.select("ul.metadata.top");
                            // post date
                            mPostDate = postMetadata.select("li.post-date").text();
                            // post author
                            mPostAuthor = postMetadata.select("li.post-author").text();
                            // post duration
                            mPostDuration = postMetadata.select("li.post-duration").text().split(" read")[0];
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
                                } else {
                                    mPostFeaturedImageURL = DOMAIN_URL + mPostFeaturedImageURL;
                                    Picasso.get().load(mPostFeaturedImageURL).into(mPostFeaturedImageView);
                                }
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                mPostContentView.setText(Html.fromHtml(mPostContents.html(), Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                mPostContentView.setText(Html.fromHtml(mPostContents.html()));
                            }
                            mPostDateView.setVisibility(View.VISIBLE);
                            mPostAuthorView.setVisibility(View.VISIBLE);
                            mPostDurationView.setVisibility(View.VISIBLE);
                            mShimmerViewContainer.setVisibility(View.GONE);
                            mShimmerViewContainer.stopShimmer();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initViews() {
        mShimmerViewContainer = findViewById(R.id.shimmer_post_container);
        mShimmerViewContainer.startShimmer();
        mPostTitleView = findViewById(R.id.view_post_title);
        mPostDateView = findViewById(R.id.view_post_date);
        mPostAuthorView = findViewById(R.id.view_post_author);
        mPostDurationView = findViewById(R.id.view_post_duration);
        mPostFeaturedImageView = findViewById(R.id.view_post_featured_image);
        mPostContentView = findViewById(R.id.view_post_body);
    }

}
