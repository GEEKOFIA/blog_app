package in.geekofia.blog.models;

public class Post {
    private String mPostTitle, mPostDate, mPostDescription, mAuthor, mPostDuration, mThumbnailUrl, mPostUrl;

    public Post(String mPostTitle, String mPostDescription, String mAuthor, String mPostDate, String mPostDuration, String mThumbnailUrl, String mPostUrl) {
        this.mPostTitle = mPostTitle;
        this.mPostDescription = mPostDescription;
        this.mAuthor = mAuthor;
        this.mPostDate = mPostDate;
        this.mPostDuration = mPostDuration;
        this.mThumbnailUrl = mThumbnailUrl;
        this.mPostUrl = mPostUrl;
    }

    public String getmPostTitle() {
        return mPostTitle;
    }

    public String getmPostDate() {
        return mPostDate;
    }

    public String getmPostDescription() {
        return mPostDescription;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmPostDuration() {
        return mPostDuration;
    }

    public String getmThumbnailUrl() {
        return mThumbnailUrl;
    }

    public String getmPostUrl() {
        return mPostUrl;
    }
}