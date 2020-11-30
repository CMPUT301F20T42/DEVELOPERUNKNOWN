package com.example.developerunknown;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/**
 * holds the view for the requestlist
 */
public class RequestList extends ArrayAdapter<Request> {
    private ArrayList<Request> requests;
    private Context context;


    /**
     * creates references to the current object
     * @param context current app environment
     * @param requests requests for books
     */
    public RequestList(Context context, ArrayList<Request> requests){
        super(context,0, requests);
        this.context = context;
        this.requests = requests;

    }
    /**
     * initialize the text shown in each request,basically just the username of requester
     * @param position the index of clicked request
     * @param convertView the convertView
     * @param parent ViewGroup
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.request_content, parent,false);
        }

        Request request = requests.get(position);
        TextView requestSender = view.findViewById(R.id.request_sender);

        requestSender.setText(request.getBorrowerUname());

        return view;
    }
}

