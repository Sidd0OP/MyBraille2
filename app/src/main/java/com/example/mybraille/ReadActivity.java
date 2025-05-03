package com.example.mybraille;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.List;

public class ReadActivity extends AppCompatActivity {

    private final ActivityManager activityManager = new ActivityManager(this);

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_read);

        //set Activity
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.item2);
        activityManager.changeActivity(bottomNavigationView);


        //create recycler view
        RecyclerView recyclerView = findViewById(R.id.recycleView);
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








        //-------------------------------------------------------------------------------
        dotContainer.setOnClickListener(v -> {

            pos++;
            characterIndex++;

            //reached end
            if(characterIndex == Sentences.getCharArray().length - 2)
            {
                endOfSentence();
            }

            recyclerView.smoothScrollToPosition(pos);
            //sets the braille pattern
            setPattern(Sentences.getCharArray()[characterIndex]);


        });

//        setPattern(Sentences.getCharArray()[2]);
//
//        System.out.println(Sentences.getCharArray()[characterIndex]);
        recyclerView.smoothScrollToPosition(pos);

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

                    if(distanceFromCenter < 10.0f)
                    {
                        child.setBackground(getDrawable(R.drawable.card_front));

                    }else{

                        child.setBackground(getDrawable(R.drawable.card_back));
                    }


                }
            }
        });

        setPattern(Sentences.getCharArray()[2]);

    }




    private void setPattern(char character)
    {

        int index = characterController.characterToIndex(character);
        PatternController.setPattern(index);

        dotController.setDots(PatternController.dotMatrix , dotList);

    }

    private void endOfSentence()
    {

    }
}