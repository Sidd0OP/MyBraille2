package com.example.mybraille;


import static androidx.core.view.ViewCompat.performHapticFeedback;

import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
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
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    //-------------------ui elements----------------------
    private View mainContainer;
    private View bottomBar;

    private View dotContainer;

    private Dot topLeft =  new Dot(0);
    private Dot topRight = new Dot(1);
    private Dot centerLeft = new Dot(2);
    private Dot centerRight = new Dot(3);
    private Dot bottomLeft = new Dot(4);
    private Dot bottomRight =  new Dot(5);

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

    private Vibrator vibrator;


    /// sound player for character and number audio
    private SoundPool soundPool;

    HashMap<String, Integer> soundMap = new HashMap<>();


    //main method for android
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //hide top menue bar
        getSupportActionBar().hide();

        //initialize vibrator
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        //initialize sound pool
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool  = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }





        setContentView(R.layout.activity_main);

        //load sound into pool
        loadSoundPool();

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

                                    dot.setTouched(true);




                                    if(dot.isActive() == true)
                                    {
                                        switch(dot.getCoordinate())
                                        {
                                            case 0:
                                                vibrator.vibrate(50);
                                                break;

                                            case 1:
                                                vibrator.vibrate(30);
                                                break;

                                            case 2:
                                                vibrator.vibrate(45);
                                                break;

                                            case 3:
                                                vibrator.vibrate(26);                                                break;

                                            case 4:
                                                vibrator.vibrate(48);
                                                break;

                                            case 5:
                                                vibrator.vibrate(39);
                                                break;

                                        }

                                    }

                                }


                            }else{

                                if(dot.isTouched())
                                {
                                    dot.setTouched(false);
                                }
                            }
                        }

                        return true;


                    case MotionEvent.ACTION_UP:


                        for(Dot dot : dotList)
                        {
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


        System.out.println(soundMap);

        if(characterController.getCurrentCharacterIndex() >= 0 && characterController.getCurrentCharacterIndex() <= 25)
        {
            playCharacterAudio(characterController.getCurrentCharacterIndex());
        }else{

            playNumberAudio(characterController.getCurrentCharacterIndex());
        }



    }

    private void bottomBarRightSwipe()
    {
        //gets the next character
        characterController.clockCharacterIndex(1, characterDisplay);


        if(characterController.getCurrentCharacterIndex() >= 0 && characterController.getCurrentCharacterIndex() <= 25)
        {
            playCharacterAudio(characterController.getCurrentCharacterIndex());
        }else{

            playNumberAudio(characterController.getCurrentCharacterIndex());
        }
    }


    private boolean isTouchInsideView(float x, float y, View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        Rect rect = new Rect(location[0], location[1], location[0] + view.getWidth(), location[1] + view.getHeight());
        return rect.contains((int) x, (int) y);
    }

    private void loadSoundPool()
    {
        // Load Character A-Z
        for (char c = 'A'; c <= 'Z'; c++) {
            String key = "Character_" + c;
            String resName = "character_" + Character.toLowerCase(c); // Convert to lowercase for res/raw naming
            int resId = getResources().getIdentifier(resName, "raw", getPackageName());

            if (resId != 0) {

                int soundId = soundPool.load(this , resId, 1);
                soundMap.put(key, soundId);
            }
        }



        // Load Number 0-9
        for (int i = 0; i <= 9; i++) {
            String key = "Number_" + i;
            String resName = "number_" + i;
            int resId = getResources().getIdentifier(resName, "raw", getPackageName());

            if (resId != 0) {
                int soundId = soundPool.load(this, resId, 1);
                soundMap.put(key, soundId);
            }
        }

    }
    private void playCharacterAudio(int characterIndex)
    {
        String key  = "Character_" + characterController.indexToCharacter(characterIndex).toUpperCase();
        int soundId = soundMap.get(key);
        if (soundId != 0) {
            soundPool.play(soundId, 1, 1, 0, 0, 1);
        }
    }

    private void playNumberAudio(int numberIndex)
    {
        String key = "Number_" + characterController.indexToCharacter(numberIndex);

        int soundId = soundMap.get(key);
        if (soundId != 0) {
            soundPool.play(soundId, 1, 1, 0, 0, 1);
        }
    }

    private void setDots(int[][] dotMatrix)
    {
        if(dotMatrix[0][0] == 1)
        {
            dotList.get(0).getDot().setBackground(getDrawable(R.drawable.dot_style_touch));
            dotList.get(0).setActive(true);

        }else{
            dotList.get(0).getDot().setBackground(getDrawable(R.drawable.dot_style_no_touch));
            dotList.get(0).setActive(false);
        }



        if(dotMatrix[0][1] == 1)
        {
            dotList.get(1).getDot().setBackground(getDrawable(R.drawable.dot_style_touch));
            dotList.get(1).setActive(true);
        }else{
            dotList.get(1).getDot().setBackground(getDrawable(R.drawable.dot_style_no_touch));
            dotList.get(1).setActive(false);
        }



        if(dotMatrix[1][0] == 1)
        {
            dotList.get(2).getDot().setBackground(getDrawable(R.drawable.dot_style_touch));
            dotList.get(2).setActive(true);


        }else{dotList.get(2).getDot().setBackground(getDrawable(R.drawable.dot_style_no_touch));
            dotList.get(2).setActive(false);
        }


        if(dotMatrix[1][1] == 1)
        {
            dotList.get(3).getDot().setBackground(getDrawable(R.drawable.dot_style_touch));
            dotList.get(3).setActive(true);

        }else{dotList.get(3).getDot().setBackground(getDrawable(R.drawable.dot_style_no_touch));
            dotList.get(3).setActive(false);
        }



        if(dotMatrix[2][0] == 1)
        {
            dotList.get(4).getDot().setBackground(getDrawable(R.drawable.dot_style_touch));
            dotList.get(4).setActive(true);

        }else{dotList.get(4).getDot().setBackground(getDrawable(R.drawable.dot_style_no_touch));
            dotList.get(4).setActive(false);
        }



        if(dotMatrix[2][1] == 1)
        {
            dotList.get(5).getDot().setBackground(getDrawable(R.drawable.dot_style_touch));
            dotList.get(5).setActive(true);

        }else{dotList.get(5).getDot().setBackground(getDrawable(R.drawable.dot_style_no_touch));
            dotList.get(5).setActive(false);
        }

    }


}

