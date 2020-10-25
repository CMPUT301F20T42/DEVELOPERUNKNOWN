package com.example.developerunknown;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddBookFragment extends Fragment {

    public interface OnFragmentInteractionListener {
        void onOkPressed (Book newBook);
    }


    @Override
    @Nullable
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_book, container, false);


        return view;
        //return inflater.inflate(R.layout.fragment_add_book, container, false);
    }

}
