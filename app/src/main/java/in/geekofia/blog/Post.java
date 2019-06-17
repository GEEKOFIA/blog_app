package in.geekofia.blog;

public class Post {
    private String mPostTitle, mPostDate, mPostDescription, mAuthor, mThumbnailUrl, mPostUrl;

    public Post(String mPostTitle, String mPostDescription, String mAuthor, String mPostDate, String mThumbnailUrl, String mPostUrl) {
        this.mPostTitle = mPostTitle;
        this.mPostDescription = mPostDescription;
        this.mAuthor = mAuthor;
        this.mPostDate = mPostDate;
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

    public String getmAuthor(){
        return mAuthor;
    }

    public String getmThumbnailUrl() {
        return mThumbnailUrl;
    }

    public String getmPostUrl() {
        return mPostUrl;
    }
}