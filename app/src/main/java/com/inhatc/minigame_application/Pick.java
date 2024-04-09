package com.inhatc.minigame_application;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class Pick {

    public int pick(Bitmap bitmap) {

        int targetColor = Color.rgb(51, 153, 153);
        int score;

        ColorAndCoordinate closestColorAndCoordinate = findClosestColorAndCoordinate(bitmap, targetColor);

        Log.d("Pick", "Closest Color to Target: " + closestColorAndCoordinate.color);
        Log.d("Pick", "Coordinate: (" + closestColorAndCoordinate.x + ", " + closestColorAndCoordinate.y + ")");
        score = 10000 - closestColorAndCoordinate.x - closestColorAndCoordinate.y;
        Log.d("Pick", "점수: " + score);

        return score;
    }

    public static ColorAndCoordinate findClosestColorAndCoordinate(Bitmap bitmap, int targetColor) {
        ColorAndCoordinate closestColorAndCoordinate = null;
        double minDistance = Double.MAX_VALUE;

        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                int pixel = bitmap.getPixel(x, y);
                int color = pixel;

                double distance = calculateDistance(color, targetColor);

                if (distance < minDistance) {
                    minDistance = distance;
                    closestColorAndCoordinate = new ColorAndCoordinate(color, x, y);
                }
            }
        }
        return closestColorAndCoordinate;
    }

    //거리 계산 메소드
    public static double calculateDistance(int c1, int c2) {
        int redDiff = Color.red(c1) - Color.red(c2);
        int greenDiff = Color.green(c1) - Color.green(c2);
        int blueDiff = Color.blue(c1) - Color.blue(c2);

        return Math.sqrt(redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
    }

    static class ColorAndCoordinate {
        int color,x,y;

        public ColorAndCoordinate(int color, int x, int y) {
            this.color = color;
            this.x = x;
            this.y = y;
        }
    }
}
