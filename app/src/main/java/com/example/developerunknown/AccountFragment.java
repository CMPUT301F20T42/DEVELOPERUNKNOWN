package com.example.developerunknown;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButtonToggleGroup;
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

import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class AccountFragment extends Fragment {
    private final int PICK_IMAGE_REQUEST = 22;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userCollectionReference = db.collection("user");
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private Uri filePath;
    private ImageButton imageButton;
    private Activity activity = getActivity();
    FirebaseStorage storage;
    StorageReference storageReference;
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();
    public String updatedContactEmail;
    public String undatedContactPhone;
    public String contactEmail;
    public String contactPhone;
    public String searchUser;
    public String searchUid; // uid of searched user
    DocumentReference currentUserDocRef = userCollectionReference.document(uid);


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
        View view= inflater.inflate(R.layout.fragment_account,container,false);
        final TextView uNameTextView = view.findViewById(R.id.userName);
        final TextView uEmailTextView = view.findViewById(R.id.userEmail);
        final TextView uFirstNameTextView = view.findViewById(R.id.userFirstName);
        final TextView uLastNameTextView = view.findViewById(R.id.userLastName);
        final Button editInfoButton = view.findViewById(R.id.editInfo);
        final Button confirmEditButton = view.findViewById(R.id.confirmEditProfile);
        final Button searchUserButton = view.findViewById(R.id.searchUserButton);
        final EditText contactEmailEdit = view.findViewById(R.id.editContactEmail);
        final EditText contactPhoneEdit = view.findViewById(R.id.editContactPhone);
        final EditText searchUserEdit = view.findViewById(R.id.searchUnameEdit);
        final Button editImageButton = view.findViewById(R.id.editImageButton);

        confirmEditButton.setVisibility(View.INVISIBLE);
        contactEmailEdit.setEnabled(false);
        contactEmailEdit.setClickable(false);
        contactPhoneEdit.setEnabled(false);
        contactPhoneEdit.setClickable(false);
        editImageButton.setClickable(false);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

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
                editImageButton.setClickable(true);
                editInfoButton.setVisibility(View.INVISIBLE);
                confirmEditButton.setVisibility(View.VISIBLE);
            }
        });


        confirmEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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



        return view;
    }

    /*=====================================================================
        editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //selectImage();
            }
        });
         */

    /*=====================================================
    private void selectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    /*===========================================================
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code 
        // if request code is PICK_IMAGE_REQUEST and 
        // resultCode is RESULT_OK 
        // then set image in the image view 
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data 
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = ImageDecoder
                        .decodeBitmap(ImageDecoder.createSource(activity.getContentResolver(), filePath));
                imageButton.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception 
                e.printStackTrace();
            }
        }
    }

    // UploadImage method 
    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading 
            final ProgressDialog progressDialog
                    = new ProgressDialog(activity);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference 
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload 
            // or failure of image 
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully 
                                    // Dismiss dialog 
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(activity,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded 
                            progressDialog.dismiss();
                            Toast
                                    .makeText(activity,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading 
                                // percentage on the dialog box 
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }*/

}
