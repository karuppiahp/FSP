package com.gradapp.au.activities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gradapp.au.homescreen.CameraActivity;
import com.gradapp.au.slidingmenu.SlidingMenuActivity;
import com.gradapp.au.support.CameraPreview;
import com.gradapp.au.support.DBAdapter;
import com.gradapp.au.utils.Constant;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

@SuppressLint({ "SimpleDateFormat", "NewApi" })
public class PhotoScreenActivity extends SlidingMenuActivity implements
		OnClickListener, SurfaceHolder.Callback {

	Typeface typeFace, typeFaceHeader, typeFaceLight;
	ImageButton btnForiconSlider, btnForHamberger, btnForBack, zoomBtn,
			btnForZoomIn, btnForZoomOut, cameraSwitch;
	TextView textForHeader;
	ImageView imageForCameraBtn, imgForCapturedPic;
	Camera mCamera;
	public static String capturedImagePath = "";
	FrameLayout preview;
	CameraPreview mCameraPreview;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	Camera camera;
	boolean previewing = false;
	Animation animationFadeIn, animationFadeOut;
	AnimationSet animation;
	View view;
	int zoom;
	String galleryId, imageUrl;
	int currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

	Handler h;
	Runnable runnable;
	ProgressDialog dialog;
	Bitmap rotated;
	DBAdapter db;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_screen_2);

		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		typeFace = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Semibold.otf");
		typeFaceHeader = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Regular.otf");
		typeFaceLight = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Light.otf");
		db = new DBAdapter(this);

		//SurfaceView created for load camera
		surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		btnForiconSlider = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnForHamberger = (ImageButton) findViewById(R.id.btnHamberger);
		btnForBack = (ImageButton) findViewById(R.id.backBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		imageForCameraBtn = (ImageView) findViewById(R.id.imageForCameraBtn);
		imgForCapturedPic = (ImageView) findViewById(R.id.imageForCaptured);
		btnForZoomIn = (ImageButton) findViewById(R.id.zoomIn);
		btnForZoomOut = (ImageButton) findViewById(R.id.zoomOut);
		cameraSwitch = (ImageButton) findViewById(R.id.cameraSwitch);
		textForHeader.setTypeface(typeFaceHeader);
		btnForiconSlider.setVisibility(View.GONE);
		btnForHamberger.setVisibility(View.VISIBLE);
		btnForHamberger.setOnClickListener(this);
		btnForBack.setVisibility(View.VISIBLE);
		btnForBack.setOnClickListener(this);
		imgForCapturedPic.setEnabled(false);
		btnForZoomIn.setOnClickListener(this);
		btnForZoomOut.setOnClickListener(this);
		cameraSwitch.setOnClickListener(this);
		textForHeader.setTextColor(Color.parseColor("#FFFFFF"));

		dialog = new ProgressDialog(this);
		dialog.setMessage("Loading..");

		//Camera button clickability is set after the surface view is created so it is post delayed for few minutes.
		handler();
	}

	public void handler() {
		h = new Handler();
		runnable = new Runnable() {
			@Override
			public void run() {
				imageForCameraBtn.setOnClickListener(PhotoScreenActivity.this);
			}
		};
		h.postDelayed(runnable, 1000);
	}

	//After the picture captured the callback is called to write the image in folder.
	PictureCallback mPicture = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			File pictureFile = getOutputMediaFile();
			if (pictureFile == null) {
				return;
			}
			//Matrix conversion is used to rotate the image and saved in file, because in the app we maintained as in potrait mode 
			//if we save as it is means the image has been saved in different degree to avoid this the matrix rotation is used.
			try {
				InputStream is = new ByteArrayInputStream(data);
				Bitmap bmp = BitmapFactory.decodeStream(is);

				if (bmp != null) {
					int w = bmp.getWidth();
					int h = bmp.getHeight();
					// Setting post rotate to 90 and 270
					Matrix mtx = new Matrix();
					if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
						mtx.postRotate(90);
					} else {
						mtx.postRotate(270);
					}
					// Rotating Bitmap
					Bitmap rotatedBMP = Bitmap.createBitmap(bmp, 0, 0, w, h,
							mtx, true);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					rotatedBMP.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					byte[] byteArray = stream.toByteArray();

					//Write image file
					FileOutputStream fos = new FileOutputStream(pictureFile);
					fos.write(byteArray);
					fos.close();
				} else {
					FileOutputStream fos = new FileOutputStream(pictureFile);
					fos.write(data);
					fos.close();
				}

				Stop_Preview();
				Show_Preview();
				dialog.dismiss();
				
				//Update the image in gallery media mounted
				ContentValues values = new ContentValues();
				values.put(Images.Media.DATE_TAKEN,
						System.currentTimeMillis());
				values.put(Images.Media.MIME_TYPE, "image/jpeg");
				values.put(MediaStore.MediaColumns.DATA,
						capturedImagePath);
				PhotoScreenActivity.this.getContentResolver().insert(
						Images.Media.EXTERNAL_CONTENT_URI, values);

				//Captured image has been displayed in imageview after it writes in the path
				String path1 = capturedImagePath;
				Bitmap myBitmap = Utils.getBitmap(path1,
						PhotoScreenActivity.this);
				imgForCapturedPic.setImageBitmap(myBitmap);

				String pathReplace = "file:/" + path1;
				db.open();
				db.insertToImagePath(pathReplace);
				db.close();
				view.setVisibility(View.GONE);

				imageForCameraBtn.setEnabled(true);
				imageForCameraBtn.setClickable(true);
				
			} catch (Exception e) {

			}
		}
	};

	//File path has been defined to store the captured image
	private File getOutputMediaFile() {
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
				"Camera");
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}
		String userId;
		File mediaFile;
		userId = SessionStores.getUserId(PhotoScreenActivity.this) + "_"
				+ System.currentTimeMillis();
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ userId + ".jpg");
		capturedImagePath = mediaFile.getAbsolutePath();
		return mediaFile;
	}

	public void Show_Preview() {
		if (!previewing) {
			camera = Camera.open(currentCameraId);
			if (camera != null) {
				imageForCameraBtn.setEnabled(true);
				imageForCameraBtn.setClickable(true);
				try {
					//According to the parameters the surface view size for camera has been set
					Parameters parameters = camera.getParameters();
					List<Size> sizes = parameters.getSupportedPreviewSizes();
					Size optimalSize = getOptimalPreviewSize(sizes,
							getResources().getDisplayMetrics().widthPixels,
							getResources().getDisplayMetrics().heightPixels);
					parameters.setPreviewSize(optimalSize.width,
							optimalSize.height);

					List<String> focusModes = parameters
							.getSupportedFocusModes();
					if (focusModes
							.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
						parameters
								.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
					} else if (focusModes
							.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
						parameters
								.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
					}
					camera.setParameters(parameters);
					camera.setPreviewDisplay(surfaceHolder);
					camera.setDisplayOrientation(90);//Mode in Potrait
					camera.startPreview();
					previewing = true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void Stop_Preview() {
		if (camera != null && previewing) {
			camera.stopPreview();
			camera.release();
			camera = null;
			previewing = false;
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnHamberger) {
			showSecondaryMenu();
		}

		if (v.getId() == R.id.backBtn) {
			Stop_Preview();
			Intent intent = new Intent(PhotoScreenActivity.this,
					CameraActivity.class);
			startActivity(intent);
			finish();
		}

		if (v.getId() == R.id.imageForCameraBtn) {
			if (previewing == true) {
				dialog.setCancelable(false);
				dialog.show();
				camera.takePicture(null, null, mPicture);
				imgForCapturedPic.setEnabled(true);
				imgForCapturedPic.setOnClickListener(this);
			}

		}

		//Thumb view of captured image click functionality
		if (v.getId() == R.id.imageForCaptured) {
			if (Utils.isOnline()) {
				Stop_Preview();
				Intent intent = new Intent(PhotoScreenActivity.this,
						PhotoScreenOptions.class);
				intent.putExtra("from", "photo");
				intent.putExtra("galleryId", Constant.galleryId);
				intent.putExtra("galleryUrl", Constant.galleryUrl);
				startActivity(intent);
				finish();
			} else {
				Toast("Network connection is not available");
			}
		}

		if (v.getId() == R.id.zoomOut) {
			Camera.Parameters p = camera.getParameters();
			int maxZoom = p.getMaxZoom();
			if (p.isZoomSupported()) {
				zoom += 10;
				if (zoom > maxZoom) {
					zoom -= 10;
				}
				p.setZoom(zoom);
			}
			camera.setParameters(p);
			try {
				camera.setPreviewDisplay(surfaceHolder);
			} catch (Exception e) {
			}
			camera.startPreview();
		}

		if (v.getId() == R.id.zoomIn) {
			Camera.Parameters p = camera.getParameters();
			if (p.isZoomSupported()) {
				zoom -= 10;
				if (zoom <= 0) {
					zoom = 0;
				}
				p.setZoom(zoom);
			}
			camera.setParameters(p);
			try {
				camera.setPreviewDisplay(surfaceHolder);
			} catch (Exception e) {
			}
			camera.startPreview();
		}

		if (v.getId() == R.id.cameraSwitch) {
			if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
				currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
				Stop_Preview();
				Show_Preview();
			} else {
				currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
				Stop_Preview();
				Show_Preview();
			}
		}
	}

	public void refreshCamera() {
		if (surfaceHolder.getSurface() == null) {
			return;
		}
		try {
			camera.stopPreview();
		} catch (Exception e) {
		}
		try {
			camera.setPreviewDisplay(surfaceHolder);
			camera.startPreview();
		} catch (Exception e) {
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		Show_Preview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
	}

	public void Toast(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
			}
		});
	}

	//OptimalPreviewSize calculation
	public Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w,
			int h) {

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

	public void onBackPressed() {
		Stop_Preview();
		Intent intent = new Intent(PhotoScreenActivity.this,
				CameraActivity.class);
		startActivity(intent);
		finish();
		super.onBackPressed();
	}
}
