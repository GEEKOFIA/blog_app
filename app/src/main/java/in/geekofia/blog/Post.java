package in.geekofia.blog;

import java.util.List;

public class Post {
    private String mPostTitle, mPostDate, mPostDescription, mAuthor, mThumbnailUrl, mPostUrl;
    private List<String> mCategories, mTags;

    public Post(String mPostTitle, String mPostDate, String mPostDescription, String mAuthor,String mThumbnailUrl, String mPostUrl, List<String> mCategories, List<String> mTags) {
        this.mPostTitle = mPostTitle;
        this.mPostDate = mPostDate;
        this.mPostDescription = mPostDescription;
        this.mAuthor = mAuthor;
        this.mThumbnailUrl = mThumbnailUrl;
        this.mPostUrl = mPostUrl;
        this.mCategories = mCategories;
        this.mTags = mTags;
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

    public List<String> getmCategories() {
        return mCategories;
    }

    public List<String> getmTags() {
        return mTags;
    }
}
