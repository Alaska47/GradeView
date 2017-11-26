package com.akotnana.fcpsstudentvue.utils;

import android.graphics.Color;

/**
 * Created by anees on 11/25/2017.
 */

public class ColorManager {
    public static int getColor(String grade) {
        if(grade.charAt(0) == 'A') {
            int baseColor = Color.GREEN;
            if(grade.length() == 1)
                return baseColor;
            if(grade.charAt(1) == '+') {
                baseColor = lighten(baseColor, 0.25);
            } else {
                baseColor = darken(baseColor, 0.25);
            }
            return baseColor;
        } else if(grade.charAt(0) == 'B') {
            int baseColor = darken(Color.parseColor("#ffa500"), 0.25);
            if(grade.length() == 1)
                return baseColor;
            if(grade.charAt(1) == '+') {
                baseColor = lighten(baseColor, 0.25);
            } else {
                baseColor = darken(baseColor, 0.25);
            }
            return baseColor;
        } else if(grade.charAt(0) == 'C') {
            int baseColor = darken(Color.YELLOW, 0.25);
            if(grade.length() == 1)
                return baseColor;
            if(grade.charAt(1) == '+') {
                baseColor = lighten(baseColor, 0.25);
            } else {
                baseColor = darken(baseColor, 0.25);
            }
            return baseColor;
        } else if(grade.charAt(0) == 'D') {
            int baseColor = Color.RED;
            if(grade.length() == 1)
                return baseColor;
            if(grade.charAt(1) == '+') {
                baseColor = lighten(baseColor, 0.25);
            } else {
                baseColor = darken(baseColor, 0.25);
            }
            return baseColor;
        } else if(grade.charAt(0) == 'F') {
            return darken(Color.RED, 0.25);
        } else {
            return Color.BLACK;
        }
    }

    public static int lighten(int color, double fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = lightenColor(red, fraction);
        green = lightenColor(green, fraction);
        blue = lightenColor(blue, fraction);
        int alpha = Color.alpha(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static int darken(int color, double fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = darkenColor(red, fraction);
        green = darkenColor(green, fraction);
        blue = darkenColor(blue, fraction);
        int alpha = Color.alpha(color);

        return Color.argb(alpha, red, green, blue);
    }

    private static int darkenColor(int color, double fraction) {
        return (int)Math.max(color - (color * fraction), 0);
    }

    private static int lightenColor(int color, double fraction) {
        return (int) Math.min(color + (color * fraction), 255);
    }
}
