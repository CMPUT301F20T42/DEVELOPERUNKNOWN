package com.example.developerunknown;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class UserBorrowedListFragment extends Fragment {
    ListView bookList;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookDataList;
    Context context;

    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_borrow_book_list,container,false);
        //currentUser = (User) this.getArguments().getSerializable("current user");

        context = container.getContext();
        bookList = view.findViewById(R.id.borrower_booklist);

        String []titles = {"My Dark Places"};
        String []author = {"James Ellroy"};
        String []status = {"Borrowwed"};
        String []ISBN = {"9780099537847"};
        String []description = {"this is place holder"};
        String []id = {"XZPshaw"};

        bookDataList = new ArrayList<>();


        for (int i = 0; i < titles.length; i++) {
            bookDataList.add((new Book(id[i],titles[i], author[i],status[i],ISBN[i],description[i])));
        }

        bookAdapter = new CustomList(context, bookDataList);

        bookList.setAdapter(bookAdapter);





        //bookDataList = currentUser.getBookList();

        //bookAdapter = new CustomList(context, bookDataList);
        //bookList.setAdapter(bookAdapter);



        return view;

    }
}

