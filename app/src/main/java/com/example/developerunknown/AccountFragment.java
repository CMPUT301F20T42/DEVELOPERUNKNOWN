package com.example.developerunknown;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class AccountFragment extends Fragment {
    private static final int RESULT_LOAD_IMG = 111;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userCollectionReference = db.collection("user");
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private Activity activity = getActivity();
    private Uri filePath;
    private ImageButton editImageButton;
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public String uid = user.getUid();
    public String updatedContactEmail;
    public String undatedContactPhone;
    public String contactEmail;
    public String contactPhone;
    public String searchUser;
    public String searchUid; // uid of searched user
    DocumentReference currentUserDocRef = userCollectionReference.document(uid);

    final Context applicationContext = MainActivity.getContextOfApplication();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState){

        View view= inflater.inflate(R.layout.fragment_account,container,false);            //initialize view
        final TextView uNameTextView = view.findViewById(R.id.userName);
        final TextView uEmailTextView = view.findViewById(R.id.userEmail);
        final TextView uFirstNameTextView = view.findViewById(R.id.userFirstName);
        final TextView uLastNameTextView = view.findViewById(R.id.userLastName);
        final Button editInfoButton = view.findViewById(R.id.editInfo);
        final Button confirmEditButton = view.findViewById(R.id.confirmEditProfile);
        final Button searchUserButton = view.findViewById(R.id.searchUserButton);
        editImageButton = view.findViewById(R.id.editImageButton);
        final EditText contactEmailEdit = view.findViewById(R.id.editContactEmail);
        final EditText contactPhoneEdit = view.findViewById(R.id.editContactPhone);
        final EditText searchUserEdit = view.findViewById(R.id.searchUnameEdit);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        confirmEditButton.setVisibility(View.INVISIBLE);
        contactEmailEdit.setEnabled(false);
        contactEmailEdit.setClickable(false);
        contactPhoneEdit.setEnabled(false);
        contactPhoneEdit.setClickable(false);
        editInfoButton.setClickable(false);

        try {
            Glide.with(applicationContext)
                    .load(storageReference.child("profileImages/" + uid))
                    .into(editImageButton);
        }catch (Exception e){
        }


        currentUserDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    uNameTextView.setText(document.getString("userName"));
                    uEmailTextView.setText(document.getString("email"));
                    uFirstNameTextView.setText(document.getString("firstName"));
                    uLastNameTextView.setText(document.getString("lastName"));
                    contactEmailEdit.setText(document.getString("contactEmail"));
                    contactPhoneEdit.setText(document.getString("contactPhone"));
                }
                else {
                    Log.d("check userProfile", "user profile error");
                    Toast.makeText(getActivity(), "There is a error showing the profile", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactEmail = contactEmailEdit.getText().toString();
                contactPhone = contactPhoneEdit.getText().toString();  //current contactEmail and contactPhone,used for backup if update fails
                contactEmailEdit.setEnabled(true);
                contactEmailEdit.setClickable(true);
                contactPhoneEdit.setEnabled(true);
                contactPhoneEdit.setClickable(true);
                editInfoButton.setClickable(true);
                editImageButton.setClickable(true);

                editInfoButton.setVisibility(View.INVISIBLE);           //make sure user could not click edit button during edition
                confirmEditButton.setVisibility(View.VISIBLE);
            }
        });

        confirmEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                updatedContactEmail = contactEmailEdit.getText().toString();
                undatedContactPhone = contactPhoneEdit.getText().toString();
                if (Patterns.PHONE.matcher(undatedContactPhone).matches() && Patterns.EMAIL_ADDRESS.matcher(updatedContactEmail).matches() ) {
                    currentUserDocRef.update("contactEmail", updatedContactEmail,
                            "contactPhone", undatedContactPhone)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                    Toast.makeText(getActivity(), "New contact info is saved", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                    contactEmailEdit.setText(contactEmail);    //use original contact email and contactPhone
                                    contactPhoneEdit.setText(contactPhone);
                                    Toast.makeText(getActivity(), "There is a error updating the contact info", Toast.LENGTH_SHORT).show();

                                }
                            });
                }
                else {
                    Toast.makeText(getActivity(), "Please check format of phone and email", Toast.LENGTH_SHORT).show();
                    contactEmailEdit.setText(contactEmail);    //use original contact email and contactPhone
                    contactPhoneEdit.setText(contactPhone);
                }

                contactEmailEdit.setEnabled(false);
                contactEmailEdit.setClickable(false);
                contactPhoneEdit.setEnabled(false);
                contactPhoneEdit.setClickable(false);
                editInfoButton.setClickable(false);
                editImageButton.setClickable(false);
                confirmEditButton.setVisibility(View.INVISIBLE);
                editInfoButton.setVisibility(View.VISIBLE);
            }
        });

        searchUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUser = searchUserEdit.getText().toString();
                if (searchUser.length()==0){
                    Toast.makeText(getActivity(), "You have to enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }
                CollectionReference userNameCollectionReference = db.collection("userName");
                DocumentReference searchUserDocRef = userNameCollectionReference.document(searchUser);
                searchUserDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("TAG", "found user");
                                searchUid = document.getString("uid");
                                Toast.makeText(getActivity(), "user found", Toast.LENGTH_SHORT).show();
                                new SearchUserDialogFragment(searchUid).show(getActivity().getSupportFragmentManager(),"result user");


                            } else {
                                Log.d("check userName", "user not Exist");
                                Toast.makeText(getActivity(), "user not Exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


            }
        });

        editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        return view;
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
        if (resultCode == RESULT_OK) {

            try {
                // Get the Uri of data
                filePath = data.getData();
                final InputStream imageStream = applicationContext.getContentResolver().openInputStream(filePath);
                final Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                editImageButton.setImageBitmap(bitmap);
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show();
            }


        }else{
            Toast.makeText(applicationContext, "You haven't picked Image", Toast.LENGTH_SHORT).show();
        }
    }
    // UploadImage method
    private void uploadImage()
    {

        if (filePath != null) {

            // Defining the child of storageReference
            StorageReference ref = storageReference.child("profileImages/" + uid);


            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    // Image uploaded successfully
                                    Toast.makeText(applicationContext, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
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