package com.example.myflappy;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.myflappy.GameView.telaRatioX;
import static com.example.myflappy.GameView.telaRatioY;

public class Bullet {

    int x, y, width, height;
    Bitmap bullet;

    Bullet(Resources res){
        bullet = BitmapFactory.decodeResource(res, R.drawable.bullet);

        width = bullet.getWidth();
        height = bullet.getHeight();

        width /= 4;
        height /= 4;

        width = (int) (width * telaRatioX);
        height = (int) (height * telaRatioY);

        bullet = Bitmap.createScaledBitmap(bullet, width, height, false);
    }

    Rect getCollissionShape(){
        return  new Rect(x, y, x + width, y + height);
    }
}
