package com.example.developerunknown;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ViewBookFragment extends Fragment {
    Context context;
    User currentUser;
    Book clickedBook;

    public Button editBookButton;
    public Button deleteBookButton;
    public Button backButton;
    public ImageView imageView;

    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookDescription;
    private TextView bookISBN;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();
    public CollectionReference userBookCollectionReference = db.collection("user").document(uid).collection("Book");
    final Context applicationContext = MainActivity.getContextOfApplication();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("ViewBook Message", "Successfully entered fragment");

        currentUser = (User) this.getArguments().getSerializable("current user");
        clickedBook = (Book) this.getArguments().getSerializable("clicked book");

        View view = inflater.inflate(R.layout.fragment_view_book, container,false);
        context = container.getContext();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Assign buttons
        editBookButton = view.findViewById(R.id.editButton);
        deleteBookButton = view.findViewById(R.id.deleteButton);
        backButton = view.findViewById(R.id.backButton);

        // Display clicked book
        bookTitle = view.findViewById(R.id.viewTitle);
        bookAuthor = view.findViewById(R.id.viewAuthor);
        bookDescription = view.findViewById(R.id.viewDescription);
        bookISBN = view.findViewById(R.id.viewISBN);

        bookTitle.setText(clickedBook.getTitle());
        bookAuthor.setText(clickedBook.getAuthor());
        bookDescription.setText(clickedBook.getDescription());
        bookISBN.setText(clickedBook.getISBN());
        imageView = view.findViewById(R.id.imageView);

        storageReference.child("BookImages/"+clickedBook.getID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                GlideApp.with(applicationContext)
                        .load(uri)
                        .placeholder(new ColorDrawable(Color.GRAY))
                        .error(R.drawable.defaultphoto)
                        .into(imageView);
            }
        });

        editBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("View Book Fragment", "Clicked on Edit Button");
                // Create new fragment
                Bundle args = new Bundle();
                args.putSerializable("current user", currentUser);
                args.putSerializable("clicked book", clickedBook);

                Fragment fragment = new EditBookFragment();
                fragment.setArguments(args);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment, "Edit Book Fragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        deleteBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete book from user collection
                Log.d("delete book", "delete button clicked");
                userBookCollectionReference
                        .document(clickedBook.getID())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("delete book", "book has been deleted successfully!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("delete book", "ISBN is not be deleted! " + e.toString());
                            }
                        });

                storageReference.child("BookImages/"+clickedBook.getID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        StorageReference photoRef = storage.getReferenceFromUrl(uri.toString());
                        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // File deleted successfully
                                Log.d("delete photo", "onSuccess: deleted file");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Uh-oh, an error occurred!
                                Log.d("delete photo", "onFailure: did not delete file");
                            }
                        });
                    }
                });

                // Close fragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        return view;
    }

}
