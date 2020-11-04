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

public class NotificationList extends ArrayAdapter<UserNotification> {
    private ArrayList<UserNotification> userNotifications;
    private Context context;

    public NotificationList(Context context,ArrayList<UserNotification> userNotifications){
        super(context,0,userNotifications);
        this.context=context;
        this.userNotifications=userNotifications;

    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.notification_content,parent,false);
        }

        UserNotification currentNotification = userNotifications.get(position);
        TextView notificationType = view.findViewById(R.id.notificationType);
        TextView notificationDescription = view.findViewById(R.id.Notificationdescription);
        TextView notificationTime = view.findViewById(R.id.notificationTime);

        notificationType.setText(currentNotification.getType());
        notificationDescription.setText(currentNotification.getDescription());

        return view;
    }
}

