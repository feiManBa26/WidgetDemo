package widgetdemo.growing.com.uiwidgetlibrary.mywdiget.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * File: GapRoundProgressBar.java
 * Author: ejiang
 * Version: V100R001C01
 * Create: 2017-08-08 15:07
 */

public class GapRoundProgressBar extends RoundProgressBar {

    private boolean isShowSelect = false;   //是否显示断
    private int mSelect;    //分成多少段
    private int mSelectAngle;   //每个圆环之间的间隔
    private float mRingAngleWidth;  //每一段的角度

    public GapRoundProgressBar(Context context) {
        super(context);
    }

    public GapRoundProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GapRoundProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setColor(insideRingColor);
        canvas.drawCircle(viewAxisX, viewAxisY, insideWidth, mPaint);
        //绘制外部圆环
        drawRingExternal(canvas);
        //绘制内部描边圆环
        drawStrokeRing(canvas);
        //绘制彩色描边进度
        drawStrokeRingArc(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mRingAngleWidth = (360 - mSelect * mSelectAngle) / mSelect;
    }

    @Override
    protected void drawStrokeRingArc(Canvas canvas) {
        Paint ringColorPaint = new Paint(mPaint);
        ringColorPaint.setStyle(Paint.Style.STROKE);
        ringColorPaint.setStrokeWidth(ringDiameterWidth);
        ringColorPaint.setAntiAlias(true);
//        ringColorPaint.setStrokeJoin(Paint.Join.ROUND);
//        ringColorPaint.setStrokeCap(Paint.Cap.ROUND);
        ringColorPaint.setShader(new SweepGradient(viewAxisX, viewAxisY, colorArray, null));
        if (!isShowSelect) {
            canvas.drawArc(mRectF, 270, selectRing, false, ringColorPaint);
            return;
        }

        if (mSelect == selectRing && selectRing != 0 && mSelect != 0) {
            canvas.drawArc(mRectF, 270, 360, false, ringColorPaint);
        } else {
            Log.d(TAG, (mRingAngleWidth * selectRing + mSelectAngle + selectRing) + "");
            canvas.drawArc(mRectF, 270, mRingAngleWidth * selectRing + mSelectAngle * selectRing, false, ringColorPaint);
        }

        ringColorPaint.setShader(null);
        ringColorPaint.setColor(ringOuterColor);
        for (int i = 0; i < selectRing; i++) {
            canvas.drawArc(mRectF, 270 + (i * mRingAngleWidth + (i) * mSelectAngle), mSelectAngle, false, ringColorPaint);
        }

    }

    public void setShowSelect(boolean showSelect) {
        isShowSelect = showSelect;
    }

    /**
     * 添加多少段
     * @param select
     */
    public void setSelect(int select) {
        mSelect = select;
    }

    /**
     * 每段之间的间隔
     * @param selectAngle
     */
    public void setSelectAngle(int selectAngle) {
        mSelectAngle = selectAngle;
    }



}
