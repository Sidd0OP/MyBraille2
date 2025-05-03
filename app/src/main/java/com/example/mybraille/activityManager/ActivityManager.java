package com.example.mybraille.activityManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.mybraille.MainActivity;
import com.example.mybraille.R;
import com.example.mybraille.ReadActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityManager {

    private Context context;

    public ActivityManager(Context context)
    {
        this.context = context;
    }

    public void changeActivity(BottomNavigationView bottomNavigationView)
    {
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.item1) {

                    Intent intent = new Intent(context , MainActivity.class);
                    context.startActivity(intent);

                    if (context instanceof Activity) {
                        ((Activity) context).overridePendingTransition(0, 0);
                    }

                    return true;

                }

                if (itemId == R.id.item2) {

                    Intent intent = new Intent(context , ReadActivity.class);
                    context.startActivity(intent);

                    if (context instanceof Activity) {
                        ((Activity) context).overridePendingTransition(0, 0);
                    }

                    return true;

                }

                if (itemId == R.id.item3) {

                    return true;

                }


                return false;
            }


        });
    }
}
