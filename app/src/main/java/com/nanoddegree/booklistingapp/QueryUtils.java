package com.nanoddegree.booklistingapp;

import android.text.TextUtils;
import android.util.Log;

import com.nanoddegree.booklistingapp.models.Book;

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


public final class QueryUtils {


  private static final String LOG_TAG = "Log: ";
  private static final int READ_TIMEOUT = 10000;
  private static final int SET_CONNECTION_TIMEOUT = 15000;

  private static final String KEY_ITEMS = "items";
  private static final String KEY_TITLE = "title";
  private static final String KEY_VOLUME_INFO = "volumeInfo";
  private static final String KEY_AUTHORS = "authors";

  private static URL createUrl(String stringUrl) {
    URL url = null;
    try {
      url = new URL(stringUrl);
    } catch (MalformedURLException e) {
      Log.e(LOG_TAG, "Problem building the URL ", e);
    }
    return url;
  }


  private static String makeHttpRequest(URL url) throws IOException {
    String jsonResponse = "";

    if (url == null) {
      return jsonResponse;
    }

    HttpURLConnection urlConnection = null;
    InputStream inputStream = null;
    try {
      urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.setReadTimeout(READ_TIMEOUT);
      urlConnection.setConnectTimeout(SET_CONNECTION_TIMEOUT);
      urlConnection.setRequestMethod("GET");
      urlConnection.connect();

      // If the request was successful (response code 200),
      // then read the input stream and parse the response.
      if (urlConnection.getResponseCode() == 200) {
        inputStream = urlConnection.getInputStream();
        jsonResponse = readFromStream(inputStream);
      } else {
        Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
      }
    } catch (IOException e) {
      Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
    } finally {
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
      if (inputStream != null) {
        inputStream.close();
      }
    }
    return jsonResponse;
  }

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


  private static List<Book> extractFeatureFromJson(String bookJSON) {

    if (TextUtils.isEmpty(bookJSON)) {
      return null;
    }

    List<Book> books = new ArrayList<>();

    try {

      // Create a JSONObject from the JSON response string
      JSONObject baseJsonResponse = new JSONObject(bookJSON);

      if (baseJsonResponse.has(KEY_ITEMS)) {
        JSONArray bookArray = baseJsonResponse.optJSONArray(KEY_ITEMS);

        for (int i = 0; i < bookArray.length(); i++) {

          JSONObject currentBook = bookArray.getJSONObject(i);
          JSONObject volumeInfo = currentBook.getJSONObject(KEY_VOLUME_INFO);

          String title = volumeInfo.getString(KEY_TITLE);
          JSONArray authorArray = volumeInfo.getJSONArray(KEY_AUTHORS);
          String author = authorArray.getString(0);

          Book book = new Book(title, author);

          books.add(book);
        }
      }
    } catch (JSONException e) {
      Log.e("QueryUtils", "Problem parsing the book JSON results", e);
    }

    return books;
  }

  public static List<Book> fetchBookData(String requestUrl) {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    URL url = createUrl(requestUrl);

    String jsonResponse = null;
    try {
      jsonResponse = makeHttpRequest(url);
    } catch (IOException e) {
      Log.e(LOG_TAG, "Problem making the HTTP request.", e);
    }

    List<Book> books = extractFeatureFromJson(jsonResponse);
    return books;
  }

}