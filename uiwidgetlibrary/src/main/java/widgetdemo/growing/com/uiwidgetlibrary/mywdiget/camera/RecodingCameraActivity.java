package widgetdemo.growing.com.uiwidgetlibrary.mywdiget.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import widgetdemo.growing.com.uiwidgetlibrary.R;

/**
 * File: RecodingCameraActivity.java
 * Author: ejiang
 * Version: V100R001C01
 * Create: 2017-08-11 11:20
 */

public class RecodingCameraActivity extends AppCompatActivity {
    private LinearLayout mLlSuface;
    private Camera mCamera;
    private static boolean flash = false;
    private static boolean cameraFront = false;
    private CameraSurfaceView mCameraSurfaceView;
    private ImageView mImgClickShoot;
    private static String TAG = RecodingCameraActivity.class.getName();

    private static final int FOCUS_AREA_SIZE = 500;
    private ImageView mImgIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recoding_camera);
        mLlSuface = (LinearLayout) findViewById(R.id.ll_surface);
        mImgClickShoot = (ImageView) findViewById(R.id.img_click_shoot);
        mImgIcon = (ImageView) findViewById(R.id.img_icon);
        mCameraSurfaceView = new CameraSurfaceView(this, mCamera);
        mLlSuface.addView(mCameraSurfaceView);
        mCameraSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        focusOnTouch(event);
                    } catch (Exception e) {
                        Log.i(TAG, getString(R.string.fail_when_camera_try_autofocus, e.toString()));
                        //do nothing
                    }
                }
                return true;
            }
        });

        mImgClickShoot.setOnClickListener(clickShootListener);
    }

    private void focusOnTouch(MotionEvent event) {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters.getMaxNumMeteringAreas() > 0) {
                Rect rect = calculateFocusArea(event.getX(), event.getY());
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
                meteringAreas.add(new Camera.Area(rect, 800));
                parameters.setFocusAreas(meteringAreas);
                mCamera.setParameters(parameters);
                mCamera.autoFocus(mAutoFocusTakePictureCallback);
            } else {
                mCamera.autoFocus(mAutoFocusTakePictureCallback);
            }
        }
    }

    private Camera.AutoFocusCallback mAutoFocusTakePictureCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                // do something...
                Log.i("tap_to_focus", "success!");
            } else {
                // do something...
                Log.i("tap_to_focus", "fail!");
            }
        }
    };

    private Rect calculateFocusArea(float x, float y) {
        int left = clamp(Float.valueOf((x / mCameraSurfaceView.getWidth()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
        int top = clamp(Float.valueOf((y / mCameraSurfaceView.getHeight()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
        return new Rect(left, top, left + FOCUS_AREA_SIZE, top + FOCUS_AREA_SIZE);
    }

    private int clamp(int touchCoordinateInCameraReper, int focusAreaSize) {
        int result;
        if (Math.abs(touchCoordinateInCameraReper) + focusAreaSize / 2 > 1000) {
            if (touchCoordinateInCameraReper > 0) {
                result = 1000 - focusAreaSize / 2;
            } else {
                result = -1000 + focusAreaSize / 2;
            }
        } else {
            result = touchCoordinateInCameraReper - focusAreaSize / 2;
        }
        return result;
    }


    public static void startActivityForResult(Activity activity) {
        Intent intent = new Intent(activity, RecodingCameraActivity.class);
        activity.startActivity(intent);
    }

    //拍摄照片
    View.OnClickListener clickShootListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mCamera == null) {
                Toast.makeText(RecodingCameraActivity.this, R.string.camera_Authority_no, Toast.LENGTH_SHORT).show();
                return;
            }
            mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
        }
    };

    /*为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量*/
    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback()
            //快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
    {
        public void onShutter() {
            // TODO Auto-generated method stub
            Log.i(TAG, "myShutterCallback:onShutter...");
        }
    };
    Camera.PictureCallback mRawCallback = new Camera.PictureCallback()
            // 拍摄的未压缩原数据的回调,可以为null
    {

        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Log.i(TAG, "myRawCallback:onPictureTaken...");

        }
    };
    private int cameraId;

    Camera.PictureCallback mJpegPictureCallback = new Camera.PictureCallback() {
        //对jpeg图像数据的回调,最重要的一个回调
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.i(TAG, "myJpegCallback:onPictureTaken...");
            Bitmap b = null;
            if (null != data) {
                b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
                mCamera.stopPreview();
            }
            //保存图片到sdcard
            if (null != b) {
                //设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。
                //图片竟然不能旋转了，故这里要旋转下
                Bitmap mRotaBitmap = ImageUtil.getRotateBitmap(b, cameraId == 0 ? 90.0f : -90.0f, cameraId == 0 ? 1f : -1f);
                mImgIcon.setImageBitmap(mRotaBitmap);
                mCameraSurfaceView.refreshCamera(mCamera);
            }
            //拍摄完成后-- 选择 上传--是否重拍--删除之前拍摄照片 --重新拍摄
        }
    };

    boolean recording = false;
    //切换前置后置摄像头
    View.OnClickListener switchCameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!recording) {
                int camerasNumber = Camera.getNumberOfCameras();
                if (camerasNumber > 1) {
                    releaseCamera();
                    chooseCamera();
                } else {
                    //只有一个摄像头不允许切换
                    Toast.makeText(getApplicationContext(), R.string.only_have_one_camera
                            , Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    //选择摄像头
    public void chooseCamera() {
        try {
            if (cameraFront) {
                //当前是前置摄像头
                int cameraId = findBackFacingCamera();
                if (cameraId >= 0) {
                    // open the backFacingCamera
                    // set a picture callback
                    // refresh the preview
                    mCamera = Camera.open(cameraId);
                    // mPicture = getPictureCallback();
                    mCameraSurfaceView.refreshCamera(mCamera);
                    this.cameraId = 1;
                }
            } else {
                //当前为后置摄像头
                int cameraId = findFrontFacingCamera();
                if (cameraId >= 0) {
                    // open the backFacingCamera
                    // set a picture callback
                    // refresh the preview
                    this.cameraId = 0;
                    mCamera = Camera.open(cameraId);
                    if (flash) {
                        flash = false;
                        mCameraSurfaceView.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    }
                    // mPicture = getPictureCallback();
                    mCameraSurfaceView.refreshCamera(mCamera);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(RecodingCameraActivity.this, R.string.camera_Authority_no, Toast.LENGTH_SHORT).show();
        }
    }

    //闪光灯
    View.OnClickListener flashListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!recording && !cameraFront) {
                if (flash) {
                    flash = false;
                    setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                } else {
                    flash = true;
                    setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                }
            }
        }
    };

    //闪光灯
    public void setFlashMode(String mode) {
        try {
            if (getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA_FLASH)
                    && mCamera != null
                    && !cameraFront) {
                mCameraSurfaceView.setFlashMode(mode);
                mCameraSurfaceView.refreshCamera(mCamera);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), R.string.changing_flashLight_mode,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //检查设备是否有摄像头
        if (!hasCamera(getApplicationContext())) {
            Toast.makeText(this, "该台设备不存在摄像头！", Toast.LENGTH_SHORT).show();
            releaseCamera();
        }
        if (mCamera == null) {
            releaseCamera();
            final boolean frontal = cameraFront;
            int cameraId = findFrontFacingCamera();
            if (cameraId < 0) {
                //前置摄像头不存在
                switchCameraListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(RecodingCameraActivity.this, R.string.dont_have_front_camera, Toast.LENGTH_SHORT).show();
                    }
                };
                //尝试寻找后置摄像头
                cameraId = findBackFacingCamera();
                if (flash) {
                    mCameraSurfaceView.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                }
            } else if (!frontal) {
                cameraId = findBackFacingCamera();
                if (flash) {
                    mCameraSurfaceView.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                }
            }
            try {
                mCamera = Camera.open(cameraId);
                mCameraSurfaceView.refreshCamera(mCamera);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(RecodingCameraActivity.this, R.string.camera_Authority_no, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private boolean hasCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 找前置摄像头,没有则返回-1
     *
     * @return cameraId
     */
    private int findFrontFacingCamera() {
        int cameraId = -1;
        //获取摄像头个数
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    /**
     * 找后置摄像头,没有则返回-1
     *
     * @return cameraId
     */
    private int findBackFacingCamera() {
        int cameraId = -1;
        //获取摄像头个数
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

}
