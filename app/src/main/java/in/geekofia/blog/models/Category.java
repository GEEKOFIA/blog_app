package in.geekofia.blog.models;

public class Category {
    private String mCategoryName, mCategoryLogoUrl;

    public Category(String mCategoryName, String mCategoryLogoUrl) {
        this.mCategoryName = mCategoryName;
        this.mCategoryLogoUrl = mCategoryLogoUrl;
    }

    public String getmCategoryName() {
        return mCategoryName;
    }

    public String getmCategoryLogoUrl() {
        return mCategoryLogoUrl;
    }

}