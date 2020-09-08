package com.example.myflappy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements Runnable{

    private Thread thread;
    private boolean jogando;
    private int telaX, telaY;
    public static float telaRatioX, telaRatioY;
    private Paint paint;
    private List<Bullet> bullets;
    private Flight flight;
    private Background background1, background2;

    public GameView(Context context, int telaX, int telaY ) {
        super(context);

        this.telaX = telaX;
        this.telaY = telaY;
        telaRatioX = 1920f / telaX;
        telaRatioY = 1080f / telaY;

        background1 = new Background(telaX, telaY, getResources());
        background2 = new Background(telaX, telaY, getResources());

        flight = new Flight(this, telaY, getResources());

        bullets = new ArrayList<>();

        background2.x = telaX;

        paint = new Paint();
    }

    @Override
    public void run() {
        while(jogando){
            update();
            draw();
            sleep();
        }
    }

    private void update(){

        //o fundo moverá 10 pixels no eixo x para esquerda
        background1.x -= 10 * telaRatioX;
        background2.x -= 10 * telaRatioX;
        //verificar o lado direito
        if(background1.x + background1.background.getWidth() < 0){
            background1.x = telaX;
        }

        if(background2.x + background2.background.getWidth() < 0){
            background2.x = telaX;
        }

        if(flight.isGoingUp)
            flight.y -= 30 * telaRatioY;
        else
            flight.y += 30 * telaRatioY;

        if(flight.y < 0)
            flight.y = 0;

        if(flight.y >= telaY - flight.height)
            flight.y = telaY - flight.height;

        List<Bullet> trash = new ArrayList<>();

        for(Bullet bullet : bullets){
            if(bullet.x > telaX);
                trash.add(bullet);

            bullet.x += 50 * telaRatioX;
        }

        for(Bullet bullet : trash)
            bullets.remove(bullet);


    }

    private void draw(){
        if(getHolder().getSurface().isValid()){

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);

            for(Bullet bullet : bullets){
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint );

            }
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep(){
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void resume(){
        jogando = true;
        thread = new Thread(this);
        thread.start();
    }
    public void pause(){
        try {
            jogando = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        switch ((event.getAction())){
            case MotionEvent.ACTION_DOWN:
                if(event.getX() < telaX / 2){
                    flight.isGoingUp = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                flight.isGoingUp = false;
                if(event.getX() > telaX / 2)
                    flight.toShoot++;
                break;
        }
        return true;
    }

    public void newBullet() {

        Bullet bullet = new Bullet(getResources());
        bullet.x = flight.x + flight.width;
        bullet.y = flight.y + (flight.height / 2);
        bullets.add(bullet);


    }
}
