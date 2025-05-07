package com.example.mybraille.dot;



import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;


import androidx.core.content.ContextCompat;


import com.example.mybraille.R;

import java.util.List;

public class DotController {

    private final Context context;

    public DotController(Context context)
    {
        this.context = context;
    }

    public void setDots(int[][] dotMatrix , List<Dot> dotList)
    {
        if(dotMatrix[0][0] == 1)
        {
            changeState(dotList.get(0) , true , R.drawable.dot_style_touch);


        }else{

            changeState(dotList.get(0) , false , R.drawable.dot_style_no_touch);
        }



        if(dotMatrix[0][1] == 1)
        {
            changeState(dotList.get(1) , true , R.drawable.dot_style_touch);

        }else{

            changeState(dotList.get(1) , false , R.drawable.dot_style_no_touch);
        }



        if(dotMatrix[1][0] == 1)
        {
            changeState(dotList.get(2) , true , R.drawable.dot_style_touch);


        }else{

            changeState(dotList.get(2) , false , R.drawable.dot_style_no_touch);
        }


        if(dotMatrix[1][1] == 1)
        {
            changeState(dotList.get(3) , true , R.drawable.dot_style_touch);

        }else{

            changeState(dotList.get(3) , false , R.drawable.dot_style_no_touch);
        }



        if(dotMatrix[2][0] == 1)
        {
            changeState(dotList.get(4) , true , R.drawable.dot_style_touch);

        }else{

            changeState(dotList.get(4) , false , R.drawable.dot_style_no_touch);
        }



        if(dotMatrix[2][1] == 1)
        {
            changeState(dotList.get(5) , true , R.drawable.dot_style_touch);

        }else{

            changeState(dotList.get(5) , false , R.drawable.dot_style_no_touch);
        }
    }


    private void changeState(Dot dot , boolean state , int drawableId)
    {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        dot.getDot().setBackground(drawable);
        dot.setActive(state);

    }



    public void checkDotTouch(List<Dot> dotList , float x , float y , Vibrator vibrator)
    {

        long[] timings = {0,50,3,50};
//        long[] timings = {0,50,2,20};


        for(Dot dot : dotList)
        {
            if (isTouchInsideView(x, y, dot.getDot())) {

                if(!dot.isTouched())
                {

                    dot.setTouched(true);

                    if(dot.isActive())
                    {
                        vibrator.vibrate(timings , -1);

                    }else{

                        vibrator.vibrate(1);

                    }


                }


            }else{

                if(dot.isTouched())
                {
                    dot.setTouched(false);
                }
            }
        }
    }


    private boolean isTouchInsideView(float x, float y, View view)
    {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        Rect rect = new Rect(location[0], location[1], location[0] + view.getWidth(), location[1] + view.getHeight());
        return rect.contains((int) x, (int) y);
    }


}
