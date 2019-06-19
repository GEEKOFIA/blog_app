package in.geekofia.blog.models;

import java.util.List;

public class Tag {
    private String mTagName;
    private List<Post> mPostsList;

    public Tag(String mTagName, List<Post> mPostsList) {
        this.mTagName = mTagName;
        this.mPostsList = mPostsList;
    }

    public String getmTagName() {
        return mTagName;
    }

    public List<Post> getmPostsList() {
        return mPostsList;
    }
}