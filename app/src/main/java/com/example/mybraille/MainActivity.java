package com.example.mybraille;


import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{


    //main method for android
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //hide top menue bar
        getSupportActionBar().hide();



        setContentView(R.layout.activity_main);


        View bottomBar = findViewById(R.id.bottomBar);



        bottomBar.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent e) {

                if (e.getAction() == MotionEvent.ACTION_DOWN ) {
                    System.out.println("Inside Bottom Bar");
                    return true;
                }

                return false;
            }
        });

    }



}

