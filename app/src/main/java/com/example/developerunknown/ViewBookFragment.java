package com.example.developerunknown;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ViewBookFragment extends Fragment {
    Context context;
    User currentUser;
    Book clickedBook;

    private TextView bookTitle;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentUser = (User) this.getArguments().getSerializable("current user");
        clickedBook = (Book) this.getArguments().getSerializable("clicked book");

        View view = inflater.inflate(R.layout.fragment_view_book, container,false);
        context = container.getContext();

        // Display clicked book
        bookTitle = view.findViewById(R.id.viewTitle);

        bookTitle.setText(clickedBook.getTitle());

        return view;
    }
}
