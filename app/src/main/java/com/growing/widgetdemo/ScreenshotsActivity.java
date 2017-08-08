package com.growing.widgetdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import widgetdemo.growing.com.uiwidgetlibrary.mywdiget.uiwidget.screenshot.ScreenshotsUiUtils;

/**
 * Created by 明正 on 2017/8/6.
 */

public class ScreenshotsActivity extends Activity {

    private ImageView mImageView1;
    private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshots);
        mImageView = (ImageView) findViewById(R.id.img_timg);
        mImageView1 = (ImageView) findViewById(R.id.img_timg1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = ScreenshotsUiUtils.getIntence().screenshotsChildWindow(ScreenshotsActivity.this,mImageView);
                            if (bitmap != null) {
                                mImageView1.setImageBitmap(bitmap);
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onScreen(View view) {

    }
}
