package com.example.mybraille.sentence;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Sentences {

    public static int randomSentenceIndex = 0;

    public static int currentCharIndex = 0;


    private static Random random = new Random();


    public static int chooseRandomSentence()
    {
        randomSentenceIndex = random.nextInt(SENTENCES.size());

        return randomSentenceIndex;
    }

    public static final List<String> SENTENCES = Arrays.asList(

            //easy
            "  there is nobody here  ",
            "  paris is the capital of france  ",
            "  cows give us milk  ",
            "  the tailor made a new dress for the princess  ",
            "  his success made his parents happy  ",
            "  you should help yourself  ",
            "  whose umbrella is this  ",
            "  peter is fatter than maurice  ",
            "  alan is an honest boy  " ,

            //long
            "  the sun dipped below the horizon painting the sky in fiery hues  " ,
            "  she whispered a secret to the wind hoping it would carry her words far away  ",
            "  the cat curled up in a patch of sunlight purring softly  ",
            "  he opened the letter slowly his hands trembling with anticipation  ",
            "  raindrops tapped against the window each one a tiny reminder of the storm outside  ",
            "  the old clock ticked steadily marking each second with a quiet certainty  ",
            "  she found comfort in the quiet where her thoughts could roam freely  ",
            "  the forest felt alive each leaf rustling like a whispered promise  ",
            "  he gazed at the stars letting his mind drift into the infinite  ",


            //words
            "  time  ",
            "  person  ",
            "  thing  ",
            "  man  ",
            "  world  ",
            "  hand  ",
            "  part  ",
            "  child  ",
            "  woman  ",
            "  place  ",
            "  work  ",
            "  week  ",
            "  case  ",
            "  point  ",
            "  government  ",
            "  company  ",
            "  number  ",
            "  group  ",
            "  problem  ",
            "  fact  "


    );


    public static char[] getCharArray()
    {
        return SENTENCES.get(randomSentenceIndex).toCharArray();
    }

    public static char[] chooseRandomSentenceArray()
    {
        return SENTENCES.get(chooseRandomSentence()).toCharArray();
    }


}
