package com.example.developerunknown;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * this class is used to display searched user when user enter a specific userName and tap search in AccountFragment
 */
public class SearchUserDialogFragment extends DialogFragment {
    private ImageView resultUserProfile;
    private TextView resultUserName;
    private TextView resultUserFullName;
    private TextView resultUserEmail;
    private TextView resultUserPhone;
    public String resultUid;
    private FragmentActivity myContext;
    public DocumentReference resultUserDocRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage;
    private StorageReference storageReference;
    final Context applicationContext = MainActivity.getContextOfApplication();
    private CollectionReference userCollectionReference = db.collection("user");

    /**
     * method for SearchUserDialogFragment
     * @param uid user id number
     */
    public SearchUserDialogFragment(String uid){
        super();
        this.resultUid = uid;
    }

    @NonNull
    @Override
    /**
     *creates the view of the oncreate dialog
     * @param savedInstanceState current state
     */
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.search_user_result_dialog, null);
        resultUserName = view.findViewById(R.id.searchUserName);
        resultUserFullName = view.findViewById(R.id.searchUserFullName);
        resultUserEmail = view.findViewById(R.id.searchUserContactMail);
        resultUserPhone = view.findViewById(R.id.searchUserContactPhone);
        resultUserProfile = view.findViewById(R.id.imageViewBorrowerBorrowed);
        DocumentReference resultUserDocRef = userCollectionReference.document(resultUid);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        resultUserDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    resultUserName.setText(document.getString("userName"));
                    resultUserFullName.setText(document.getString("firstName")+' '+document.getString("lastName"));
                    resultUserEmail.setText(document.getString("contactEmail"));
                    resultUserPhone.setText(document.getString("contactPhone"));
                    Photographs.viewImage("U", resultUid, storageReference, applicationContext, resultUserProfile);
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




