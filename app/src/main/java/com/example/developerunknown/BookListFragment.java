package com.example.developerunknown;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import com.example.developerunknown.AddBookFragment;

public class BookListFragment extends Fragment implements AddBookFragment.OnFragmentInteractionListener  {
    ListView bookList;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookDataList;
    Context context;
    User currentUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentUser = (User) this.getArguments().getSerializable("current user");

        View view = inflater.inflate(R.layout.fragment_booklist,container,false);
        context = container.getContext();

        bookList = view.findViewById(R.id.user_book_list);

        bookDataList = currentUser.getBookList();

        bookAdapter = new CustomList(context, bookDataList);
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
                Bundle args = new Bundle();
                args.putSerializable("current user", currentUser);

                Fragment fragment = new AddBookFragment();
                fragment.setArguments(args);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment, "Add Book Fragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        });

        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Book clickedBook = bookDataList.get(position);

                Bundle args = new Bundle();
                args.putSerializable("current user", currentUser);
                args.putSerializable("clicked book", clickedBook);

                Fragment fragment = new ViewBookFragment();
                fragment.setArguments(args);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment, "View Book Fragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onOkPressed (Book newBook) {

    }
}
