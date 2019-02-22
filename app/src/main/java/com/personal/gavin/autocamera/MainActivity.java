package com.personal.gavin.autocamera;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private JavaCameraView camera_view;
    private int M_REQUEST_CODE = 203;
    private String[] permissions = {Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        camera_view = (JavaCameraView) findViewById(R.id.camera_view);
        camera_view.setCvCameraViewListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, M_REQUEST_CODE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera_view != null) {
            camera_view.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            camera_view.enableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (camera_view != null) {
            camera_view.disableView();
        }
    }

    private Mat mCannyResult;

    @Override
    public void onCameraViewStarted(int width, int height) {
        mCannyResult = new Mat(height, width, CvType.CV_8UC1);
    }

    @Override
    public void onCameraViewStopped() {
        mCannyResult.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Imgproc.Canny(inputFrame.gray(), mCannyResult, 60, 80);
        return mCannyResult;
    }
}
