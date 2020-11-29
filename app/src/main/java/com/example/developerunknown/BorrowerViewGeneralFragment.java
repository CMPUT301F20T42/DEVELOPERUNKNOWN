package com.example.developerunknown;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BorrowerViewGeneralFragment extends Fragment {
    Context context;
    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookISBN;
    private TextView bookDescription;
    private TextView bookStatus;
    private TextView OwnerUname;
    private ImageView BookImage;
    private Button backButton;
    public Book clickedBook;
    public User currentUser;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    final Context applicationContext = MainActivity.getContextOfApplication();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        clickedBook = (Book) this.getArguments().getSerializable("clicked book");
        View view = inflater.inflate(R.layout.general_book_view_borrower, container,false);
        context = container.getContext();


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        // Assign buttons
        backButton = view.findViewById(R.id.back_general);

        // Display clicked book
        bookTitle = view.findViewById(R.id.rbook_title);
        bookAuthor = view.findViewById(R.id.rbook_author);
        bookDescription = view.findViewById(R.id.rbook_description);
        bookISBN = view.findViewById(R.id.rbook_ISBN);
        OwnerUname = view.findViewById(R.id.displayOwner);
        bookStatus = view.findViewById(R.id.rbook_status);
        BookImage = view.findViewById(R.id.BookImage);




        Photographs.viewImage("B", clickedBook.getID(), storageReference, applicationContext, BookImage);

        bookTitle.setText(clickedBook.getTitle());
        bookAuthor.setText(clickedBook.getAuthor());
        bookDescription.setText(clickedBook.getDescription());
        bookISBN.setText(clickedBook.getISBN());
        bookStatus.setText(clickedBook.getStatus());


        OwnerUname.setText("Owner:"+clickedBook.getOwnerUname());


        OwnerUname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SearchUserDialogFragment(clickedBook.getOwnerId()).show(getActivity().getSupportFragmentManager(),"owner profile");

            }
        });



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Destroy Map fragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });



        return view;
    }



}
