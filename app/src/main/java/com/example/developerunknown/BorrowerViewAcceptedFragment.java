package com.example.developerunknown;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

/**
 *Allows borrower to view books hat they have been accepted from someone else and perform confirm borrow action. This extends the Fragment
 * class and implements on click listeners for response and map functions so that the borrower can
 * see location from a map
 */

public class BorrowerViewAcceptedFragment extends Fragment implements
        android.view.View.OnClickListener, OnMapReadyCallback {
    Context context;
    User currentUser;
    Book clickedBook;
    private GoogleMap mMap;

    MapFragment mapFragment;
  
    String Address;

    public Button confirmBorrowButton;
    public Button backButton;
    public ImageView imageView;


    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookDescription;
    private TextView bookISBN;
    private TextView bookOwner;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();

    public DocumentReference currentBookDocRef;
    public DocumentReference ownerSideCurrentBookRef;
    final Context applicationContext = MainActivity.getContextOfApplication();

    private String borrowDenoted;

    /**
     * initialize currentUser and clickedBook and documentReference before creating the view
     * @param savedInstanceState contains data from previous activity
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = (User) this.getArguments().getSerializable("current user");
        clickedBook = (Book) this.getArguments().getSerializable("clicked book");

        ownerSideCurrentBookRef = db.collection("user").document(clickedBook.getOwnerId()).collection("Book").document(clickedBook.getID());
        currentBookDocRef = db.collection("user").document(uid).collection("AcceptedBook").document(clickedBook.getID());

    }

    @Override
    /**
     * This displays the view of view a accepted Book as a borrower
     * @param inflater creates view
     * @param container contains the layout view
     * @param savedInstanceState contains the recent data
     * @return
     * Return the view of the  Fragment
     */
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentUser = (User) this.getArguments().getSerializable("current user");
        clickedBook = (Book) this.getArguments().getSerializable("clicked book");
        View view = inflater.inflate(R.layout.fragment_borrower_view_accepted, container,false);
        context = container.getContext();


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        currentBookDocRef = db.collection("user").document(uid).collection("AcceptedBook").document(clickedBook.getID());
        ownerSideCurrentBookRef = db.collection("user").document(clickedBook.getOwnerId()).collection("Book").document(clickedBook.getID());

        // Assign buttons
        backButton = view.findViewById(R.id.back);
        confirmBorrowButton = view.findViewById(R.id.borrower_confirm_borrow);

        // Display clicked book
        bookTitle = view.findViewById(R.id.viewTitleBorrowerAccepted);
        bookAuthor = view.findViewById(R.id.viewAuthorBorrowerAccepted);
        bookDescription = view.findViewById(R.id.viewDescriptionBorrowerAccepted);
        bookISBN = view.findViewById(R.id.viewISBNBorrowerAccepted);

        bookOwner = view.findViewById(R.id.acceptedOwner);



        bookTitle.setText(clickedBook.getTitle());
        bookAuthor.setText(clickedBook.getAuthor());
        bookDescription.setText(clickedBook.getDescription());
        bookISBN.setText(clickedBook.getISBN());

        bookOwner.setText("Owner:"+clickedBook.getOwnerUname());

        imageView = view.findViewById(R.id.imageViewBorrowerAccepted);

        mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapp2);
        mapFragment.getMapAsync(this);





        Photographs.viewImage("B", clickedBook.getID(), storageReference, applicationContext, imageView);


    // another version,try to solve the bug that first confirm will be rejected
  //to be tested

        confirmBorrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBookDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                borrowDenoted = document.getString("borrowDenoted");
                                if (borrowDenoted !=null && borrowDenoted.equals("true")) {
                                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                                        Toast.makeText(getActivity(), "You must grant the permission of camera to confirm borrow", Toast.LENGTH_SHORT).show();
                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 445);
                                    } else {
                                        Intent intent = new Intent(getActivity(), Scanner.class);
                                        startActivityForResult(intent, 325);
                                    }
                                }
                                //the book is either first time to be denoted or denoted=="false" currently
                                else {
                                    Toast.makeText(getActivity(), "The owner have not denoted borrow yet", Toast.LENGTH_SHORT).show();

                                }

                            } else {
                                Toast.makeText(getActivity(), "some error occurs", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


            }
        });

        bookOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SearchUserDialogFragment(clickedBook.getOwnerId()).show(getActivity().getSupportFragmentManager(),"borrower profile");

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Destroy Map fragment
                if (mapFragment != null) {
                    getActivity().getFragmentManager().beginTransaction().remove(mapFragment).commit();
                }
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });



        return view;
    }
    /**
     * handles the result by calling scanning activity,check if the return ISBN matches and decide if user action if valid
     * @param requestCode  allowing you to identify who the activity result came from.
     * @param resultCode returned by the child activity
     * @param data  An Intent, which can return result data to the caller
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 325) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("RESULT_ISBN");
                if (result.equals(clickedBook.getISBN())){
                    //borrower confirm borrow
                    Toast.makeText(getActivity(), "You successfully borrowed this book", Toast.LENGTH_SHORT).show();
                    currentBookDocRef.update("borrowDenoted","false");
                    ownerSideCurrentBookRef.update("status","Borrowed");
                    ownerSideCurrentBookRef.update("borrowDenoted","false");

                    // for current user,remove book from AcceptedBook and add to BorrowedBook
                    DocumentReference MyBorrowedBookRef = db.collection("user").document(uid).collection("BorrowedBook").document(clickedBook.getID());
                    Map borrowedBookData = new HashMap<>();
                    borrowedBookData.put("Bookid", clickedBook.getID());
                    borrowedBookData.put("book", clickedBook.getTitle());
                    borrowedBookData.put("ownerUname", clickedBook.getOwnerUname());
                    borrowedBookData.put("ownerId",clickedBook.getOwnerId());
                    borrowedBookData.put("title",clickedBook.getTitle());
                    borrowedBookData.put("description", clickedBook.getDescription());
                    borrowedBookData.put("ISBN",clickedBook.getISBN());
                    borrowedBookData.put("borrower",currentUser.getUsername());
                    borrowedBookData.put("borrowerId", currentUser.getUid());
                    borrowedBookData.put("lat", clickedBook.getLat());
                    borrowedBookData.put("lng", clickedBook.getLon());
                    //use a status to denote is borrower denote return
                    borrowedBookData.put("returnDenoted","false");
                    //also need to handle address
                    MyBorrowedBookRef.set(borrowedBookData);

                    currentBookDocRef.delete();

                }
                else {
                    Toast.makeText(getActivity(), "The ISBN you scaned does not match the ISBN of the book", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    /**
     * override onClick method,do nothing
     * @param view
     */
    @Override
    public void onClick(View view) {

    }

    /**
     * Load geolocation of the meeting location book owner set
     * @param googleMap the google map for locations
     */
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        MarkerOptions markerOptions = new MarkerOptions();
        Log.d("Lat", String.valueOf(clickedBook.getLat()));
        Log.d("Lon", String.valueOf(clickedBook.getLon()));
        markerOptions.position(new LatLng(clickedBook.getLat(), clickedBook.getLon()));

        markerOptions.title(Address);
        mMap.clear();
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                new LatLng(clickedBook.getLat(), clickedBook.getLon()), 16f);
        mMap.animateCamera(location);
        mMap.addMarker(markerOptions);
        Log.d("status", "success");
    }
}
