package com.example.developerunknown;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SearchUserDialogFragment extends DialogFragment {
    private Image resultUserProfile;
    private TextView resultUserName;
    private TextView resultUserFullName;
    private TextView resultUserEmail;
    private TextView resultUserPhone;
    public String resultUid;
    private FragmentActivity myContext;
    public DocumentReference resultUserDocRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userCollectionReference = db.collection("user");

    public SearchUserDialogFragment(String uid){
        super();
        this.resultUid = uid;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.search_user_result_dialog, null);
        resultUserName = view.findViewById(R.id.searchUserName);
        resultUserFullName = view.findViewById(R.id.searchUserFullName);
        resultUserEmail = view.findViewById(R.id.searchUserContactMail);
        resultUserPhone = view.findViewById(R.id.searchUserContactPhone);
        DocumentReference resultUserDocRef = userCollectionReference.document(resultUid);

        resultUserDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    resultUserName.setText(document.getString("userName"));
                    resultUserFullName.setText(document.getString("firstName")+' '+document.getString("lastName"));
                    resultUserEmail.setText(document.getString("contactEmail"));
                    resultUserPhone.setText(document.getString("contactPhone"));
                }
                else {
                    Log.d("check userProfile", "user profile error");
                    Toast.makeText(getActivity(), "There is a error showing the profile", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


        return builder
                .setView(view)
                .setTitle("The result user")
                .setPositiveButton("OK", null)
                .create();
        }
    }




