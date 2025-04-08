package com.example.mybraille.dot;

import android.view.View;

public class Dot {




    private View dot;



    private boolean isActive = false;


    private boolean touched = false;



    private int coordinate;

    public Dot(View dot)
    {
        this.dot = dot;
    }

    public Dot(int coordinate)
    {
        this.coordinate = coordinate;
    }


    public View getDot() {
        return dot;
    }

    public void setDot(View dot) {
        this.dot = dot;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }


    public int getCoordinate() {
        return coordinate;
    }
}
