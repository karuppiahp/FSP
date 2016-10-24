package com.gradapp.au.support;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

private SurfaceHolder prHolder;
private Camera prCamera;
public List<Camera.Size> prSupportedPreviewSizes;
private Camera.Size prPreviewSize;

@SuppressWarnings("deprecation")
public CameraPreview(Context context, Camera camera) {
    super(context);
    prCamera = camera;//Camera initialize
    //Caamera support sizes are fetched
    prSupportedPreviewSizes = prCamera.getParameters().getSupportedPreviewSizes();
    prHolder = getHolder();
    prHolder.addCallback(this);
    prHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
}

public void surfaceCreated(SurfaceHolder holder) {
    try {
        prCamera.setPreviewDisplay(holder);
        prCamera.startPreview();
    } catch (IOException e) {
        Log.d("Yologram", "Error setting camera preview: " + e.getMessage());
    }
}

public void surfaceDestroyed(SurfaceHolder holder) {
}

public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    if (prHolder.getSurface() == null){
      return;
    }

    try {
        prCamera.stopPreview();
    } catch (Exception e){
    }

    try {
    	//Focus Modes
        Camera.Parameters parameters = prCamera.getParameters();
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        parameters.setPreviewSize(prPreviewSize.width, prPreviewSize.height);
        prCamera.setParameters(parameters);
        prCamera.setPreviewDisplay(prHolder);
        prCamera.startPreview();

    } catch (Exception e){
        Log.d("Yologram", "Error starting camera preview: " + e.getMessage());
    }
}

@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

	//Height and width sizes
    final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
    final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);

    setMeasuredDimension(width, height);

    if (prSupportedPreviewSizes != null) {
        prPreviewSize = 
            getOptimalPreviewSize(prSupportedPreviewSizes, width, height);
    }    
}

public static Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {

    final double ASPECT_TOLERANCE = 0.1;
    double targetRatio = (double) h / w;

    if (sizes == null)
        return null;

    Camera.Size optimalSize = null;
    double minDiff = Double.MAX_VALUE;

    int targetHeight = h;

    for (Camera.Size size : sizes) {
        double ratio = (double) size.width / size.height;
        if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
            continue;

        if (Math.abs(size.height - targetHeight) < minDiff) {
            optimalSize = size;
            minDiff = Math.abs(size.height - targetHeight);
        }
    }

    if (optimalSize == null) {
        minDiff = Double.MAX_VALUE;
        for (Camera.Size size : sizes) {
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
    }

    return optimalSize;
}
}