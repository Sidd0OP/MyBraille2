package com.example.mybraille;


import static androidx.core.view.ViewCompat.performHapticFeedback;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybraille.character.CharacterController;
import com.example.mybraille.character.PatternController;
import com.example.mybraille.dot.Dot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    //-------------------ui elements----------------------
    private View mainContainer;
    private View bottomBar;

    private View dotContainer;

    private Dot topLeft =  new Dot();
    private Dot topRight = new Dot();
    private Dot centerLeft = new Dot();
    private Dot centerRight = new Dot();
    private Dot bottomLeft = new Dot();
    private Dot bottomRight =  new Dot();

    List<Dot> dotList = new ArrayList<>();

    private TextView characterDisplay;

    //-----------------swipe data--------------------

    private float startX = 0f;
    private final int swipeThreshold = 150;



    //------------------Controller--------------------


    /*
    Braille Dot Index

    0 *  1 *

    2 *  3 *

    4 *  5 *

     */

    CharacterController characterController = new CharacterController();

    PatternController patterController = new PatternController();


    //main method for android
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //hide top menue bar
        getSupportActionBar().hide();



        setContentView(R.layout.activity_main);

        //load root view
        mainContainer = findViewById(R.id.Container);

        //load the bottom bar
        bottomBar = findViewById(R.id.bottomBar);
        dotContainer =  findViewById(R.id.dot_container);
        characterDisplay = findViewById(R.id.current_character_display);

        //initialize dots
        topLeft.setDot(findViewById(R.id.dot_top_left));
        topRight.setDot(findViewById(R.id.dot_top_right));
        centerLeft.setDot(findViewById(R.id.dot_center_left));
        centerRight.setDot(findViewById(R.id.dot_center_right));
        bottomLeft.setDot(findViewById(R.id.dot_bottom_left));
        bottomRight.setDot(findViewById(R.id.dot_bottom_right));

        //add all dots to list
        Collections.addAll(dotList,
                topLeft,
                topRight,
                centerLeft,
                centerRight,
                bottomLeft,
                bottomRight);

        //set patter to A , first
        PatternController.setPattern(0);
        //set the dots
        setDots(PatternController.dotMatrix);

        mainContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_MOVE:

                        float x = e.getRawX();
                        float y = e.getRawY();


                        for(Dot dot : dotList)
                        {
                            if (isTouchInsideView(x, y, dot.getDot())) {

                                if(!dot.isTouched())
                                {
//                                    dot.getDot().setBackground(getDrawable(R.drawable.dot_style_touch));
                                    dot.setTouched(true);

                                    //call haptics

                                }


                            }else{

                                if(dot.isTouched())
                                {
                                    dot.setTouched(false);
//                                    dot.getDot().setBackground(getDrawable(R.drawable.dot_style_no_touch));
                                }
                            }
                        }

                        return true;


                    case MotionEvent.ACTION_UP:


                        for(Dot dot : dotList)
                        {
//                            dot.getDot().setBackground(getDrawable(R.drawable.dot_style_no_touch));
                            dot.setTouched(false);
                        }
                        return true;



                }
                return true;
            }
        });



        bottomBar.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent e) {

                switch (e.getAction())
                {
                        case MotionEvent.ACTION_DOWN:

                            startX = e.getX();
                            return true;

                        case MotionEvent.ACTION_UP:

                            float endX = e.getX();
                            float deltaX = endX - startX;

                            if (Math.abs(deltaX) > swipeThreshold) {
                                // Sufficient swipe distance
                                if (deltaX > 0) {
                                    // left swipe
                                    bottomBarLeftSwipe();

                                } else {
                                    // right swipe
                                    bottomBarRightSwipe();
                                }

                                setDots(PatternController.dotMatrix);
                            }
                            return true;

                    default:
                        return false;
                }

            }
        });


    }


    private void bottomBarLeftSwipe()
    {
        //gets the previous character
        characterController.clockCharacterIndex(-1 , characterDisplay);


    }

    private void bottomBarRightSwipe()
    {
        //gets the next character
        characterController.clockCharacterIndex(1, characterDisplay);

    }


    private boolean isTouchInsideView(float x, float y, View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        Rect rect = new Rect(location[0], location[1], location[0] + view.getWidth(), location[1] + view.getHeight());
        return rect.contains((int) x, (int) y);
    }


    private void setDots(int[][] dotMatrix)
    {
        if(dotMatrix[0][0] == 1)
        {
            dotList.get(0).getDot().setBackground(getDrawable(R.drawable.dot_style_touch));

        }else{dotList.get(0).getDot().setBackground(getDrawable(R.drawable.dot_style_no_touch));}



        if(dotMatrix[0][1] == 1)
        {
            dotList.get(1).getDot().setBackground(getDrawable(R.drawable.dot_style_touch));

        }else{dotList.get(1).getDot().setBackground(getDrawable(R.drawable.dot_style_no_touch));}



        if(dotMatrix[1][0] == 1)
        {
            dotList.get(2).getDot().setBackground(getDrawable(R.drawable.dot_style_touch));

        }else{dotList.get(2).getDot().setBackground(getDrawable(R.drawable.dot_style_no_touch));}


        if(dotMatrix[1][1] == 1)
        {
            dotList.get(3).getDot().setBackground(getDrawable(R.drawable.dot_style_touch));

        }else{dotList.get(3).getDot().setBackground(getDrawable(R.drawable.dot_style_no_touch));}



        if(dotMatrix[2][0] == 1)
        {
            dotList.get(4).getDot().setBackground(getDrawable(R.drawable.dot_style_touch));

        }else{dotList.get(4).getDot().setBackground(getDrawable(R.drawable.dot_style_no_touch));}



        if(dotMatrix[2][1] == 1)
        {
            dotList.get(5).getDot().setBackground(getDrawable(R.drawable.dot_style_touch));

        }else{dotList.get(5).getDot().setBackground(getDrawable(R.drawable.dot_style_no_touch));}

    }


}

