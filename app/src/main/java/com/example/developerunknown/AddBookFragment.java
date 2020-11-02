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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Spinner;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddBookFragment extends Fragment {

    public Button addBookButton;
    public Button cancelButton;

    //new added code
    public Button scanButton;

    public String ISBN;
    private EditText bookTitle;
    private EditText bookAuthor;
    private EditText bookDescription;
    private EditText bookISBN;
    Spinner bookStatus;
    int spinValue;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();
    public CollectionReference userBookCollectionReference = db.collection("user").document(uid).collection("Book");

    Context context;
    User currentUser;

    public interface OnFragmentInteractionListener {
        void onOkPressed (Book newBook);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 325) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("RESULT_ISBN");
                bookISBN = getView().findViewById(R.id.book_isbn_editText);
                bookISBN.setText(result);
            }
        }
    }

    @Override
    @Nullable
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        currentUser = (User) this.getArguments().getSerializable("current user");

        final View view = inflater.inflate(R.layout.fragment_add_book, container, false);

        //initialize add button
        addBookButton = view.findViewById(R.id.add_book_button2);
        cancelButton = view.findViewById(R.id.cancel_book_button);
        bookStatus = view.findViewById(R.id.spinner);


        AdapterView.OnItemSelectedListener onSpinner = new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(
                    AdapterView<?> parent,
                    View view,
                    int position,
                    long id) {
                //assign to 'global' for sending to Book on ok button press
                spinValue = position;
            }

            @Override
            public void onNothingSelected(
                    AdapterView<?>  parent) {
            }
        };

        bookStatus.setOnItemSelectedListener(onSpinner);







        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                bookTitle = view.findViewById(R.id.book_title_editText);
                bookAuthor = view.findViewById(R.id.book_author_editText);
                bookDescription = view.findViewById(R.id.book_description_editText);
                bookISBN = view.findViewById(R.id.book_isbn_editText);
                //bookStatus = view.findViewById(R.id.book_status);

                String title = bookTitle.getText().toString();
                String author = bookAuthor.getText().toString();
                String description = bookDescription.getText().toString();
                String ISBN = bookISBN.getText().toString();
                String status = bookStatus.getSelectedItem().toString();


                if(title.length() > 0 && author.length() > 0 && description.length() > 0 && ISBN.length() > 0 && status.length() > 0)
                {
                    Book book = new Book(title, author,  status, ISBN, description);
                    currentUser.addBook(book);

                    // Add book to book collection
                    HashMap<String, String> data = new HashMap<>();
                    data.put("title", title);
                    data.put("author", author);
                    data.put("description", description);
                    data.put("ISBN", ISBN);
                    data.put("status", status);

                    userBookCollectionReference
                            .document(ISBN)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("create book", "book has been added successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("create book", "ISBN is not be added!" + e.toString());
                                }
                            });

                    destroy_current_fragment();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroy_current_fragment();
            }
        });


        scanButton = view.findViewById(R.id.scan_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED)
                {
                    Toast.makeText(getActivity(), "Scan functionality can work only when CAMERA permission is granded", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 445);
                }
                else {
                    Intent intent = new Intent(getActivity(), Scanner.class);
                    startActivityForResult(intent, 325);
                }
            }
        });

        /*
        scanButton = view.findViewById(R.id.scan_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_CALENDAR)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getActivity(), "Please grand the permission of camera ", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(getActivity(), Scanner.class);
                    startActivity(intent);
                }
            }
        });
*/
        return view;
    }




/*
                // Create dummy user + book data
                // TODO: replace with calls to firestore
                loggedIn = new User("A Admin", "admin", "admin@gmail.com");

                Book b1 = new Book("To Kill A Mockingbird", "Harper Lee", "Available", "123", "The mockingbird dies");
                Book b2 = new Book("1984", "George Orwell", "Borrowed", "1234", "Set in 1983");

                loggedIn.addBook(b1);
                loggedIn.addBook(b2);
*/

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 445) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), Scanner.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(getActivity(), "Scan functionality could not work without CAMERA permission", Toast.LENGTH_SHORT).show();
            }
        }}
*/
        @Nullable
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void destroy_current_fragment() {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragmentManager.findFragmentByTag("Add Book Fragment"));

        Bundle args = new Bundle();
        args.putSerializable("current user", currentUser);
        Fragment fragment = new BookListFragment();
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.fragment_container, fragment, "Booklist Fragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }



}
