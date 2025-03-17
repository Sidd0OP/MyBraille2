package com.example.mybraille.dot;

import android.view.View;

public class Dot {




    private View dot;



    private boolean touched = false;

    public Dot(View dot)
    {
        this.dot = dot;
    }

    public Dot()
    {

    }


    public View getDot() {
        return dot;
    }

    public void setDot(View dot) {
        this.dot = dot;
    }


    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }
}
