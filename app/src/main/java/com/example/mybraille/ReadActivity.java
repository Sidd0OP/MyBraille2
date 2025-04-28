package com.example.mybraille;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybraille.activityManager.ActivityManager;
import com.example.mybraille.card.RecycleViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ReadActivity extends AppCompatActivity {

    ActivityManager activityManager = new ActivityManager(this);

    RecycleViewAdapter recycleViewAdapter = new RecycleViewAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_read);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.item2);

        activityManager.changeActivity(bottomNavigationView);


        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        RecyclerView recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL , false));
        recyclerView.setAdapter(recycleViewAdapter);
        linearSnapHelper.attachToRecyclerView(recyclerView);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int recyclerViewCenterX = (recyclerView.getLeft() + recyclerView.getRight()) / 2;

                int heightBias = 20;

                System.out.println(recyclerViewCenterX);

                for (int i = 0; i < recyclerView.getChildCount(); i++)
                {
                    View child = recyclerView.getChildAt(i);
                    TextView textView = child.findViewById(R.id.character);



                    int childCenterX = (child.getLeft() + child.getRight()) / 2;
                    int distanceFromCenter = Math.abs(recyclerViewCenterX - childCenterX);

                    float verticalOffset = (float) heightBias - distanceFromCenter/heightBias;
                    float scale = 1.1f - (float) distanceFromCenter / recyclerView.getWidth();
                    float alpha = 1.0f - (float) distanceFromCenter / (recyclerView.getWidth() / 3.0f);

                    scale = Math.max(0.85f, scale);

                    verticalOffset = Math.max(verticalOffset , -3.0f);

                    //modify scale
                    child.setScaleX(scale);
                    child.setScaleY(scale);

                    //modify vertial position
                    child.setTranslationY(-(verticalOffset * 6.0f));

                    alpha = Math.max(0.1f, Math.min(1.0f, alpha));
                    textView.setAlpha(alpha);

                    if(distanceFromCenter < 10.0f)
                    {
                        child.setBackground(getDrawable(R.drawable.character_container_style));

                    }else{

                        child.setBackground(getDrawable(R.drawable.dot_container_style));
                    }


                }
            }
        });

    }
}