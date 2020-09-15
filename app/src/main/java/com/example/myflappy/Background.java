package com.example.myflappy;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {
    int x = 0 , y = 0;
    Bitmap background;

    Background(int xTela, int yTela, Resources res){
        background = BitmapFactory.decodeResource(res, R.drawable.background);
        background = Bitmap.createScaledBitmap(background, xTela, yTela, false);

    }
}
