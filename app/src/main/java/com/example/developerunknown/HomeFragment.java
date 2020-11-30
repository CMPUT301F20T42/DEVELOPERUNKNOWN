package com.example.developerunknown;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
/**
*HomeFragment is used to display notification of user,it is also the default interface when user
 * logged in
 */
public class HomeFragment extends Fragment {
    public ListView notificationList;
    public TextView description;
    public ArrayAdapter<UserNotification> notificationAdapter;
    ArrayList<UserNotification> notificationDataList;
    Context context;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private String uid ;
    private CollectionReference userNotificationCollectionReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
        View view= inflater.inflate(R.layout.fragment_home,container,false);            //initialize view

        context = container.getContext();

        notificationList = view.findViewById(R.id.notificationList);
        description = view.findViewById(R.id.notification);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        userNotificationCollectionReference = db.collection("user").document(uid).collection("Notification");


        notificationDataList = new ArrayList<>();

        notificationAdapter = new NotificationList(context, notificationDataList);
        notificationList.setAdapter(notificationAdapter);

        Log.d("Home Fragment", "Arrived");


        userNotificationCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            /**
             * Retrieves information from Firebase
             * @param queryDocumentSnapshots holds and retrieves information from fB
             * @param error status that holds the exception
             */
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {

                notificationDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String type = (String)doc.getData().get("type");
                    String sender = (String) doc.getData().get("sender");
                    String book = (String) doc.getData().get("book");
                    //Timestamp time = (Timestamp) doc.getData().get("time");
                    String id = doc.getId();
                    notificationDataList.add(new UserNotification(sender, type, book,id) ); // Adding the cities and provinces from FireStore

                    //notificationDataList.add(new UserNotification(sender, time, type, book,id) ); // Adding the cities and provinces from FireStore
                }
                notificationAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetch
                if (notificationDataList.size()==0){
                    description.setText("No New Notification");
                }else{
                    description.setText("Notifications to be dealt with");
                }
            }
        });

        //  if user click the notification,it will be deleted since user already know the information
        notificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                notificationAdapter.getItem(position);          //locate the item that is clicked
                UserNotification currentNote = notificationDataList.get(position);
                String currentNoteID = currentNote.getId();
                userNotificationCollectionReference.document(currentNoteID)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //By clicking the notification, it gets deleted and message is shown
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //If there is an issue, this message is shown
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
            }
            }
        );
        return view;

    }
}
