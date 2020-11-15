package com.example.developerunknown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * controls intents of the request function
 */
public class requestActicity extends AppCompatActivity {
    TextView Address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_acticity);

        Intent intent = getIntent();
        Request request = (Request) intent.getSerializableExtra("Request");

        Address = findViewById(R.id.ac_address);


    }
    public void finishIt(View view){
        String goAddress = Address.getText().toString();
    }
}