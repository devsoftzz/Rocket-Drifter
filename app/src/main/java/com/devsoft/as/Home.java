package com.devsoft.as;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Home extends AppCompatActivity {

    ImageView mLoading;
    Button mPlay;

    LinearLayout layout;
    MediaPlayer launch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        background();
        launch= MediaPlayer.create(Home.this,R.raw.launch);

        mLoading = findViewById(R.id.loading);
        mPlay = findViewById(R.id.ply_btn);
        mLoading.setEnabled(false);
        layout = findViewById(R.id.layout);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.myanimation);
        layout.setAnimation(animation);

        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch.start();
                mPlay.setEnabled(false);
                mLoading.setX(-350);
                mLoading.setVisibility(View.VISIBLE);
                Animation boom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.position);
                mLoading.setAnimation(boom);
                final Handler start = new Handler();
                start.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences storage = getSharedPreferences("data",MODE_PRIVATE);
                        if(storage.getBoolean("Visited",false)){
                            Intent intent = new Intent(Home.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(Home.this,instruction.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                },1500);
            }
        });
    }
    void background(){
        ValueAnimator animator3 = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator3.setRepeatCount(ValueAnimator.INFINITE);
        animator3.setInterpolator(new LinearInterpolator());
        animator3.setDuration(10000L);
        animator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ImageView back2 = (ImageView) findViewById(R.id.home);
                ImageView back1 = (ImageView) findViewById(R.id.home1);
                float progress2 = (float) animation.getAnimatedValue();
                float width2 = (float) back2.getWidth();
                float translationX2 = width2 * progress2;
                back2.setTranslationX(-translationX2);
                back1.setTranslationX(-translationX2 + width2);
            }
        });
        animator3.start();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(getApplicationContext(), "PRESS BACK TO EXIT", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
