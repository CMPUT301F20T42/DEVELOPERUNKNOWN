package com.example.developerunknown;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = (User) this.getArguments().getSerializable("current user");
        clickedBook = (Book) this.getArguments().getSerializable("clicked book");

        ownerSideCurrentBookRef = db.collection("user").document(clickedBook.getOwnerId()).collection("Book").document(clickedBook.getID());
        currentBookDocRef = db.collection("user").document(uid).collection("AcceptedBook").document(clickedBook.getID());
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
        });*/
    }

    @Override
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
/*
        String ownerUnameLink = "Owner:"+clickedBook.getOwnerUname();
        SpannableString ss= new SpannableString(ownerUnameLink);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                new SearchUserDialogFragment(clickedBook.getOwnerId()).show(getActivity().getSupportFragmentManager(),"borrower profile");
            }
            public void updateDrawState(TextPaint ds){
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(true);

            }
        };
        ss.setSpan(clickableSpan1,6, 6+clickedBook.getOwnerUname().length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        bookOwner.setMovementMethod(LinkMovementMethod.getInstance());
*/
        bookOwner.setText("Owner:"+clickedBook.getOwnerUname());

        imageView = view.findViewById(R.id.imageViewBorrowerAccepted);

        mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapp2);
        mapFragment.getMapAsync(this);


        //FragmentManager fragmentManager = getChildFragmentManager();
        //SupportMapFragment mapFragment = new SupportMapFragment();
        //fragmentManager.beginTransaction().replace(R.id.mapp2, mapFragment).commit();
        //mapFragment.getMapAsync(this);
        /*currentBookDocRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot!=null) {
                            String slat = documentSnapshot.getString("lat");
                            Lat = Double.parseDouble(slat);
                            String slng = documentSnapshot.getString("lng");
                            System.out.println(Lat);
                            Lng = Double.parseDouble(slng);
                            Address = documentSnapshot.getString("add");
                            } else {
                        }
                    }
                });*/



        Photographs.viewImage("B", clickedBook.getID(), storageReference, applicationContext, imageView);

/*
        confirmBorrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBookDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            borrowDenoted = document.getString("borrowDenoted");
                        }
                    }
                });
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

            }
        });
*/
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



//        backButton.setOnClickListener(new View.OnClickListener() {
//           @Override
//            public void onClick(View v) {
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                fragmentManager.popBackStack();

//            }


//        });

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

    @Override
    public void onClick(View view) {

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
        markerOptions.position(new LatLng(Lat, Lng));
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                new LatLng(clickedBook.getLat(), clickedBook.getLon()), 16f);
        mMap.animateCamera(location);
        mMap.addMarker(markerOptions);
        Log.d("status", "success");
    }

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
