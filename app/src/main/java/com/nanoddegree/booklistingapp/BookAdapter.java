package com.nanoddegree.booklistingapp;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanoddegree.booklistingapp.models.Book;

import java.util.ArrayList;

/**
 * Created by qaqnass on 11/10/17.
 */

public class BookAdapter extends ArrayAdapter<Book> {

  public BookAdapter(Context context, ArrayList<Book> arrayList) {
    super(context, 0, arrayList);
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    View listItemView = convertView;

    if (listItemView == null) {
      listItemView = LayoutInflater.from(getContext()).inflate(
          R.layout.list_book, parent, false);
    }

    Book book = getItem(position);

    TextView title = (TextView) listItemView.findViewById(R.id.bookTitle);
    title.setText(book.getTitle());

    TextView author = (TextView) listItemView.findViewById(R.id.bookAuthor);
    author.setText(book.getAuthor());

    return listItemView;
  }
}
