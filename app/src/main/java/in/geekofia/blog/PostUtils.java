package in.geekofia.blog;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class PostUtils {
    private static final String SAMPLE_JSON_RESPONSE = "[\n" +
            "  {\n" +
            "    \"title\": \"How To Set Up Flutter For Web Development\",\n" +
            "    \"url\": \"/howto/2019/06/11/how-to-setup-flutter-web.html\",\n" +
            "    \"category\": \"HowTo\",\n" +
            "    \"tags\": \"[Flutter,Dart]\",\n" +
            "    \"date\": \"June 11, 2019\",\n" +
            "    \"desc\": \"Setting Up Flutter For Web Dev\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"title\": \"How To Add External Link Indicator With CSS\",\n" +
            "    \"url\": \"/programming/2019/06/11/how-to-add-external-link-indicator-with-css.html\",\n" +
            "    \"category\": \"Programming\",\n" +
            "    \"tags\": \"[CSS]\",\n" +
            "    \"date\": \"June 11, 2019\",\n" +
            "    \"desc\": \"Add an external link indicator with CSS to anchor tags\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"title\": \"How To Swap Two Numbers in C using Pointers\",\n" +
            "    \"url\": \"/programming/howto/2019/06/01/how-to-swap-values-using-pointers.html\",\n" +
            "    \"category\": \"Programming,HowTo\",\n" +
            "    \"tags\": \"[C]\",\n" +
            "    \"date\": \"June 01, 2019\",\n" +
            "    \"desc\": \"C function to swap two numbers\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"title\": \"How To Install Django in Virtual Environment\",\n" +
            "    \"url\": \"/howto/2019/05/26/how-to-install-django-in-virtual-environment.html\",\n" +
            "    \"category\": \"HowTo\",\n" +
            "    \"tags\": \"[Django,Python]\",\n" +
            "    \"date\": \"May 26, 2019\",\n" +
            "    \"desc\": \"install Django in virtual environment\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"title\": \"Fix unexpected token at start of statement error with clang\",\n" +
            "    \"url\": \"/android/patches/2019/05/20/unexpected-token-at-start-of-statement-patch.html\",\n" +
            "    \"category\": \"Android,Patches\",\n" +
            "    \"tags\": \"[Kernel,CompilationError]\",\n" +
            "    \"date\": \"May 20, 2019\",\n" +
            "    \"desc\": \"Work around to fix unexpected tocken error\"\n" +
            "  }\n" +
            "]";

    public static final String LOG_TAG = "Error Log";

    private PostUtils() {
    }

    /**
     * Return a list of {@link Post} objects that has been built up from
     * parsing a JSON response.
     */
    
    public static ArrayList<Post> extractPosts() {

        // Create an empty ArrayList that we can start adding blog posts to
        ArrayList<Post> latestPosts = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONArray RootEarthPosts = new JSONArray(SAMPLE_JSON_RESPONSE);

            for (int i = 0; i < RootEarthPosts.length(); i++) {
                JSONObject CurrentPost = RootEarthPosts.getJSONObject(i);
                String title = CurrentPost.getString("title");
                String desc = CurrentPost.getString("desc");
                String date = CurrentPost.getString("date");
                String url = CurrentPost.getString("url");
                String  categoryArray = CurrentPost.getString("category");
                String  tagsArray = CurrentPost.getString("tags");

//                List<String> categories = new ArrayList<>();
//                for (int c = 0; c < categoryArray.length(); i++) {
//                    categories.add(categoryArray.getString(c));
//                }

//                List<String> tags = new ArrayList<>();
//                for (int c = 0; c < tagsArray.length(); i++) {
//                    tags.add(tagsArray.getString(c));
//                }

                Post newPost = new Post(title, date, desc, url, null, null);
                latestPosts.add(newPost);
            }


            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthposts
        return latestPosts;
    }
}
