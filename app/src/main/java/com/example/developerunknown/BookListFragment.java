package com.example.developerunknown;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BookListFragment extends Fragment {
    ListView bookList;
    ArrayAdapter<Book> bookAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booklist,container,false);

        // Add book button
        final FloatingActionButton addBookButton = view.findViewById(R.id.add_book_button);

        bookList = view.findViewById(R.id.user_book_list);
        // TODO: get current user? how to pass this data to fragment?
        // bookAdapter = new ArrayAdapter<Book>(this, ???);


        return view;
    }

    @Nullable
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Get add book button

    }

}
