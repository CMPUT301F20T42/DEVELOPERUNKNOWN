package com.example.developerunknown;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {
    ListView notificationList;
    ArrayAdapter<UserNotification> notificationAdapter;
    ArrayList<UserNotification> notificationDataList;
    Context context;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();
    public CollectionReference userNotificationCollectionReference = db.collection("user").document(uid).collection("Notification");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
        View view= inflater.inflate(R.layout.fragment_home,container,false);            //initialize view

        context = container.getContext();

        notificationList = view.findViewById(R.id.notificationList);

        notificationDataList = new ArrayList<>();

        notificationAdapter = new NotificationList(context, notificationDataList);
        notificationList.setAdapter(notificationAdapter);

        userNotificationCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {

                notificationDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String type = (String)doc.getData().get("type");
                    String sender = (String) doc.getData().get("sender");
                    String book = (String) doc.getData().get("book");
                    Timestamp time = (Timestamp) doc.getData().get("time");

                    notificationDataList.add(new UserNotification(sender, time, type, book) ); // Adding the cities and provinces from FireStore
                }
                notificationAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetcheh
            }
        });
        return view;


    }
}
