package com.devsoft.as;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView scoreView,tapView;
    private LinearLayout footer;
    private ImageView rocketView,blueView,blueView1,redView,missileView,pauseBtn;

    MediaPlayer ring1;
    MediaPlayer ring;

    private int rocketY;
    private int blueX;
    private int blueY;
    private int blue1X;
    private int blue1Y;
    private int redX;
    private int redY;
    private int missileX;
    private int missileY;
    private int score=0;
    private int blueSpeed;
    private int blue1Speed;
    private int redSpeed;
    private int missileSpeed;
    private int rocketSpeed;

    private static String SCORE = "Score : ";

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    private boolean action_flag = false;
    private boolean start_flag = false;
    private boolean back_flag = false;
    private boolean over_flag = false;

    private int frameHeight;
    private int rocketHeight;
    private int rocketWidth;
    private int screenHeight;
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        background();
        ring1= MediaPlayer.create(MainActivity.this,R.raw.over);
        ring= MediaPlayer.create(MainActivity.this,R.raw.pop);

        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        scoreView = (TextView) findViewById(R.id.score);
        tapView = (TextView) findViewById(R.id.tap_view);
        rocketView = (ImageView) findViewById(R.id.rocket);
        blueView = (ImageView) findViewById(R.id.blue);
        blueView1 = (ImageView) findViewById(R.id.blue1);
        redView = (ImageView) findViewById(R.id.red);
        missileView = (ImageView) findViewById(R.id.missile);
        pauseBtn = (ImageView) findViewById(R.id.pause);
        footer = (LinearLayout) findViewById(R.id.footer);

        blueView.setX(-1500);
        blueView.setY(-1500);
        blueView1.setX(-1500);
        blueView1.setY(-1500);
        redView.setX(-1500);
        redView.setY(-1500);
        missileView.setX(-1500);
        missileView.setY(-1500);

        blueSpeed = (int) screenWidth/85 - 5;
        blue1Speed = (int) screenWidth/80 - 5;
        redSpeed = (int) screenWidth/55 - 5;
        missileSpeed = (int) screenWidth/66 - 5;
        rocketSpeed = (int) screenHeight/48 - 2;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(start_flag){
                            changePos();
                        }
                    }
                });
            }
        }, 0 , 20);
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_flag=false;
                pauseBtn.setVisibility(View.INVISIBLE);
                tapView.setVisibility(View.VISIBLE);
                footer.setVisibility(View.VISIBLE);
                pauseBtn.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
            }
        });
    }

    public void changePos(){

        hitCheck();

        blueX -= blueSpeed;
        if(blueX + blueView.getWidth() < 0){
            blueX = screenWidth + 50;
            blueY = (int) Math.floor(Math.random() * (frameHeight - blueView.getHeight()));
        }
        blueView.setX(blueX);
        blueView.setY(blueY);

        blue1X -= blue1Speed;
        if(blue1X + blueView1.getWidth() < 0){
            blue1X = screenWidth + 20;
            blue1Y = (int) Math.floor(Math.random() * (frameHeight - blueView1.getHeight()));
            if(blueY<screenHeight/2 && blue1Y<frameHeight/2){
                blue1Y += frameHeight/4;
            }
            else if(blueY>frameHeight/2 && blue1Y>frameHeight/2){
                blue1Y -= frameHeight/4;
            }
        }
        blueView1.setX(blue1X);
        blueView1.setY(blue1Y);

        missileX -= missileSpeed;
        if(missileX + missileView.getWidth() < 0){
            missileX = screenWidth + 10;
            missileY = (int) Math.floor(Math.random() * (frameHeight - missileView.getHeight()));
        }
        missileView.setX(missileX);
        missileView.setY(missileY);

        redX -= redSpeed;
        if(redX + redView.getWidth() < 0){
            redX = screenWidth + 5000;
            redY = (int) Math.floor(Math.random() * (frameHeight - redView.getHeight()));
        }
        redView.setX(redX);
        redView.setY(redY);

        if(action_flag){
            rocketY -= rocketSpeed;
        }
        else {
            rocketY += rocketSpeed;
        }
        if(rocketY < 0){
            rocketY=0;
        }
        else if(rocketY > frameHeight - rocketHeight){
            rocketY=frameHeight-rocketHeight;
        }
        rocketView.setY(rocketY);
    }

    public void speedUpdate(){
        if(score!=0 && score%100==0){
            redSpeed+=1;
            blueSpeed+=1;
            blue1Speed+=1;
            missileSpeed+=1;
            if(score%300==0){
                missileSpeed+=1;
            }
            if(score%200==0){
                rocketSpeed-=1;
            }
        }
    }

    public void hitCheck(){

        int blueCenterX = blueX + blueView.getWidth()/2;
        int blueCenterY = blueY + blueView.getHeight()/2;

        int blue1CenterX = blue1X + blueView1.getWidth()/2;
        int blue1CenterY = blue1Y + blueView1.getHeight()/2;

        int redCenterX = redX + redView.getWidth()/2;
        int redCenterY = redY + redView.getHeight()/2;

        int missileCenterX = missileX + missileView.getWidth()/2;
        int missileCenterY = missileY + missileView.getHeight()/2;

        if(blueCenterX >=0 && blueCenterX <= rocketWidth && blueCenterY >= rocketY && blueCenterY <= rocketY+rocketHeight){
            vibrate(50);
            ring.start();
            blueX = -100;
            score += 10;
            speedUpdate();
            scoreView.setText(SCORE+score);
        }

        if(blue1CenterX >=0 && blue1CenterX <= rocketWidth && blue1CenterY >= rocketY && blue1CenterY <= rocketY+rocketHeight){
            vibrate(50);
            ring.start();
            blue1X = -100;
            score += 10;
            speedUpdate();
            scoreView.setText(SCORE+score);
        }

        if(redCenterX >=0 && redCenterX <= rocketWidth && redCenterY >= rocketY && redCenterY <= rocketY+rocketHeight){
            vibrate(50);
            ring.start();
            redX = -100;
            score+=30;
            speedUpdate();
            scoreView.setText(SCORE+score);
        }

        if(missileCenterX >=0 && missileCenterX <= rocketWidth && missileCenterY >= rocketY && missileCenterY <= rocketY+rocketHeight){
            timer.cancel();
            timer = null;
            over_flag = true;
            vibrate(1000);
            ring1.start();
            pauseBtn.setVisibility(View.INVISIBLE);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences storage = getSharedPreferences("data",MODE_PRIVATE);
                    SharedPreferences.Editor editor = storage.edit();
                    editor.putInt("Score",score);
                    editor.putBoolean("Visited",true).commit();
                    Intent intent = new Intent(MainActivity.this,over.class);
                    startActivity(intent);
                    finish();
                }
            },2200);
        }
    }

    public boolean onTouchEvent(MotionEvent me){

        if(!start_flag){
            start_flag = true;
            pauseBtn.setVisibility(View.VISIBLE);
            pauseBtn.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();
            rocketY = (int) rocketView.getY();
            rocketHeight = rocketView.getHeight();
            rocketWidth = rocketView.getWidth();
            back_flag = false;
            tapView.setVisibility(View.GONE);
            footer.setVisibility(View.GONE);
        }else{
            if(me.getAction() == MotionEvent.ACTION_DOWN){
                action_flag = true;
            }else if(me.getAction() == MotionEvent.ACTION_UP){
                action_flag = false;
            }
        }
        return true;
    }

    void background(){
        final ImageView backgroundOne = (ImageView) findViewById(R.id.background_one);
        final ImageView backgroundTwo = (ImageView) findViewById(R.id.background_two);

        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(10000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress;
                backgroundOne.setTranslationX(-translationX);
                backgroundTwo.setTranslationX(-translationX + width);
            }
        });
        animator.start();
    }
    public void vibrate(int TIME){

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(TIME, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(TIME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(over_flag){
            start_flag=false;
        }
        else{
            start_flag=false;
            tapView.setVisibility(View.VISIBLE);
            footer.setVisibility(View.VISIBLE);
            pauseBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if(!back_flag){
            onPause();
            back_flag=true;
        }
        else{
            Intent intent = new Intent(MainActivity.this,Home.class);
            startActivity(intent);
            finish();
        }
    }
}
