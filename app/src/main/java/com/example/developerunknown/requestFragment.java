package com.example.developerunknown;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link requestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class requestFragment extends DialogFragment {
    private TextView Title;
    private TextView Author;
    private Book nowBook;
    private Request nowRequest;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public requestFragment() {
        // Required empty public constructor
    }

    public requestFragment(Book nowBook, Request nowRequest) {
        this.nowBook = nowBook;
        this.nowRequest = nowRequest;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment requestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static requestFragment newInstance(String param1, String param2) {
        requestFragment fragment = new requestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_request, null);
        Title = view.findViewById(R.id.rf_title);

        String dialogTitle = "You got an request from" + nowRequest.getBorrowerUname();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(dialogTitle)
                .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Todo: Delete request from firebase
                    }})
                .setPositiveButton("OK I accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(),requestActicity.class);
                        intent.putExtra("Request", nowRequest);
                        intent.putExtra("Book", nowBook);
                        startActivity(intent);
                    }}).create();
    }
}
