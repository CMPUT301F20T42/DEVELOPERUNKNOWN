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
 * Creates a list of user Notifications
 */
public class NotificationList extends ArrayAdapter<UserNotification> {
    private ArrayList<UserNotification> userNotifications;
    private Context context;

    /**
     * This is a class that keeps track of a list of city objects
     * @param context context of notification list
     * @param userNotifications ArrayList for notifications
     */
    public NotificationList(Context context,ArrayList<UserNotification> userNotifications){
        super(context,0,userNotifications);
        this.context = context;
        this.userNotifications = userNotifications;

    }

    /**
     * initialize the shown text for each notification
     * @param position
     * @param convertView
     * @param parent
     * @return
     * return the view generated
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.notification_content,parent,false);
        }

        //sets notifications to a Textview
        UserNotification currentNotification = userNotifications.get(position);
        TextView notificationType = view.findViewById(R.id.notificationType);
        TextView notificationBook = view.findViewById(R.id.notificationBook);
        TextView notificationSender = view.findViewById(R.id.notificationSender);

        notificationType.setText(currentNotification.getType());
        notificationBook.setText(currentNotification.getBook());
        notificationSender.setText(currentNotification.getSender());

        return view;
    }
}

