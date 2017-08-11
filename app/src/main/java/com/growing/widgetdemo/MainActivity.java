package com.growing.widgetdemo;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import widgetdemo.growing.com.uiwidgetlibrary.mywdiget.progress.GapRoundProgressBar;

public class MainActivity extends AppCompatActivity {
    GapRoundProgressBar mProgressBar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.tv);
        mProgressBar = (GapRoundProgressBar) findViewById(R.id.round_bar);
        mProgressBar.setShowSelect(true); //显示间隔
        mProgressBar.setSelect(80);
        mProgressBar.setSelectAngle(2);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 90);
        valueAnimator.setTarget(textView);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int i = Integer.valueOf(String.valueOf(animation.getAnimatedValue()));
                textView.setText(i + "");
                mProgressBar.setSelectRing((int) (360 * (i / 100f)));
            }
        });
        valueAnimator.start();
    }
}
