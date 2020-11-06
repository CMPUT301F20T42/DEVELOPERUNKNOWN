package com.example.developerunknown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
/**
 * controls intents of the request function
 */
public class requestActicity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_acticity);

        Intent intent = getIntent();
        Request request = (Request) intent.getSerializableExtra("Request");

        
    }
}