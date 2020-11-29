package com.example.developerunknown;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Similar to BookList- extends ArrayAdapter
 */
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
        TextView bookStatus = view.findViewById(R.id.book_status);
        TextView bookDescription = view.findViewById(R.id.book_description);
        TextView bookBorrower = view.findViewById(R.id.book_borrower);
        TextView owner = view.findViewById(R.id.book_owner);

        bookName.setText(book.getTitle());
        authorName.setText(book.getAuthor());
        bookStatus.setText(book.getStatus());
        bookDescription.setText(book.getDescription());
        owner.setText(book.getOwnerUname());

        if (book.getBorrowerUname() != null) {
            bookBorrower.setText(book.getBorrowerUname());
        }


        return view;
    }
}