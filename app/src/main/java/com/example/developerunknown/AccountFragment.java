package com.example.developerunknown;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userCollectionReference = db.collection("user");
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();;
    public String uid = user.getUid();;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
        View view= inflater.inflate(R.layout.fragment_account,container,false);
        final TextView uNameTextView = view.findViewById(R.id.userName);
        final TextView uEmailTextView = view.findViewById(R.id.userEmail);
        final TextView uFirstNameTextView = view.findViewById(R.id.userFirstName);
        final TextView uLastNameTextView = view.findViewById(R.id.userLastName);

        DocumentReference uidDocRef = userCollectionReference.document(uid);
        uidDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                        uNameTextView.setText(document.getString("userName"));
                        uEmailTextView.setText(document.getString("email"));
                        uFirstNameTextView.setText(document.getString("firstName"));
                        uLastNameTextView.setText(document.getString("lastName"));
                    }
                else {
                        Log.d("check userProfile", "user profile error");
                        Toast.makeText(getActivity(), "There is a error showing the profile", Toast.LENGTH_SHORT).show();
                    }
                }
        });
        return view;
    }
}
