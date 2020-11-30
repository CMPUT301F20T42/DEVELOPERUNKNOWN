package com.example.developerunknown;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Creates fragment to show searchlist view
 */
public class    SearchFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public User currentUser;
    ListView resultList;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> dataList = new ArrayList<>();
    TextView searchBook;
    Button search_button;

    public SearchFragment(User user){this.currentUser = user;}


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_search,container,false);


        resultList = (ListView)view.findViewById(R.id.result_list);

        final Context context= container.getContext();
        bookAdapter = new BookList(context, dataList);

        resultList.setAdapter(bookAdapter);

        searchBook = (TextView)view.findViewById(R.id.editText_book);

        search_button = (Button)view.findViewById(R.id.search);



        //Fetch book date from firestore
        search_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String keyword = searchBook.getText().toString();
                db.collectionGroup("Book").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            dataList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String Description = document.getString("description");
                                String Author = document.getString("author");
                                String Title = document.getString("title");
                                String ISBN = document.getString("ISBN");
                                String Status = document.getString("status");
                                String OwnerId = document.getString("ownerId");
                                String OwnerUname = document.getString("ownerUname");
                                //if (Description.toLowerCase().contains(keyword.toLowerCase()) || title.toLowerCase().contains(keyword..toLowerCase())))
                                if (Status.equals("Available") || Status.equals("Requested")) {
                                    if (Description.toLowerCase().contains(keyword.toLowerCase())|| Title.toLowerCase().contains(keyword.toLowerCase())||Author.toLowerCase().contains(keyword.toLowerCase())) {
                                        Book nowBook = new Book(document.getId(), Title, Author, Status, ISBN, Description, OwnerId, OwnerUname);
                                        dataList.add(nowBook);                      //dataList stores all valid book
                                    }
                                }
                            }
                            bookAdapter = new BookList(context,dataList);
                            resultList.setAdapter(bookAdapter);   //result list is the list view
                        }
                    }
                });
            }
        });
        //select result from list
        resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                Intent intent = new Intent(getActivity(),resultActivity.class);
                Book thisBook = bookAdapter.getItem(pos);

                intent.putExtra("SelectedBook", thisBook);
                intent.putExtra("nowUser", currentUser);
                startActivity(intent);
            }
        });


        return view;
    }

}
