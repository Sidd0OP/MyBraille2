package com.example.mybraille;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybraille.activityManager.ActivityManager;
import com.example.mybraille.card.RecycleViewAdapter;
import com.example.mybraille.controller.CharacterController;
import com.example.mybraille.controller.PatternController;
import com.example.mybraille.dot.Dot;
import com.example.mybraille.dot.DotController;
import com.example.mybraille.sentence.Sentences;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ReadActivity extends AppCompatActivity {

    private final ActivityManager activityManager = new ActivityManager(this);

    private SoundPool soundPool;
    HashMap<Integer, Integer> soundMap = new HashMap<>();

    private  RecyclerView recyclerView;

    private View readContainer;

    private  Vibrator vibrator;

    private final RecycleViewAdapter recycleViewAdapter = new RecycleViewAdapter();

    DotController dotController = new DotController(this);

    CharacterController characterController = new CharacterController();

    private final Dot topLeft =  new Dot(0);
    private final Dot topRight = new Dot(1);
    private final Dot centerLeft = new Dot(2);
    private final Dot centerRight = new Dot(3);
    private final Dot bottomLeft = new Dot(4);
    private final Dot bottomRight =  new Dot(5);

    List<Dot> dotList = new ArrayList<>();

    //position of center element in recycle view
    int pos = 3;

    //position of character in center
    int characterIndex = 2;

    //end screen data
    ImageView listenIcon;
    ImageView newSentenceIcon;

    TextView slideText;

    View endScreen;
    View bottomBar;
    View slider;

    public float initialTouchX;
    public float initialSliderX;

    private SpringAnimation springAnimation;
    private SpringForce spring = new SpringForce(0f);

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

//        getSupportActionBar().hide();
        setContentView(R.layout.activity_read);

        loadSoundPool();

        readContainer = findViewById(R.id.Container_Read);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        //set Activity
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.item2);
        activityManager.changeActivity(bottomNavigationView);


        //create recycler view
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL , false));
        recyclerView.setAdapter(recycleViewAdapter);
        recyclerView.setOnTouchListener((v, event) -> true);

        //attach snap helper
        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(recyclerView);

        //set up dots and containers
        View dotContainer = findViewById(R.id.dot_container);

        topLeft.setDot(findViewById(R.id.dot_top_left));
        topRight.setDot(findViewById(R.id.dot_top_right));
        centerLeft.setDot(findViewById(R.id.dot_center_left));
        centerRight.setDot(findViewById(R.id.dot_center_right));
        bottomLeft.setDot(findViewById(R.id.dot_bottom_left));
        bottomRight.setDot(findViewById(R.id.dot_bottom_right));


        Collections.addAll(dotList,
                topLeft,
                topRight,
                centerLeft,
                centerRight,
                bottomLeft,
                bottomRight
        );


        listenIcon = findViewById(R.id.listen_icon);
        newSentenceIcon = findViewById(R.id.new_icon);

        slideText = findViewById(R.id.slide_text);

        endScreen = findViewById(R.id.end_screen);
        bottomBar = findViewById(R.id.bottomBar);
        slider = findViewById(R.id.slider);

        listenIcon.setVisibility(View.INVISIBLE);
        newSentenceIcon.setVisibility(View.INVISIBLE);
        endScreen.setVisibility(View.INVISIBLE);
        bottomBar.setVisibility(View.INVISIBLE);
        slider.setVisibility(View.INVISIBLE);
        slideText.setVisibility(View.INVISIBLE);



        springAnimation = new SpringAnimation(slider, SpringAnimation.TRANSLATION_X , 0f);
        spring.setDampingRatio(0.25f);
        spring.setStiffness(250f);

        springAnimation.setSpring(spring);

        //sets first character on load
        setPattern(Sentences.getCharArray()[characterIndex]);
        recyclerView.smoothScrollToPosition(pos);

        readContainer.setOnTouchListener((v, e) -> {



            switch (e.getAction())
            {
                case MotionEvent.ACTION_DOWN:


                    return true;

                case MotionEvent.ACTION_MOVE:

                    float x = e.getRawX();
                    float y = e.getRawY();


                    dotController.checkDotTouch(dotList , x , y , vibrator);

                    return true;


                case MotionEvent.ACTION_UP:

                    showNextCharacter();

                    for(Dot dot : dotList)
                    {
                        dot.setTouched(false);
                    }


                    return true;



            }


            return true;
        });



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                float recyclerViewCenterX = (float) (recyclerView.getLeft() + recyclerView.getRight()) / 2;

                float heightBias = 20;




                for (int i = 0; i < recyclerView.getChildCount(); i++)
                {
                    View child = recyclerView.getChildAt(i);

                    float childCenterX = (float) (child.getLeft() + child.getRight()) / 2;
                    float distanceFromCenter = Math.abs(recyclerViewCenterX - childCenterX);

                    float verticalOffset =  heightBias - distanceFromCenter/heightBias;
                    float scale = 1.1f -  distanceFromCenter / recyclerView.getWidth();
                    float alpha = 1.0f -  distanceFromCenter / (recyclerView.getWidth() / 2.0f);

                    scale = Math.max(0.8f, scale);

                    alpha = Math.max(0.3f, Math.min(1.0f, alpha));

                    verticalOffset = Math.max(verticalOffset , -3.0f);

                    //modify scale
                    child.setScaleX(scale);
                    child.setScaleY(scale);

                    //modify vertical position
                    child.setTranslationY(-(verticalOffset * 6.0f));


//                    textView.setAlpha(alpha);
                    child.setAlpha(alpha);

                    if(distanceFromCenter < 20.0f)
                    {
                        child.setBackground(getDrawable(R.drawable.card_front));

                    }else{

                        child.setBackground(getDrawable(R.drawable.card_back));
                    }


                }
            }
        });

        setPattern(Sentences.getCharArray()[2]);







        slider.setOnTouchListener((v , e) ->
        {

            switch (e.getAction())
            {
                case MotionEvent.ACTION_DOWN:

                        initialTouchX = e.getRawX();
                        initialSliderX = v.getX();
                        break;

                case MotionEvent.ACTION_MOVE:



                    float currentTouchX = e.getRawX();
                    float deltaX = currentTouchX - initialTouchX;
                    float newX = initialSliderX + deltaX;

                    // Get the bounds of the parent view (the screen)
                    View parentView = (View) v.getParent();
                    int parentWidth = parentView.getWidth() - 20;
                    int sliderWidth = v.getWidth();

                    // Clamp the new X position within the parent bounds
                    newX = Math.max(20, newX); // Prevent moving beyond the left edge
                    newX = Math.min(parentWidth - sliderWidth, newX); // Prevent moving beyond the right edge

                    v.setX(newX);


                    int padding = 250;

                    float transparency;
                    if (newX <= padding) {
                        // Slider is at or near the left edge
                        transparency = Math.max(0.1f, newX / padding); // Reduce transparency towards 0.3
                    } else if (newX >= parentWidth - sliderWidth - padding) {
                        // Slider is at or near the right edge
                        transparency = Math.max(0.1f, (parentWidth - sliderWidth - newX) / padding); // Reduce transparency towards 0.3
                    } else {
                        // Slider is in the middle, full transparency
                        transparency = 1.0f;
                    }

                    slideText.setAlpha(transparency);

                    return true;

                case MotionEvent.ACTION_UP:

                    //distance from edge
                    float edgeThreshold = 50;

                    parentView = (View) v.getParent();
                    parentWidth = parentView.getWidth();
                    sliderWidth = v.getWidth();

                    if (v.getX() < edgeThreshold) {

                        playCurrentSentenceAudio(Sentences.randomSentenceIndex);

                    } else if (v.getX() > parentWidth - sliderWidth - edgeThreshold) {


                        //reload activity
                        Intent intent = new Intent(this , ReadActivity.class);
                        startActivity(intent);

                        overridePendingTransition(0, 0);

                    }
                    springAnimation.start();
                    slideText.setAlpha(1.0f);
                return true;
            }


            return true;
        });

    }



    public void showNextCharacter()
    {


        //reached end
        if(characterIndex <= Sentences.getCharArray().length - 2)
        {
            pos++;
            characterIndex++;

            System.out.println(pos + " " + characterIndex);

            recyclerView.smoothScrollToPosition(pos);
            //sets the braille pattern
            setPattern(Sentences.getCharArray()[characterIndex]);

            vibrator.vibrate(50);


        }else{

            endOfSentence();

        }


    }

    private void setPattern(char character)
    {

        int index = characterController.characterToIndex(character);
        PatternController.setPattern(index);

        dotController.setDots(PatternController.dotMatrix , dotList);

    }

    private void endOfSentence()
    {
        System.out.println("end of sentence");

        listenIcon.setVisibility(View.VISIBLE);
        newSentenceIcon.setVisibility(View.VISIBLE);
        endScreen.setVisibility(View.VISIBLE);
        bottomBar.setVisibility(View.VISIBLE);
        slider.setVisibility(View.VISIBLE);
        slideText.setVisibility(View.VISIBLE);

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



        //load all sentence audio
        for (int i = 1; i <= 37; i++ ) {

            int soundIndex = i - 1;
            String resName = "audio_" + i;
            int resId = getResources().getIdentifier(resName, "raw", getPackageName());

            if (resId != 0) {

                int soundId = soundPool.load(this , resId, 1);
                soundMap.put(soundIndex, soundId);
            }
        }





    }

    private void playCurrentSentenceAudio(int sentenceIndex)
    {
        int soundId = soundMap.get(sentenceIndex);
        if (soundId != 0) {
            soundPool.play(soundId, 1, 1, 0, 0, 1);
        }
    }
}