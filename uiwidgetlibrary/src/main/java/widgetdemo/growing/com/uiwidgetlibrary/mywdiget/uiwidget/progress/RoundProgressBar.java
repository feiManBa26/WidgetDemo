package widgetdemo.growing.com.uiwidgetlibrary.mywdiget.uiwidget.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import widgetdemo.growing.com.uiwidgetlibrary.R;

/**
 * File: RoundProgressBar.java
 * Author: ejiang
 * Version: V100R001C01
 * Create: 2017-08-08 11:53
 */

public class RoundProgressBar extends View {
    /*view的高度*/
    protected int mViewHeight;
    /*view的宽度*/
    protected int mViewWidth;
    /*圆视图坐标中心点x点*/
    protected int viewAxisX;
    /*圆视图坐标中心点y点*/
    protected int viewAxisY;

    /*内部圆的半径*/
    protected int insideWidth;
    /*圆环的直径大小*/
    protected int ringDiameterWidth;
    /*外部圆环的默认颜色*/
    protected int ringOuterColor;
    /*内部圆环的默认颜色*/
    protected int insideRingColor;

    /*绘制圆环的颜色渐变color数组*/
    protected int[] colorArray = new int[3];

    /*显示弧度*/
    protected int selectRing;

    protected RectF mRectF;
    protected RectF mRectF1;

    protected Paint mPaint;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
        insideWidth = a.getInteger(R.styleable.RoundProgressBar_insideWidth, 400);
        ringDiameterWidth = a.getInteger(R.styleable.RoundProgressBar_ringDiameterWidth, 60);
        ringOuterColor = a.getInteger(R.styleable.RoundProgressBar_ringOuterColor, Color.parseColor("#d9d9d9"));
        insideRingColor = a.getInteger(R.styleable.RoundProgressBar_insideRingColor, Color.parseColor("#ffffff"));
        a.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //绘制可以抗锯齿的油漆标识
        mPaint.setAntiAlias(true);
        this.setWillNotDraw(false);
        colorArray[2] = Color.parseColor("#8EE484");
        colorArray[1] = Color.parseColor("#97C0EF");
        colorArray[0] = Color.parseColor("#8EE484");
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        viewAxisX = mViewWidth / 2;
        viewAxisY = mViewHeight / 2;
        mRectF = new RectF(viewAxisX - insideWidth - ringDiameterWidth / 2, viewAxisY - insideWidth - ringDiameterWidth / 2
                , viewAxisX + insideWidth + ringDiameterWidth / 2, viewAxisY + insideWidth + ringDiameterWidth / 2);
        mRectF1 = new RectF(viewAxisX - insideWidth + 30, viewAxisY - insideWidth + 30, viewAxisX + insideWidth - 30, viewAxisY + insideWidth - 30);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(insideRingColor);
        canvas.drawCircle(viewAxisX, viewAxisY, insideWidth, mPaint);
        //绘制外部圆环
        drawRingExternal(canvas);
        //绘制内部描边圆环
        drawStrokeRing(canvas);
        //绘制彩色描边进度
        drawStrokeRingArc(canvas);
    }

    /**
     * 绘制彩色描边进度
     *
     * @param canvas
     */
    protected void drawStrokeRingArc(Canvas canvas) {
        Paint ringColorPaint = new Paint(mPaint);
        ringColorPaint.setStyle(Paint.Style.STROKE);
        ringColorPaint.setStrokeWidth(ringDiameterWidth);
        ringColorPaint.setAntiAlias(true);
        ringColorPaint.setStrokeJoin(Paint.Join.ROUND);
        ringColorPaint.setStrokeCap(Paint.Cap.ROUND);
        ringColorPaint.setShader(new SweepGradient(viewAxisX, viewAxisY, colorArray, null));
        canvas.drawArc(mRectF, 270, selectRing, false, ringColorPaint);
    }

    protected void drawStrokeRing(Canvas canvas) {
        Paint ringNormalPaint = new Paint(mPaint);
        ringNormalPaint.setStyle(Paint.Style.STROKE);
        ringNormalPaint.setStrokeWidth(3);
        ringNormalPaint.setColor(ringOuterColor);
        canvas.drawArc(mRectF1, 270, 360, false, ringNormalPaint);
    }

    protected void drawRingExternal(Canvas canvas) {
        Paint ringNormalPaint = new Paint(mPaint);
        ringNormalPaint.setStyle(Paint.Style.STROKE);
        ringNormalPaint.setStrokeWidth(ringDiameterWidth);
        ringNormalPaint.setColor(ringOuterColor);
        canvas.drawArc(mRectF, 270, 360, false, ringNormalPaint);
    }


    public void setSelectRing(int selectRing) {
        this.selectRing = selectRing;
        this.invalidate();
    }

    public void setInsideWidth(int insideWidth) {
        this.insideWidth = insideWidth;
    }

    public void setRingDiameterWidth(int ringDiameterWidth) {
        this.ringDiameterWidth = ringDiameterWidth;
    }

    public void setRingOuterColor(int ringOuterColor) {
        this.ringOuterColor = ringOuterColor;
    }

    public void setInsideRingColor(int insideRingColor) {
        this.insideRingColor = insideRingColor;
    }

    public void setColorArray(int[] colorArray) {
        this.colorArray = colorArray;
    }

}
