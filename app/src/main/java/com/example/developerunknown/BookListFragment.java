package com.example.developerunknown;

import android.content.Context;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class BookListFragment extends Fragment implements AddBookFragment.OnFragmentInteractionListener  {
    ListView bookList;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookDataList;
    Context thiscontext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booklist,container,false);
        thiscontext = container.getContext();

        // Add book button
        final FloatingActionButton addBookButton = view.findViewById(R.id.add_book_button);

        bookList = view.findViewById(R.id.user_book_list);
        // TODO: get current user? how to pass this data to fragment?
        // bookAdapter = new ArrayAdapter<Book>(this, ???);

        String []books = {"To Kill a Mocking Bird"};
        String []authors = {"Harper Lee"};
        String []status = {"Available"};
        String []ISBN = {"9780060888695"};
        String []description = {"This is a placeholder"};

        bookDataList = new ArrayList<>();

        bookDataList = new ArrayList<>();

        for (int i = 0; i < books.length; i++) {
            bookDataList.add((new Book(books[i], authors[i], status[i], ISBN[i], description[i])));
        }

        bookAdapter = new CustomList(thiscontext, bookDataList);
        bookList.setAdapter(bookAdapter);

        return view;
    }

    @Nullable
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Get add book button
        FloatingActionButton addBookButton = view.findViewById(R.id.add_book_button);
        addBookButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View v) {
                Fragment fragment = new AddBookFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        });
    }

    @Override
    public void onOkPressed (Book newBook) {

    }
}
