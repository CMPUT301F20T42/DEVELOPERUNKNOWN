package com.example.developerunknown;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
/**
 *  Scans book ISBN
 *  this class is inspired from
 *  https://www.youtube.com/watch?v=drH63NpSWyk&t=4s
 *  Citation：
 *  URL:https://github.com/yuriy-budiyev/code-scanner
 *  author：https://stackoverflow.com/users/1992715/android-coder
 *  date:Dec 6,2018
 *  license:MIT License Copyright (c) 2017 Yuriy Budiyev [yuriy.budiyev@yandex.ru]
 */
public class Scanner extends AppCompatActivity {
    private CodeScanner mCodeScanner;


    @Override
    /**
     *initialize everything needed to use camera to scan
     * @param savedInstanceState contains the recent data
     * @return
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);


        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                Scanner.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent();
                        //Toast.makeText(Scanner.this, result.getText(), Toast.LENGTH_SHORT).show();
                        intent.putExtra("RESULT_ISBN", result.getText());
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    /**
     * Continues activity
     */
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    /**
     * pauses activity
     */
    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}
