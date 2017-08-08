package widgetdemo.growing.com.uiwidgetlibrary.mywdiget.uiwidget.screenshot;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by 明正 on 2017/8/6.
 * 截屏
 * 1.全屏截屏
 * 2.区域截屏
 */

public class ScreenshotsUiUtils {
    private static ScreenshotsUiUtils screenshotsUiUtils;

    private ScreenshotsUiUtils() {
    }

    public static ScreenshotsUiUtils getIntence() {
        if (screenshotsUiUtils == null) {
            screenshotsUiUtils = new ScreenshotsUiUtils();
        }
        return screenshotsUiUtils;
    }

    /**
     * @param activity
     * @return
     */
    public Bitmap screenshotsWindow(Activity activity) {
        if (activity == null) throw new NullPointerException("activity对象为空！");
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        decorView.layout(0, 0, decorView.getMeasuredWidth(), decorView.getMeasuredHeight());
        decorView.buildDrawingCache();
        Bitmap bitmap = decorView.getDrawingCache();
        return bitmap;
    }

    /**
     * @param activity
     * @param view
     * @return
     */
    public Bitmap screenshotsChildWindow(Activity activity, View view) {
        if (activity == null || view == null) throw new NullPointerException("参数有误请检查");
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        decorView.layout(0, 0, decorView.getMeasuredWidth(), decorView.getMeasuredHeight());
        decorView.buildDrawingCache();
        Bitmap bitmap = decorView.getDrawingCache();
        if (bitmap != null) {
            //需要截取的长和宽
            int outWidth = view.getWidth();
            int outHeight = view.getHeight();
            //获取需要截图部分的在屏幕上的坐标(view的左上角坐标）
            int[] viewLocationArray = new int[2];
            view.getLocationOnScreen(viewLocationArray);
            //从屏幕整张图片中截取指定区域
            bitmap = Bitmap.createBitmap(bitmap, viewLocationArray[0], viewLocationArray[1], outWidth, outHeight);
        }
        return bitmap;
    }
}
