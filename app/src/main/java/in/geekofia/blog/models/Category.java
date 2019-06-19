package in.geekofia.blog.models;

import java.util.List;

public class Category {
    private String mCategoryName;
    private List<Post> mPostsList;

    public Category(String mCategoryName, List<Post> mPostsList) {
        this.mCategoryName = mCategoryName;
        this.mPostsList = mPostsList;
    }

    public String getmCategoryName() {
        return mCategoryName;
    }

    public List<Post> getmPostsList() {
        return mPostsList;
    }
}