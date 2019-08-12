package in.geekofia.blog.utils;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.geekofia.blog.models.Category;

public final class CategoryUtils {

    private CategoryUtils() {
    }

    public static ArrayList<Category> extractPosts(String JSON_RESPONSE) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(JSON_RESPONSE)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding blog posts to
        ArrayList<Category> categories = new ArrayList<>();

        try {

            JSONArray RootResponseArray = new JSONArray(JSON_RESPONSE);

            for (int i = 0; i < RootResponseArray.length(); i++) {
                JSONObject CurrentCategory = RootResponseArray.getJSONObject(i);

                // Current Category Name
                String categoryName = CurrentCategory.getString("category");
                // Current Category Logo
                String categoryLogo = CurrentCategory.getString("logo");

                Category newCategory = new Category(categoryName, categoryLogo);
                categories.add(newCategory);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("PostUtils", "Problem parsing the LatestPosts JSON results", e);
        }

        // Return the list of latest posts
        return categories;
    }
}
