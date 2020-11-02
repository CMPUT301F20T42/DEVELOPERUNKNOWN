package com.example.developerunknown;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.lang.String;
import java.util.AbstractMap;

public class Photographs {
    private Context context;
    private Drawable defaultPhoto;
    private AbstractMap<String, Drawable> bookPhotoPair;
    private AbstractMap<String, Drawable> userPhotoPair;


    public Photographs(Context current, AbstractMap<String, Drawable> aPhotoPair, String type) {
        this.context = current;
        if (type == "U"){
            this.userPhotoPair = aPhotoPair;
        }
        if (type == "B"){
            this.bookPhotoPair = aPhotoPair;
        }
        defaultPhoto = context.getResources().getDrawable(R.drawable.defaultphoto);
    }

    public AbstractMap<String, Drawable> getBookPhotoPair() {
        return bookPhotoPair;
    }

    public AbstractMap<String, Drawable> getUserPhotoPair() {
        return userPhotoPair;
    }

    public void attachBookPhoto(String a_book, Drawable a_photo){
        bookPhotoPair.put(a_book, a_photo);

    }
    public void deleteBookPhoto(String a_book){
        bookPhotoPair.put(a_book, defaultPhoto);
    }

    public void attachUserPhoto(String a_book, Drawable a_photo){
        userPhotoPair.put(a_book, a_photo);

    }
    public void deleteUserPhoto(String a_book){
        userPhotoPair.put(a_book, defaultPhoto);
    }

    public void viewPhoto(){

    }
}
