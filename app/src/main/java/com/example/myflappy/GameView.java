package com.example.myflappy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable{

    private Thread thread;
    private boolean jogando, gameOver = false;
    private int telaX, telaY, score = 0;
    public static float telaRatioX, telaRatioY;
    private Paint paint;
    private Bird[] birds;
    private SharedPreferences prefs;
    private Random random;
    private SoundPool soundPool;
    private List<Bullet> bullets;
    private int sound;
    private Flight flight;
    private GameActivity activity;
    private Background background1, background2;

    public GameView(GameActivity activity, int telaX, int telaY ) {
        super(activity);

        this.activity = activity;

        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

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
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        birds = new Bird[4];

        for(int i = 0; i < 4; i++){
            Bird bird = new Bird(getResources());
            birds[i] = bird;
        }

        random = new Random();
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
        background1.x -= 8 * telaRatioX;
        background2.x -= 8 * telaRatioX;
        //verificar o lado direito
        if(background1.x + background1.background.getWidth() < 0){
            background1.x = telaX;
        }

        if(background2.x + background2.background.getWidth() < 0){
            background2.x = telaX;
        }

        if(flight.isGoingUp)
            flight.y -= 40 * telaRatioY;
        else
            flight.y += 15 * telaRatioY;

        if(flight.y < 0)
            flight.y = 0;

        if(flight.y >= telaY - flight.height)
            flight.y = telaY - flight.height;

        List<Bullet> trash = new ArrayList<>();

        for(Bullet bullet : bullets) {
            if (bullet.x > telaX) ;
            trash.add(bullet);

            bullet.x += 50 * telaRatioX;

            for(Bird bird : birds){

                if(Rect.intersects(bird.getCollissionShape(),
                        bullet.getCollissionShape())){
                    score++;
                    bird.x = -500;
                    bullet.x = telaX + 500;
                    bird.wasShot = true;

                }

            }

        }


        for(Bird bird : birds) {
            bird.x -= bird.speed;

            if (bird.x + bird.width < 0) {

                if(!bird.wasShot){
                    gameOver = true;
                    return;
                }

                int bound = (int) (15 * telaRatioX);
                bird.speed = random.nextInt(bound);

                if (bird.speed < 5 * telaRatioX)
                    bird.speed = (int) (5 * telaRatioX);

                bird.x = telaX;
                bird.y = random.nextInt(telaY - bird.height);

                bird.wasShot = false;
            }

            if(Rect.intersects(bird.getCollissionShape(), flight.getCollissionShape())){

                gameOver = true;
                return;
            }
        }
    }

    private void draw(){
        if(getHolder().getSurface().isValid()){

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            for (Bird bird : birds)
                canvas.drawBitmap(bird.getBird(), bird.x, bird.y, paint);

            canvas.drawText(score + "", telaX / 2f, 164, paint);

            if(gameOver) {
                jogando = false;
                canvas.drawBitmap(flight.getDead(), flight.x, flight.y, paint);
                saveIfHighScore();
                waitBeforeExiting();
                getHolder().unlockCanvasAndPost(canvas);
                return;
            }

            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);

            for(Bullet bullet : bullets)
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void waitBeforeExiting() {

        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void saveIfHighScore() {

        if (prefs.getInt("highscore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
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

        switch ((event.getAction()& MotionEvent.ACTION_MASK)){
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
