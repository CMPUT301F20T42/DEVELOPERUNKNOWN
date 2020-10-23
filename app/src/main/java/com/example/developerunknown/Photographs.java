package com.example.developerunknown;
import android.media.Image;

import java.lang.String;
import java.util.AbstractMap;

public class Photographs {
    private Image defaultPhoto;
    private AbstractMap<String, Image> bookPhotoPair;

    public Photographs(Image defaultPhoto, AbstractMap<String, Image> bookPhotoPair) {
        this.defaultPhoto = defaultPhoto;
        this.bookPhotoPair = bookPhotoPair;
    }

    public AbstractMap<String, Image> getBookPhotoPair() {
        return bookPhotoPair;
    }

    public void attachPhoto(String a_book, Image a_photo){
        bookPhotoPair.put(a_book, a_photo);

    }
    public void deletePhoto(String a_book){
        bookPhotoPair.put(a_book, defaultPhoto);
    }

    public void viewPhoto(){

    }
}
