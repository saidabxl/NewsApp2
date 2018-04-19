package com.example.android.newsapp;

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


public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils() {
    }

    public static List<Article> fetchArticleData(String requestUrl) {

        URL url = createUrl(requestUrl); //Instantiate URL object and execute createURL(@String) method

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            // TODO Handle the IOException
            Log.e(LOG_TAG, "It was not possible to connect to the server", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Article> articles = extractFeatureFromJson(jsonResponse);

        // Return the list of articles
        return articles;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            // TODO: Handle the exception
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
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

    /**
     * Return an {@link Article} object by parsing out information
     * about the first earthquake from the input earthquakeJSON string.
     */
    private static List<Article> extractFeatureFromJson(String articlesJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(articlesJSON)) {
            return null;
        }


        List<Article> articles = new ArrayList<>();


        try {

            JSONObject baseJsonResponse = new JSONObject(articlesJSON);


            JSONObject responseObject = baseJsonResponse.getJSONObject("response");
            JSONArray articlesArray = responseObject.getJSONArray("results");


            if (articlesArray.length() > 0) {


                for (int i = 0; i < articlesArray.length(); i++) {


                    JSONObject articleObject = articlesArray.getJSONObject(i);

                    String section = "";
                    if (articleObject.has("sectionName")) {
                        section = articleObject.getString("sectionName");
                    }

                    String date = "";
                    if (articleObject.has("webPublicationDate")) {
                        date = articleObject.getString("webPublicationDate");
                    }

                    String title = "";
                    if (articleObject.has("webTitle")) {
                        title = articleObject.getString("webTitle");
                    }

                    String url = "";
                    if (articleObject.has("webUrl")) {
                        url = articleObject.getString("webUrl");
                    }


                    String author = "";
                    if (articleObject.has("author")) {
                        author = articleObject.getString("author");
                    }


                    Article article = new Article(section, title, author, date, url);


                    articles.add(article);
                }
            }

        } catch (JSONException e) {

            Log.e(LOG_TAG, "Problem parsing the article JSON results", e);
        }

        //Return the list of articles
        return articles;
    }

}
