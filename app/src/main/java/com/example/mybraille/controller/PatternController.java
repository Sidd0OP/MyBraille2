package com.example.mybraille.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PatternController {

    public static int[][] dotMatrix = new int[3][2];


    /*
    Patter should be of six characters which should be o and 1

     0 1 2 3 4 5

    "1 0 0 0 0 0" => A
    "1 0 1 0 0 0" => B
    "1 1 0 0 0 0" => c

     */

    private static final List<List<Boolean>> patternList = new ArrayList<>
            (Arrays.asList(

                    //Character from A to Z

                    Arrays.asList(true, false, false, false, false, false),
                    Arrays.asList(true, false, true, false, false, false),
                    Arrays.asList(true, true, false, false, false, false),
                    Arrays.asList(true, true, true, false, false, false),
                    Arrays.asList(true, false, true, true, false, false),
                    Arrays.asList(true, true, false, true, false, false),
                    Arrays.asList(true, true, true, true, false, false),
                    Arrays.asList(true, false, true, true, true, false),
                    Arrays.asList(true, true, false, true, true, false),
                    Arrays.asList(true, true, true, false, true, false),
                    Arrays.asList(true, false, true, true, false, true),
                    Arrays.asList(true, true, false, true, true, true),
                    Arrays.asList(true, true, true, false, true, true),
                    Arrays.asList(true, false, true, true, true, true),
                    Arrays.asList(true, true, false, true, false, true),
                    Arrays.asList(true, true, true, true, false, true),
                    Arrays.asList(true, false, true, true, true, false),
                    Arrays.asList(true, true, false, true, true, false),
                    Arrays.asList(true, true, true, false, true, false),
                    Arrays.asList(true, false, true, true, false, true),
                    Arrays.asList(true, true, false, true, true, true),
                    Arrays.asList(true, true, true, false, true, true),
                    Arrays.asList(true, false, true, true, true, true),
                    Arrays.asList(true, true, false, true, false, true),
                    Arrays.asList(true, true, true, true, false, true),
                    Arrays.asList(true, false, true, true, true, false),

                    //Number from 0 to 9
                    Arrays.asList(true, false, false, false, false, false),
                    Arrays.asList(true, false, true, false, false, false),
                    Arrays.asList(true, true, false, false, false, false),
                    Arrays.asList(true, true, true, false, false, false),
                    Arrays.asList(true, false, true, true, false, false),
                    Arrays.asList(true, true, false, true, false, false),
                    Arrays.asList(true, true, true, true, false, false),
                    Arrays.asList(true, false, true, true, true, false),
                    Arrays.asList(true, true, false, true, true, false),
                    Arrays.asList(true, true, true, false, true, false)

            ));


    public static void setPattern(int characterIndex)
    {
        //for space
        if(characterIndex == -1)
        {

            dotMatrix[0][0] = 0;
            dotMatrix[0][1] = 0;
            dotMatrix[1][0] = 0;
            dotMatrix[1][1] = 0;
            dotMatrix[2][0] = 0;
            dotMatrix[2][1] = 0;

            return;
        }


        boolean topLeft = patternList.get(characterIndex).get(0);
        boolean topRight = patternList.get(characterIndex).get(1);
        boolean centerLeft = patternList.get(characterIndex).get(2);
        boolean centerRight = patternList.get(characterIndex).get(3);
        boolean bottomLeft = patternList.get(characterIndex).get(4);
        boolean bottomRight = patternList.get(characterIndex).get(5);

        dotMatrix[0][0] = topLeft ? 1 : 0;
        dotMatrix[0][1] = topRight ? 1 : 0;
        dotMatrix[1][0] = centerLeft ? 1 : 0;
        dotMatrix[1][1] = centerRight ? 1 : 0;
        dotMatrix[2][0] = bottomLeft ? 1 : 0;
        dotMatrix[2][1] = bottomRight ? 1 : 0;
    }




}
