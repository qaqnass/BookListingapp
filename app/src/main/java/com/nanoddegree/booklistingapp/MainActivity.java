package com.nanoddegree.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nanoddegree.booklistingapp.models.Book;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

  public static final String LOG_TAG = MainActivity.class.getName();

  private TextView warningMessage;
  private ProgressBar progressBar;

  private TextView searchInput;
  private Button  submitSearch;

  private static final int EARTHQUAKE_LOADER_ID = 1;

  private static  String url =
      "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=10";

  BookAdapter bookAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ListView bookListView = (ListView) findViewById(R.id.list_books);

    warningMessage = (TextView) findViewById(R.id.warning_message);
    bookListView.setEmptyView(warningMessage);

    progressBar = (ProgressBar) findViewById(R.id.loading_spinner);

    searchInput = (TextView) findViewById(R.id.searchInput);
    submitSearch = (Button) findViewById(R.id.submitBook);
    submitSearch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String getSearchInput = String.valueOf(searchInput.getText());
        url = "https://www.googleapis.com/books/v1/volumes?q="+ getSearchInput+ "&maxResults=1";

        Log.i("dddddddddddddddddddddddddd", url+"");
        new BookLoader(MainActivity.this, url);
      }
    });

    bookAdapter = new BookAdapter(this, new ArrayList<Book>());

    bookListView.setAdapter(bookAdapter);

    // Get a reference to the ConnectivityManager to check state of network connectivity
    ConnectivityManager connMgr = (ConnectivityManager)
        getSystemService(Context.CONNECTIVITY_SERVICE);

    // Get details on the currently active default data network
    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


    if (networkInfo != null && networkInfo.isConnected()) {
      LoaderManager loaderManager = getLoaderManager();

      loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
    } else {
      View loadingIndicator = findViewById(R.id.loading_spinner);
      loadingIndicator.setVisibility(View.GONE);

      warningMessage.setText(R.string.no_internet);
    }
  }


  @Override
  public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
    return new BookLoader(this, url);
  }

  @Override
  public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
    progressBar.setVisibility(View.GONE);
    warningMessage.setText(R.string.no_book);
    bookAdapter.clear();
    if (books != null && !books.isEmpty()) {
      bookAdapter.addAll(books);
    }
  }

  @Override
  public void onLoaderReset(Loader<List<Book>> loader) {
    bookAdapter.clear();
  }

}
