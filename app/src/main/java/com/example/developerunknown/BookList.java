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

/**
 * This class defines initializes an ArrayList and extendsadapters
 */
public class BookList extends ArrayAdapter<Book> {
    private final Context context;
    private ArrayList<Book> books;

    /**
     * refer to the current objects whose method is being called on
     * @param context what is contained in this class
     * @param books an arraylist
     */
    public BookList (Context context, ArrayList<Book> books) {
        super(context, 0, books);
        this.context = context;
        this.books = books;
    }


    /**
     * Gets view from layout fragment
     * @param position placement within the view
     * @param convertView allows for assignment of new view
     * @param parent original viewgroup
     * @return
     *      Return view
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content,parent,false);
        }

        Book book = books.get(position);

        TextView bookName = view.findViewById(R.id.book_title);
        TextView authorName = view.findViewById(R.id.book_author2);
        TextView bookStatus = view.findViewById(R.id.book_status);
        TextView bookDescription = view.findViewById(R.id.book_description);
        TextView owner = view.findViewById(R.id.book_owner);

        bookName.setText(book.getTitle());
        authorName.setText(book.getAuthor());
        bookStatus.setText(book.getStatus());
        bookDescription.setText(book.getDescription());
        owner.setText(book.getOwnerUname());



        return view;
    }
}