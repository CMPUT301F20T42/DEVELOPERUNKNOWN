package com.example.developerunknown;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
/**
 * This is a class allows user to add book to booklist and extends fragment
 */
public class AddBookFragment extends Fragment {

    private static final int RESULT_LOAD_IMG = 111;
    public Button addBookButton;
    public Button cancelButton;

    // new added code
    public Button scanButton;
    public String id;

    public String ISBN;
    private EditText bookTitle;
    private EditText bookAuthor;
    private EditText bookDescription;
    private EditText bookISBN;
    private Uri filePath;
    ImageView addPhotoButton;

    //declaration to connect to firestore collections
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseStorage storage;
    private StorageReference storageReference;
    public String uid = user.getUid();
    public CollectionReference userBookCollectionReference = db.collection("user").document(uid).collection("Book");

    final Context applicationContext = MainActivity.getContextOfApplication();
    User currentUser;


    /**
     * When the activity you launched exits, it gives you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode  allowing you to identify who the activity result came from.
     * @param resultCode returned by the child activity
     * @param data  An Intent, which can return result data to the caller
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //this is to handle scanning activity result
        if (requestCode == 325) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("RESULT_ISBN");
                bookISBN = getView().findViewById(R.id.book_isbn_editText);
                bookISBN.setText(result);
            }
        }
        //this part is to handle picture selection result
        if (requestCode == RESULT_LOAD_IMG)
            if (resultCode == RESULT_OK) {

                try {
                    // Get the Uri of data
                    filePath = data.getData();
                    final InputStream imageStream = applicationContext.getContentResolver().openInputStream(filePath);
                    final Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                    addPhotoButton.setImageBitmap(bitmap);
                }
                catch (Exception e) {
                    //Messgae that occurs if for some reason the photo can not be added
                    e.printStackTrace();
                    Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show();

                }


            }else{
                //If user do tries to add a photo but then chooses not to
                Toast.makeText(applicationContext, "You haven't picked Image", Toast.LENGTH_SHORT).show();
            }



    }

    @Override
    @Nullable
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {

        currentUser = (User) this.getArguments().getSerializable("current user");

        final View view = inflater.inflate(R.layout.fragment_add_book, container, false);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        //initialize add button

        addBookButton = view.findViewById(R.id.add_book_button2);
        cancelButton = view.findViewById(R.id.cancel_book_button);
        addPhotoButton = view.findViewById(R.id.add_photo_button);


        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bookTitle = view.findViewById(R.id.book_title_editText);
                bookAuthor = view.findViewById(R.id.book_author_editText);
                bookDescription = view.findViewById(R.id.book_description_editText);
                bookISBN = view.findViewById(R.id.book_isbn_editText);

                String title = bookTitle.getText().toString();
                String author = bookAuthor.getText().toString();
                // String status = bookStatus.getSelectedItem().toString();
                String description = bookDescription.getText().toString();
                String ISBN = bookISBN.getText().toString();

                if (title.length() > 0 && author.length() > 0 && description.length() > 0 && ISBN.length() > 0) {
                    if (ISBN.length()!=13){
                        Toast.makeText(getActivity(), "Please check ISBN you enter,it should be" +
                                " 13-digit number", Toast.LENGTH_SHORT).show();
                    }else {
                        // Create new document
                        DocumentReference newRef = userBookCollectionReference.document();
                        id = newRef.getId();
                        Book book = new Book(id, title, author, "Available", ISBN, description, uid, currentUser.getUsername());
                        currentUser.addBook(book);

                        Photographs.uploadImage("B", id, filePath, storageReference, applicationContext);
                        // Add book to book collection
                        HashMap<String, String> data = new HashMap<>();
                        data.put("Bookid", id);
                        data.put("title", title);
                        data.put("author", author);
                        data.put("status", "Available");
                        data.put("description", description);
                        data.put("ISBN", ISBN);
                        data.put("ownerId", uid);
                        data.put("ownerUname", currentUser.getUsername());

                        newRef.set(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("create book", "book has been added successfully!");
                                        //If all fields are filled in, the book will be sucessfully
                                        // added to the list
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("create book", "ISBN has not been added!" + e.toString());
                                        //If superficially an ISBN number has not been entered
                                    }
                                });

                        destroy_current_fragment();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "All fields must not be empty", Toast.LENGTH_SHORT).show();
                    //occurs if user tries to save a book with an empty field
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
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getActivity(), "Scan functionality can work only when CAMERA permission is granded", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 445);
                } else {
                    Intent intent = new Intent(getActivity(), Scanner.class);
                    startActivityForResult(intent, 325);
                }
            }
        });


        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        return view;
    }


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
        /**
         * runs after the view has been created, and adds implementation
         * @param view holds the view/layout of the fragment
         * @param savedInstanceState most current data
         * references view created by onCreateView
         */
        public void onViewCreated ( final View view, @Nullable Bundle savedInstanceState){
            super.onCreate(savedInstanceState);

        }

    /**
     * accesses the fragment manager and pops the current one back into the stack
     */
        public void destroy_current_fragment () {

//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.remove(fragmentManager.findFragmentByTag("Add Book Fragment"));
//
//        Bundle args = new Bundle();
//        args.putSerializable("current user", currentUser);
//        Fragment fragment = new BookListFragment();
//        fragment.setArguments(args);
//        fragmentTransaction.replace(R.id.fragment_container, fragment, "Booklist Fragment");
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        }

    /**
     * Allows user to select a new image
     * @param
     */
        private void selectImage () {
            // Defining Implicit Intent to mobile gallery
            Intent photoPickIntent = new Intent(Intent.ACTION_PICK);
            photoPickIntent.setType("image/*");
            startActivityForResult(photoPickIntent, RESULT_LOAD_IMG);
        }



    }