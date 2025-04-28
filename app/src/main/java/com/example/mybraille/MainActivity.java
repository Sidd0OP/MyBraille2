package com.example.mybraille;


import static androidx.core.view.ViewCompat.performHapticFeedback;

import android.content.Intent;
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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.mybraille.activityManager.ActivityManager;
import com.example.mybraille.character.CharacterController;
import com.example.mybraille.character.PatternController;
import com.example.mybraille.dot.Dot;
import com.example.mybraille.dot.DotController;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    //-------------------ui elements----------------------
    private View mainContainer;

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
    private final int swipeThreshold = 100;



    //------------------Controller--------------------

    CharacterController characterController = new CharacterController();

    DotController dotController = new DotController(this);

    ActivityManager activityManager = new ActivityManager(this);


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
        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView()).setAppearanceLightStatusBars(true);
        setContentView(R.layout.activity_main);

        //bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.item1);

        activityManager.changeActivity(bottomNavigationView);

        //initialize vibrator
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        //load sound into pool
        loadSoundPool();

        //load root view
        mainContainer = findViewById(R.id.Container);

        //load the bottom bar
        View bottomBar = findViewById(R.id.bottomBar);
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
        dotController.setDots(PatternController.dotMatrix , dotList);






        mainContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {


                switch (e.getAction())
                {

                    case MotionEvent.ACTION_MOVE:

                        float x = e.getRawX();
                        float y = e.getRawY();


                        dotController.checkDotTouch(dotList , x , y , vibrator);

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

                                if (deltaX > 0) {
                                    // left swipe
                                    bottomBarLeftSwipe();

                                } else {
                                    // right swipe
                                    bottomBarRightSwipe();
                                }

                                dotController.setDots(PatternController.dotMatrix , dotList);

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
        vibrator.vibrate(50);



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
        vibrator.vibrate(50);

        if(characterController.getCurrentCharacterIndex() >= 0 && characterController.getCurrentCharacterIndex() <= 25)
        {
            playCharacterAudio(characterController.getCurrentCharacterIndex());
        }else{

            playNumberAudio(characterController.getCurrentCharacterIndex());
        }
    }




    private void loadSoundPool()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else {
            soundPool  = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }



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






}

