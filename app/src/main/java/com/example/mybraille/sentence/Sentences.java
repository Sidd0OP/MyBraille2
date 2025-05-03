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
            "  Test  ",
            "  There is nobody here  ",
            "  Paris is the capital of France  ",
            "  Cows give us milk  ",
            "  The tailor made a new dress for the princess  ",
            "  His success made his parents happy  ",
            "  You should help yourself  ",
            "  Whose umbrella is this  ",
            "  Peter is fatter than Maurice  ",
            "  Alan is an honest boy  "
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
