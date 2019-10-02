package com.devsoft.as;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class over extends AppCompatActivity {

    TextView socre,highscore;
    Button ply_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_over);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        background();

        socre = findViewById(R.id.score);
        highscore = findViewById(R.id.high_score);
        ply_again = findViewById(R.id.again_btn);
        SharedPreferences storage = getSharedPreferences("data",MODE_PRIVATE);
        int score = storage.getInt("Score",0);
        socre.setText("Score : " + score);
        int temp_high = storage.getInt("HighScore",0);
        if(score>temp_high){
            SharedPreferences.Editor editor = storage.edit();
            editor.putInt("HighScore",score).commit();
            highscore.setText("High Score : "+score);
        }
        else {
            highscore.setText("High Score : "+temp_high);
        }

        ply_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(over.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    void background(){
        final ImageView backgroundOne1 = (ImageView) findViewById(R.id.bg_over);
        final ImageView backgroundTwo1 = (ImageView) findViewById(R.id.bg_over1);

        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(10000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress1 = (float) animation.getAnimatedValue();
                float width1 = backgroundOne1.getWidth();
                float translationX1 = width1 * progress1;
                backgroundOne1.setTranslationX(-translationX1);
                backgroundTwo1.setTranslationX(-translationX1 + width1);
            }
        });
        animator.start();
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
            Intent intent = new Intent(over.this,Home.class);
            startActivity(intent);
            finish();
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