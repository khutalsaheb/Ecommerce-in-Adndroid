package com.sudnya.ecomm.Helper;

import android.graphics.Color;

import java.util.Random;

public class GenerateRandom_Color {

    //this method will generate random colors
    public static int generateRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}