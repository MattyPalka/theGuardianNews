package com.apps.palka.matt.theguardiannews;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matt on 15.03.2018.
 */

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils(){
    }

    public static List<Article> fetchArticleData(String requestURL){

        // Create URL object
        URL url = createUrl(requestURL);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Article> articles = extractFeatureFromJson(jsonResponse);

        return articles;
    }

    public static ArrayList<Article> extractFeatureFromJson(String articleJSON){
        if (TextUtils.isEmpty(articleJSON)){
            return null;
        }

        //Create an empty Array that we can start adding Articles to
        ArrayList<Article> articles = new ArrayList<>();

        // Try parse articleJSON if there's a problem with the way JSON if formatted,
        // a JSON exception will be thrown. Catch the exception so that the app doesn't crash
        // and print the error in the logs
        try {
            JSONObject baseJSON = new JSONObject(articleJSON);

            JSONObject responseJSON = baseJSON.getJSONObject("response");

            JSONArray resultsJSON = responseJSON.getJSONArray("results");

            // Loop through all the items in JSON Array
            for (int i=0; i < resultsJSON.length(); i++){
                // store the current JSON object in the o value
                JSONObject o = resultsJSON.getJSONObject(i);

                String sectionName = o.getString("sectionName");
                String articlePublicationDate = o.getString("webPublicationDate");
                String articleTitle = o.getString("webTitle");
                String webUrl = o.getString("webUrl");
                JSONArray tagsArray = o.getJSONArray("tags");
                if (!(tagsArray.length() == 0)){
                    JSONObject tag = tagsArray.getJSONObject(0);
                    String articleAuthor = tag.getString("webTitle");
                    articles.add(new Article(sectionName, articleTitle, articlePublicationDate, webUrl, articleAuthor));
                } else {
                    articles.add(new Article(sectionName, articleTitle, articlePublicationDate, webUrl));
                }



            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        // return the list of articles
        return articles;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {

        final int SERVER_RESPONSE_CODE_OK = 200;
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == SERVER_RESPONSE_CODE_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "url: " + url);
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
