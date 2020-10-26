package com.example.developerunknown;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchFragment extends Fragment {

    ArrayAdapter<String> bookAdapter;
    ArrayList<String> dataList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
        String []books ={"To kill a mockingbird", "Indian Horse", "1984", "Greenlight"};

        ListView resultList;
        resultList = (ListView)this.getActivity().findViewById(R.id.result_list);

        dataList.addAll(Arrays.asList(books));

        bookAdapter = new ArrayAdapter<>(getActivity(), R.layout.content_search, dataList);

        resultList.setAdapter(bookAdapter);



        return inflater.inflate(R.layout.fragment_search,container,false);
    }
}
