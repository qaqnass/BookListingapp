package com.nanoddegree.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.nanoddegree.booklistingapp.models.Book;

import java.util.List;


public class BookLoader extends AsyncTaskLoader<List<Book>> {


  private String mUrl;

  public BookLoader(Context context, String url) {
    super(context);
    mUrl = url;
  }

  private static final String LOG_TAG = BookLoader.class.getName();

  @Override
  protected void onStartLoading() {
    forceLoad();
  }

  /**
   * This is on a background thread.
   */
  @Override
  public List<Book> loadInBackground() {
    if (mUrl == null) {
      return null;
    }

    List<Book> books = QueryUtils.fetchBookData(mUrl);
    return books;
  }
}
