package in.geekofia.blog.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
    private WebView mWebView;

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
                    post.select("div.post-contents").select("p").attr("style", "color: #000000;");
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
                    postFeaturedImage.remove();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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
                            } else {
                                mPostFeaturedImageView.setVisibility(View.GONE);
                            }

                            mWebView.loadUrl(mPostURL);
                            mWebView.getSettings().setJavaScriptEnabled(true);
                            mWebView.setWebViewClient(new WebViewClient() {

                                @Override
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    view.loadUrl(url);
                                    return true;
                                }

                                @Override
                                public void onPageFinished(WebView view, String url) {
                                    mWebView.loadUrl("javascript:(function() { " +
                                            "let welcome = document.getElementById('welcome'); welcome.classList.remove('d-block');" +
                                            "welcome.style.display='none';" +

                                            "document.getElementsByClassName('navbar')[0].style.display='none'; " +
                                            "document.getElementsByTagName('h1')[0].style.display='none'; " +

                                            "document.getElementsByClassName('scrollup')[0].style.display='none'; " +
                                            "document.getElementsByClassName('footer')[0].style.display='none'; " +

                                            "document.getElementsByClassName('metadata')[0].style.display='none';" +
                                            "document.getElementsByClassName('metadata')[1].style.display='none';" +

                                            "document.getElementsByClassName('related-posts')[0].style.display='none';" +

                                            "document.getElementsByTagName('img')[0].style.display='none';" +
                                            "})()");

                                    mShimmerViewContainer.setVisibility(View.GONE);
                                    mShimmerViewContainer.stopShimmer();
                                }
                            });
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
//        mPostContentView = findViewById(R.id.view_post_body);
        mWebView = findViewById(R.id.view_post_body);
    }

}
