package com.example.developerunknown;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BorrowerViewBorrowedFragment extends Fragment implements
        android.view.View.OnClickListener, OnMapReadyCallback {

    Context context;
    User currentUser;
    Book clickedBook;
    MapView mapView;
    Double Lat;
    Double Lng;
    MapFragment mapFragment;
    LatLng Latlng;
    String Address;
    private GoogleMap mMap;

    public Button denoteReturnButton;
    public Button backButton;
    public ImageView imageView;

    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookDescription;
    private TextView bookISBN;
    private Button bookOwner;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();
    final Context applicationContext = MainActivity.getContextOfApplication();

    public DocumentReference currentBookDocRef;
    public DocumentReference ownerSideCurrentBookRef;

    private String returnDenoted = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = (User) this.getArguments().getSerializable("current user");
        clickedBook = (Book) this.getArguments().getSerializable("clicked book");

        ownerSideCurrentBookRef = db.collection("user").document(clickedBook.getOwnerId()).collection("Book").document(clickedBook.getID());

        currentBookDocRef = db.collection("user").document(uid).collection("BorrowedBook").document(clickedBook.getID());
        /*currentBookDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                System.out.println("here");
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Lat = document.getDouble("lat");
                    Lng = document.getDouble("lng");
                    System.out.println(Lat);
                    Address = document.getString("add");
                }
            }
        });

         */
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        currentUser = (User) this.getArguments().getSerializable("current user");
        clickedBook = (Book) this.getArguments().getSerializable("clicked book");

        View view = inflater.inflate(R.layout.fragment_borrower_view_borrowed, container,false);
        context = container.getContext();
        mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapp3);
        mapFragment.getMapAsync(this);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        // Assign buttons
        backButton = view.findViewById(R.id.back);
        denoteReturnButton = view.findViewById(R.id.borrower_denote_return);

        // Display clicked book
        bookTitle = view.findViewById(R.id.viewTitleBorrowerBorrowed);
        bookAuthor = view.findViewById(R.id.viewAuthorBorrowerBorrowed);
        bookDescription = view.findViewById(R.id.viewDescriptionBorrowerBorrowed);
        bookISBN = view.findViewById(R.id.viewISBNBorrowerBorrowed);
        bookOwner = view.findViewById(R.id.borrowedOwner);



        bookTitle.setText(clickedBook.getTitle());
        bookAuthor.setText(clickedBook.getAuthor());
        bookDescription.setText(clickedBook.getDescription());
        bookISBN.setText(clickedBook.getISBN());
        bookOwner.setText("Owner:"+clickedBook.getOwnerUname());
        imageView = view.findViewById(R.id.imageViewBorrowerBorrowed);

        Photographs.viewImage("B", clickedBook.getID(), storageReference, applicationContext, imageView);
/*
        denoteReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBookDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            returnDenoted = document.getString("returnDenoted");
                        }
                    }
                });
                if (returnDenoted!=null && returnDenoted.equals("true")) {
                    Toast.makeText(getActivity(), "You already denoted borrow before", Toast.LENGTH_SHORT).show();
                }
                //the book is either first time to be denoted or denoted=="false" currently
                else {

                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(getActivity(), "You must grant the permission of camera to confirm return", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 445);
                    } else {
                        Intent intent = new Intent(getActivity(), Scanner.class);
                        startActivityForResult(intent, 325);
                    }
                }

            }
        });
        */


        denoteReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBookDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                returnDenoted = document.getString("returnDenoted");
                                if (returnDenoted!=null && returnDenoted.equals("true")) {
                                    Toast.makeText(getActivity(), "You already denoted borrow before", Toast.LENGTH_SHORT).show();
                                }
                                //the book is either first time to be denoted or denoted=="false" currently
                                else {

                                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                                        Toast.makeText(getActivity(), "You must grant the permission of camera to confirm return", Toast.LENGTH_SHORT).show();
                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 445);
                                    } else {
                                        Intent intent = new Intent(getActivity(), Scanner.class);
                                        startActivityForResult(intent, 325);
                                    }
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
/*
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
*/
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 325) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("RESULT_ISBN");
                if (result.equals(clickedBook.getISBN())){
                    currentBookDocRef.update("returnDenoted","true");
                    ownerSideCurrentBookRef.update("returnDenoted","true");
                    //owner denote borrow
                    Toast.makeText(getActivity(), "Your denote of return is made successfully", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(getActivity(), "The ISBN you scaned does not match the ISBN of the book", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    @Override
    public void onClick(View view) {

    }
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
    /*
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(new LatLng(clickedBook.getLat(), clickedBook.getLon()));

        markerOptions.title(Address);
        mMap.clear();
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                new LatLng(clickedBook.getLat(), clickedBook.getLon()), 16f);
        mMap.animateCamera(location);
        mMap.addMarker(markerOptions);
        Log.d("status", "success");
    }

     */
}
