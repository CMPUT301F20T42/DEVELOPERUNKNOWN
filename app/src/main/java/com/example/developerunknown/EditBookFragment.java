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

public class EditBookFragment extends Fragment {

    private static final int RESULT_LOAD_IMG = 111;

    public Button addBookButton;
    public Button cancelButton;
    public Button deletePhotoButton;
    public ImageView editImageButton;
    public Button editScanButton;

    private EditText bookTitle;
    private EditText bookAuthor;
    private Spinner bookStatus;
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
        bookStatus = view.findViewById(R.id.spinner);
        bookDescription = view.findViewById(R.id.book_description_editText);
        bookISBN = view.findViewById(R.id.book_isbn_editText);

        bookTitle.setText(clickedBook.getTitle());
        bookAuthor.setText(clickedBook.getAuthor());
        bookStatus.setSelection(((ArrayAdapter) bookStatus.getAdapter()).getPosition(clickedBook.getStatus()));
        bookDescription.setText(clickedBook.getDescription());
        bookISBN.setText(clickedBook.getISBN());

        storageReference.child("BookImages/"+clickedBook.getID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                GlideApp.with(applicationContext)
                        .load(uri)
                        .placeholder(new ColorDrawable(Color.GRAY))
                        .error(R.drawable.defaultphoto)
                        .into(editImageButton);
            }
        });

        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();
                String title = bookTitle.getText().toString();
                String author = bookAuthor.getText().toString();
                String status = bookStatus.getSelectedItem().toString();
                String description = bookDescription.getText().toString();
                String ISBN = bookISBN.getText().toString();

                if (title.length() > 0 && author.length() > 0 && description.length() > 0 && ISBN.length() > 0) {
                    // Change book
                    clickedBook.setTitle(title);
                    clickedBook.setAuthor(author);
                    clickedBook.setStatus(status);
                    clickedBook.setDescription(description);
                    clickedBook.setISBN(ISBN);
                    // Update database

                    HashMap<String, String> data = new HashMap<>();
                    data.put("title", title);
                    data.put("author", author);
                    data.put("status", status);
                    data.put("description", description);
                    // What if user updates the ISBN?
                    data.put("ISBN", ISBN);

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
                    Toast.makeText(getActivity(), "Scan functionality can work only when CAMERA permission is granded", Toast.LENGTH_SHORT).show();
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

    public void destroy_current_fragment() {

//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.remove(fragmentManager.findFragmentByTag("Edit Book Fragment"));
//
//        Bundle args = new Bundle();
//        args.putSerializable("current user", currentUser);
//        args.putSerializable("clicked book", clickedBook);
//        Fragment fragment = new ViewBookFragment();
//        fragment.setArguments(args);
//        fragmentTransaction.replace(R.id.fragment_container, fragment, "View Book Fragment");
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }


    private void selectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent photoPickIntent = new Intent(Intent.ACTION_PICK);
        photoPickIntent.setType("image/*");
        startActivityForResult(photoPickIntent, RESULT_LOAD_IMG);
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

    // UploadImage method
    private void uploadImage() {

        if (filePath != null) {

            // Defining the child of storageReference
            StorageReference ref = storageReference.child("BookImages/" + clickedBook.getID());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(
                                UploadTask.TaskSnapshot taskSnapshot) {
                            // Image uploaded successfully
                            Toast.makeText(applicationContext, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded
                            Toast.makeText(applicationContext, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        // Progress Listener for loading
                        // percentage on the dialog box
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(applicationContext, "In Progress ", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}

