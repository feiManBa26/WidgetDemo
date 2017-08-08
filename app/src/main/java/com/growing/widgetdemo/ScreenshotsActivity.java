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
    }

    public void onScreen(View view) {
        Bitmap bitmap = ScreenshotsUiUtils.getIntence().screenshotsWindow(this);
        if (bitmap != null) {
            mImageView1.setImageBitmap(bitmap);
        }
    }
}
