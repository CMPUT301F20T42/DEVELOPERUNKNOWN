package com.example.developerunknown;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class CustomList extends ArrayAdapter<Book> {
    private final Context context;
    private ArrayList<Book> books;
    //private Context context;

    public CustomList (Context context, ArrayList<Book> books) {
        super(context, 0, books);
        this.context = context;
        this.books = books;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content,parent,false);
        }

        Book book = books.get(position);

        TextView bookName = view.findViewById(R.id.book_title);
        TextView authorName = view.findViewById(R.id.book_author2);
        TextView description = view.findViewById(R.id.book_description);
        TextView status = view.findViewById(R.id.book_status);
        //TextView ISBN = view.findViewById(R.id.book_isbn_editText);

        bookName.setText(book.getTitle());
        authorName.setText(book.getAuthor());
        description.setText(book.getDescription());
        status.setText(book.getAvailability());
        //ISBN.setText(book.getISBN());


        return view;
    }
}