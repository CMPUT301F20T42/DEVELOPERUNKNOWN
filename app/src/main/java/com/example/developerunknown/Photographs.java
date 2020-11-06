package com.example.developerunknown;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.lang.String;
import java.util.AbstractMap;

import static android.app.Activity.RESULT_OK;

public class Photographs {

    public Photographs() {}

    public static void uploadImage(String type, String id, Uri filePath, StorageReference storageReference, final Context context){
        if (type == "U"){
            if (filePath != null) {

                // Defining the child of storageReference
                StorageReference ref = storageReference.child("profileImages/" + id);


                // adding listeners on upload
                // or failure of image
                ref.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(
                                    UploadTask.TaskSnapshot taskSnapshot)
                            {
                                // Image uploaded successfully
                                Toast.makeText(context, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                            }
                        })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                // Error, Image not uploaded
                                Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                            // Progress Listener for loading
                            // percentage on the dialog box
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(context, "In Progress ", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }else if(type == "B"){
            if (filePath != null) {

                // Defining the child of storageReference
                StorageReference ref = storageReference.child("BookImages/" + id);


                // adding listeners on upload
                // or failure of image
                ref.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(
                                    UploadTask.TaskSnapshot taskSnapshot)
                            {
                                // Image uploaded successfully
                                Toast.makeText(context, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                            }
                        })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                // Error, Image not uploaded
                                Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                            // Progress Listener for loading
                            // percentage on the dialog box
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(context, "In Progress ", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }
    public static void deleteImage(String type, String id, StorageReference storageReference, final FirebaseStorage storage) {
        if (type == "U"){
            storageReference.child("profileImages/"+id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
        }else if(type == "B"){
            storageReference.child("BookImages/"+id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
        }
    }
    public static void viewImage(String type, String id, StorageReference storageReference, final Context context, final ImageView imageView) {
        if (type == "U") {
            storageReference.child("profileImages/" + id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    GlideApp.with(context)
                            .load(uri)
                            .placeholder(new ColorDrawable(Color.GRAY))
                            .error(R.drawable.defaultphoto)
                            .into(imageView);
                }
            });
        } else if (type == "B") {
            storageReference.child("BookImages/" + id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    GlideApp.with(context)
                            .load(uri)
                            .placeholder(new ColorDrawable(Color.GRAY))
                            .error(R.drawable.defaultphoto)
                            .into(imageView);
                }
            });
        }
    }

}
