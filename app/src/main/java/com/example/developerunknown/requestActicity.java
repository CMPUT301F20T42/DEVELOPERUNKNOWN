package com.example.developerunknown;

import android.Manifest;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * controls intents of the request function
 */
public class requestActicity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMyLocationButtonClickListener, ActivityCompat.OnRequestPermissionsResultCallback{


    TextView Address;
    Request request;
    Book Book;
    User currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GoogleMap mMap;
    String goAddress;
    double latl;
    double lngl;

    Button btn;
    private final static int PLACE_PICKER_REQUEST = 999;
    private final static int LOCATION_REQUEST_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_acticity);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("Location");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        request = (Request) intent.getSerializableExtra("Request");
        Book = (Book) intent.getSerializableExtra("Book");
        currentUser = (User) intent.getSerializableExtra("CurrentUser");

        Address = findViewById(R.id.ac_address);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /*if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }*/
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        22);

                // ACCESS_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


    }

    public void finishIt(View view) {
        goAddress = Address.getText().toString();

        if(goAddress.length() != 0) {

            System.out.println(Book.getOwnerId());
            System.out.println(Book.getID());


            DocumentReference currentBookRef = db.collection("user").document(Book.getOwnerId()).collection("Book").document(Book.getID());


            //update the borrower info in this clicked book

            currentBookRef.update("borrowerID", request.getBorrowerID());
            currentBookRef.update("borrowerUname", request.getBorrowerUname());
            currentBookRef.update("status", "Accepted");

            //notify the user who are accepted,and update his "AcceptedBook" in database


            //update accepted book list for borrower
            DocumentReference borrowerAcceptedBookRef = db.collection("user").document(request.getBorrowerID()).collection("AcceptedBook").document(Book.getID());
            Map acceptedBookData = new HashMap<>();
            acceptedBookData.put("Bookid", Book.getID());
            acceptedBookData.put("book", Book.getTitle());
            acceptedBookData.put("ownerUname", Book.getOwnerUname());
            acceptedBookData.put("ownerId", Book.getOwnerId());
            acceptedBookData.put("title", Book.getTitle());
            acceptedBookData.put("description", Book.getDescription());
            acceptedBookData.put("ISBN", Book.getISBN());
            acceptedBookData.put("borrower", request.getBorrowerUname());
            acceptedBookData.put("borrowerId", request.getBorrowerID());
            acceptedBookData.put("add", goAddress);
            acceptedBookData.put("lat", latl);
            acceptedBookData.put("lng", lngl);
            borrowerAcceptedBookRef.set(acceptedBookData);


            //send notification

            DocumentReference userNotificationRef = db.collection("user").document(Book.getOwnerId()).collection("Notification").document();
            String notificationId = userNotificationRef.getId();
            Map acceptNotiData = new HashMap<>();
            acceptNotiData.put("sender", currentUser.getUid());
            acceptNotiData.put("type", "Accepted");
            acceptNotiData.put("book", Book.getTitle());
            acceptNotiData.put("id", notificationId);
            acceptNotiData.put("add", goAddress);


            //retrieve all request,accept one request and reject the rest
            CollectionReference requestCollectionRef = currentBookRef.collection("Request");

            requestCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String requesterID = document.getString("Borrower");
                            String documentId = document.getId();

                            if (!requesterID.equals(request.getBorrowerID())) {        //if not the user we accept,deny and send notification
                                //send deny notification
                                DocumentReference userNotificationRef = db.collection("user").document(requesterID).collection("Notification").document();
                                String notificationId = userNotificationRef.getId();
                                Map notiData = new HashMap<>();
                                notiData.put("sender", currentUser.getUid());
                                notiData.put("type", "Denied");
                                notiData.put("book", Book.getTitle());
                                notiData.put("id", notificationId);

                            }

                            //clear data from "RequestedBook" of users since some user is accepted
                            DocumentReference borrowerRequestedBookRef = db.collection("user").document(requesterID).collection("RequestedBook")
                                    .document(Book.getID());
                            borrowerRequestedBookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            document.getReference().delete();
                                        } else {

                                        }
                                    }
                                }
                            });
                            //clear the request,no matter accepted one or denied one
                            document.getReference().delete();
                        }
                    }
                }
            });
            Toast.makeText(requestActicity.this, "All done!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(requestActicity.this, "You should enter an address", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        enableMyLocation();
                    }

                    /*mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                        @Override
                        public void onMyLocationChange(Location location) {
                            LatLng ltlng=new LatLng(location.getLatitude(),location.getLongitude());
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                    ltlng, 16f);
                            mMap.animateCamera(cameraUpdate);
                        }
                    });
                    Location location = mMap.getMyLocation();*/

                    /*mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);

                            markerOptions.title(getAddress(latLng));

                            mMap.clear();
                            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                                    latLng, 15);
                            mMap.animateCamera(location);
                            mMap.addMarker(markerOptions);
                        }
                    });*/



                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

    }

    @Override
    public void onMapClick(LatLng point) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);

        markerOptions.title(getAddress(point));

        mMap.clear();
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                point, 15);
        mMap.animateCamera(location);
        mMap.addMarker(markerOptions);
    }

    private String getAddress(LatLng latLng){

        Geocoder geocoder;
        List<android.location.Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {

                ft.remove(prev);
            }
            ft.addToBackStack(null);
            DialogFragment dialogFragment = new ConfirmAddress();

            Bundle args = new Bundle();
            args.putDouble("lat", latLng.latitude);
            latl = latLng.latitude;
            args.putDouble("long", latLng.longitude);
            lngl = latLng.longitude;
            args.putString("address", address);
            dialogFragment.setArguments(args);
            dialogFragment.show(ft, "dialog");
            goAddress = address;
            Address.setText(goAddress);
            return address;
        } catch (IOException e) {
            e.printStackTrace();
            return "No Address Found";

        }


    }
    private void enableMyLocation() {
        // [START maps_check_location_permission]
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            /*PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);*/
        }
        // [END maps_check_location_permission]
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}