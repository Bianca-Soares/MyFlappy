package com.example.myflappy;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.example.myflappy.GameView.telaRatioX;
import static com.example.myflappy.GameView.telaRatioY;

public class Bullet {

    int x, y;
    Bitmap bullet;

    Bullet(Resources res){
        bullet = BitmapFactory.decodeResource(res, R.drawable.bullet);

        int width = bullet.getWidth();
        int height = bullet.getHeight();

        width /= 4;
        height /= 4;

        width = (int) (width * telaRatioX);
        height = (int) (height * telaRatioY);

        bullet = Bitmap.createScaledBitmap(bullet, width, height, false);
    }
}
