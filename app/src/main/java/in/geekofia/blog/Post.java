package in.geekofia.blog;

import java.util.List;

public class Post {
    private String mPostTitle, mPostDate, mPostDescription;
    private String mPostUrl;
    private List<String> mCategories, mTags;

    public Post(String mPostTitle, String mPostDate, String mPostDescription, String mPostUrl, List<String> mCategories, List<String> mTags) {
        this.mPostTitle = mPostTitle;
        this.mPostDate = mPostDate;
        this.mPostDescription = mPostDescription;
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
