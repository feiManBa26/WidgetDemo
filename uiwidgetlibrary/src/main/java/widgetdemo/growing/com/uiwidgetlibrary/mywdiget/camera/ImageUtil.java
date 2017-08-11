package widgetdemo.growing.com.uiwidgetlibrary.mywdiget.camera;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageUtil {
    /**
     * ��תBitmap
     *
     * @param b
     * @param rotateDegree
     * @return
     */
    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree, float mirror) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) rotateDegree);
        matrix.postScale(mirror, 1f);
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
        return rotaBitmap;
    }
}
