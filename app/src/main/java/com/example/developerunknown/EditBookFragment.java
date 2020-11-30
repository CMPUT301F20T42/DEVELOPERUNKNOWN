package com.example.developerunknown;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


/**
 * allow user to edit a book that belongs to them
 */
public class EditBookFragment extends Fragment {

    private static final int RESULT_LOAD_IMG = 111;

    public Button addBookButton;
    public Button cancelButton;
    public Button deletePhotoButton;
    public ImageView editImageButton;
    public Button editScanButton;

    private EditText bookTitle;
    private EditText bookAuthor;
    private EditText bookDescription;
    private EditText bookISBN;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();
    public CollectionReference userBookCollectionReference = db.collection("user").document(uid).collection("Book");

    private Uri filePath;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    final Context applicationContext = MainActivity.getContextOfApplication();

    Context context;
    User currentUser;
    Book clickedBook;

    @Override
    @Nullable
    /**
     * This displays the view some information as EditText and allow user to change information and image then save them to database
     * @param inflater creates view
     * @param container contains the layout view
     * @param savedInstanceState contains the recent data
     * @return
     * Return the view of the Fragment
     */
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        currentUser = (User) this.getArguments().getSerializable("current user");
        clickedBook = (Book) this.getArguments().getSerializable("clicked book");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        final View view = inflater.inflate(R.layout.fragment_edit_book, container, false);

        // initialize add button
        addBookButton = view.findViewById(R.id.add_book_button2);
        cancelButton = view.findViewById(R.id.cancel_book_button);
        editImageButton = view.findViewById(R.id.editImageButton);
        deletePhotoButton = view.findViewById(R.id.deletePhotoButton);
        editScanButton = view.findViewById(R.id.scan_button);

        bookTitle = view.findViewById(R.id.book_title_editText);
        bookAuthor = view.findViewById(R.id.book_author_editText);
        bookDescription = view.findViewById(R.id.book_description_editText);
        bookISBN = view.findViewById(R.id.book_isbn_editText);

        bookTitle.setText(clickedBook.getTitle());
        bookAuthor.setText(clickedBook.getAuthor());
        bookDescription.setText(clickedBook.getDescription());
        bookISBN.setText(clickedBook.getISBN());


        Photographs.viewImage("B", clickedBook.getID(), storageReference, applicationContext, editImageButton);


        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Photographs.uploadImage("B", clickedBook.getID(), filePath, storageReference, applicationContext);
                String title = bookTitle.getText().toString();
                String author = bookAuthor.getText().toString();
                String description = bookDescription.getText().toString();
                String ISBN = bookISBN.getText().toString();

                if (title.length() > 0 && author.length() > 0 && description.length() > 0 && ISBN.length() > 0) {
                    if(ISBN.length()!=13){
                        Toast.makeText(getActivity(), "Please check ISBN you enter,it should be 13-digit number", Toast.LENGTH_SHORT).show();
                    }
                    else{
                    // Change book
                    clickedBook.setTitle(title);
                    clickedBook.setAuthor(author);
                    clickedBook.setStatus("Available");
                    clickedBook.setDescription(description);
                    clickedBook.setISBN(ISBN);
                    // Update database

                    HashMap<String, String> data = new HashMap<>();
                    data.put("title", title);
                    data.put("author", author);
                    data.put("status", "Available");
                    data.put("description", description);
                    // What if user updates the ISBN?
                    data.put("ISBN", ISBN);
                    data.put("Bookid", clickedBook.getID());
                    data.put("ownerId", currentUser.getUid());
                    data.put("ownerUname", currentUser.getUsername());


                    userBookCollectionReference
                            .document(clickedBook.getID())
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("edit book", "book has been edited successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("edit book", "ISBN is not be edited!" + e.toString());
                                }
                            });

                    destroy_current_fragment();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "All fields must not be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroy_current_fragment();
            }
        });

        editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        editScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getActivity(), "Scan functionality can work only when CAMERA permission is granted", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 445);
                } else {
                    Intent intent = new Intent(getActivity(), Scanner.class);
                    startActivityForResult(intent, 325);
                }
            }
        });
        deletePhotoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Photographs.deleteImage("B", clickedBook.getID(), storageReference, storage);
                editImageButton.setImageResource(android.R.color.transparent);
            }
        });

        return view;
    }

    @Nullable
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * destory current fragment and jump back
     */
    public void destroy_current_fragment() {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    /**
     * allow user to pick a image from local gallery
     */
    private void selectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent photoPickIntent = new Intent(Intent.ACTION_PICK);
        photoPickIntent.setType("image/*");
        startActivityForResult(photoPickIntent, RESULT_LOAD_IMG);
    }

    /**
     * handle the result by using Scanner or ImagePicker
     * When the activity you launched exits, it gives you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode  allowing you to identify who the activity result came from.
     * @param resultCode returned by the child activity
     * @param data  An Intent, which can return result data to the caller
     */
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


        if (requestCode == RESULT_LOAD_IMG) {
            if (resultCode == RESULT_OK) {

                try {
                    // Get the Uri of data
                    filePath = data.getData();
                    final InputStream imageStream = applicationContext.getContentResolver().openInputStream(filePath);
                    final Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                    editImageButton.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show();
                }


            } else {
                Toast.makeText(applicationContext, "You haven't picked Image", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

