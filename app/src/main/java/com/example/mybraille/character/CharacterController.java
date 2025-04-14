package com.example.mybraille.character;

import android.media.MediaPlayer;
import android.widget.TextView;

import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import com.example.mybraille.R;

public class CharacterController {

    //goes from 0 to 25 for ascii character A - Z , then goes to 35 for numbers 0 - 9
    private static int currentCharacterIndex = 0;
    private SpringAnimation springAnimation;
    private SpringForce spring = new SpringForce(0f);


    public void clockCharacterIndex(int swipeDirection, TextView characterDisplay)
    {

        springAnimation = new SpringAnimation(characterDisplay, SpringAnimation.TRANSLATION_X , 0f);
        spring.setDampingRatio(0.25f);
        spring.setStiffness(250f);

        springAnimation.setSpring(spring);
        //left swipe , get previous character
        if(swipeDirection == -1)
        {

            if(currentCharacterIndex > 0)
            {
                currentCharacterIndex--;

            }else{
                currentCharacterIndex = 35;
            }

            characterDisplay.setTranslationX(-60f);

        }else{

            if(currentCharacterIndex < 35)
            {
                currentCharacterIndex++;
            }else{
                currentCharacterIndex = 0;
            }

            characterDisplay.setTranslationX(60f);
        }


        characterDisplay.setText(indexToCharacter(currentCharacterIndex));
        PatternController.setPattern(currentCharacterIndex);

        springAnimation.start();

    }

    public String indexToCharacter(int index)
    {
        if (index >= 0 && index <= 25) {

            return String.valueOf((char) ('A' + index));

        } else if (index >= 26 && index <= 35) {

            return String.valueOf((char) ('0' + (index - 26)));
        } else {
            return "";
        }
    }

    public int getCurrentCharacterIndex() {
        return currentCharacterIndex;
    }
}
