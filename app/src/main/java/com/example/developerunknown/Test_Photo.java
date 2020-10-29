package com.example.developerunknown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class Test_Photo extends AppCompatActivity {

    public ListView photoList;
    public ArrayAdapter<Drawable> photoAdapter;
    public ArrayList<Drawable> photoDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test__photo);

        photoList = findViewById(R.id.photo_list);

        photoDataList = new ArrayList<>();

        //app:srcCompat="@drawable/defaultPhoto"

        Resources res = getResources();

        Drawable drawable = res.getDrawable(R.drawable.defaultphoto);
        photoDataList.add(drawable);
        ImageView img = (ImageView)findViewById(R.id.imageView);
        img.setImageDrawable(drawable);








    }


}