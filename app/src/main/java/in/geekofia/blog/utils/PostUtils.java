package in.geekofia.blog.utils;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.geekofia.blog.Post;

public final class PostUtils {

    public static final String LOG_TAG = "Error Log";

    private PostUtils() {
    }

    public static ArrayList<Post> extractPosts(String JSON_RESPONSE) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(JSON_RESPONSE)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding blog posts to
        ArrayList<Post> latestPosts = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONArray RootEarthPosts = new JSONArray(JSON_RESPONSE);

            for (int i = 0; i < RootEarthPosts.length(); i++) {
                JSONObject CurrentPost = RootEarthPosts.getJSONObject(i);
                String title = CurrentPost.getString("title");
                String desc = CurrentPost.getString("desc");
                String date = CurrentPost.getString("date");
                String url = CurrentPost.getString("url");
                String author = CurrentPost.getString("author");
                String duration = CurrentPost.getString("duration");
                String thumbnail = CurrentPost.getString("thumbnail");

                Post newPost = new Post(title, desc, author, date, duration, thumbnail, url);
                latestPosts.add(newPost);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("PostUtils", "Problem parsing the LatestPosts JSON results", e);
        }

        // Return the list of latest posts
        return latestPosts;
    }
}
