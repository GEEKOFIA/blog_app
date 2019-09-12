package in.geekofia.blog.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DecimalFormat;

import in.geekofia.blog.R;

public class ReadingActivity extends AppCompatActivity {

    private ScrollView mScrollView;
    private TextView mPostTitleView, mPostDateView, mPostAuthorView, mPostDurationView, mPostContentView;
    private ImageView mPostFeaturedImageView;
    private String mPostURL, mPostFeaturedImageURL, mPostTitle, mPostDate, mPostAuthor, mPostDuration;
    private ShimmerFrameLayout mShimmerViewContainer;
    private WebView mWebView;
    private ViewTreeObserver viewTreeObserver;
    private ProgressBar mProgressBar;

    private static final String PROTO_ONE = "https://", PROTO_TWO = "http://", DOMAIN_URL = "https://blog.geekofia.in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // For full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_reading);

        // initialize views
        initViews();
        Bundle b = getIntent().getExtras();
        mPostURL = b.getString("PostURL");

        // Load post
        loadPost();

        viewTreeObserver = mScrollView.getViewTreeObserver();

        if (viewTreeObserver.isAlive()){
            viewTreeObserver.addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    int scrollY = mScrollView.getScrollY();
                    int scrollViewHeight = mScrollView.getHeight();
                    int childViewHeight = mScrollView.getChildAt(0).getHeight();
                    int maxScrollY = childViewHeight - scrollViewHeight;

                    double currentProgress = Double.parseDouble((new DecimalFormat("##.##").format(100.0 * scrollY / maxScrollY)));

                    // update progress bar
                    mProgressBar.setProgress((int) currentProgress);
                }
            });
        }
    }

    private void initViews() {
        mScrollView = findViewById(R.id.scrollView);

        mProgressBar = findViewById(R.id.progress_horizontal);

        mShimmerViewContainer = findViewById(R.id.shimmer_post_container);
        mShimmerViewContainer.startShimmer();

        mPostTitleView = findViewById(R.id.view_post_title);
        mPostTitleView.setVisibility(View.GONE);

        mPostDateView = findViewById(R.id.view_post_date);
        mPostDateView.setVisibility(View.GONE);

        mPostAuthorView = findViewById(R.id.view_post_author);
        mPostAuthorView.setVisibility(View.GONE);

        mPostDurationView = findViewById(R.id.view_post_duration);
        mPostDurationView.setVisibility(View.GONE);

        mPostFeaturedImageView = findViewById(R.id.view_post_featured_image);
        mPostFeaturedImageView.setVisibility(View.GONE);

        mWebView = findViewById(R.id.view_post_body);
        mWebView.setVisibility(View.GONE);
    }

    private void loadPost(){
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

                            mWebView.loadUrl(mPostURL);
                            mWebView.getSettings().setJavaScriptEnabled(true);
                            mWebView.setWebViewClient(new WebViewClient() {

                                @Override
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                                        view.getContext().startActivity(
                                                new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                        return true;
                                    } else {
                                        return false;
                                    }
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

                                    mPostTitleView.setVisibility(View.VISIBLE);
                                    mPostDateView.setVisibility(View.VISIBLE);
                                    mPostAuthorView.setVisibility(View.VISIBLE);
                                    mPostDurationView.setVisibility(View.VISIBLE);

                                    if (mPostFeaturedImageURL != "") {
                                        if (mPostFeaturedImageURL.toLowerCase().contains(PROTO_ONE) || mPostFeaturedImageURL.toLowerCase().contains(PROTO_TWO)) {
                                            Picasso.get().load(mPostFeaturedImageURL).into(mPostFeaturedImageView);
                                        } else {
                                            mPostFeaturedImageURL = DOMAIN_URL + mPostFeaturedImageURL;
                                            Picasso.get().load(mPostFeaturedImageURL).into(mPostFeaturedImageView);
                                        }
                                        mPostFeaturedImageView.setVisibility(View.VISIBLE);
                                    }

                                    Transition transition = new Slide(Gravity.BOTTOM);
                                    transition.setDuration(1000);
                                    transition.addTarget(R.id.view_post_body);
                                    TransitionManager.beginDelayedTransition((ViewGroup) mWebView.getParent(), transition);
                                    mWebView.setVisibility(View.VISIBLE);
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
}
