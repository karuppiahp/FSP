package com.gradapp.au.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gradapp.au.AsyncTasks.PhotoUploadTasks;
import com.gradapp.au.AsyncTasks.ReportTasks;
import com.gradapp.au.AsyncTasks.StreamPhotoRemoveTask;
import com.gradapp.au.AsyncTasks.TwitterHashTagTask;
import com.gradapp.au.slidingmenu.SlidingMenuActivity;
import com.gradapp.au.support.DBAdapter;
import com.gradapp.au.utils.Constant;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class PhotoScreenOptions extends SlidingMenuActivity implements OnClickListener {

	Typeface typeFace, typeFaceHeader, typeFaceLight;
	ImageButton btnForiconSlider, btnForHamberger, btnForBack;
	TextView textForHeader, textForPhotoName, textForBack, textForSave, textForDiscard, textForShare;
	EditText editTxtForPhotoName;
	String imagePath, from, imagePathFrom, reponse1, imageName, imageId, imageUserId, galleryId, galleryUrl, streamIdValue, pathFrom, streamIdFromStream, userFlag, imageCount;
	RelativeLayout layforPhotoDelete, layForPhotSave, layForPhotoBack, layForPhotoShare;
	boolean photoStatus = false, fromCheck = false, photoFromStream = false, photoSave = false, fromWhere = false;
	ImageView imageView, imgForDelete;
	Bitmap rotated;
	Dialog dialog;
	ProgressDialog progDialog;
	ProgressBar progressBar;
	
	String[] filterOptions = {"Rotate", "Brightness", "Round Corner", "Flip Vertical", "Flip Horizontal", "GreyScale"};
	String[] shareOptions = {"Twitter", "School Streams"};
	String[] streamName, streamId;
	
	ImageLoader imageLoader;
	DisplayImageOptions  options;
	
	ProgressDialog progressDialog;
	DBAdapter db;
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_screen_options);
		
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		typeFace = Typeface.createFromAsset(getAssets(), "fonts/ProximaNova-Semibold.otf");
		typeFaceHeader = Typeface.createFromAsset(getAssets(), "fonts/ProximaNova-Regular.otf");
		typeFaceLight = Typeface.createFromAsset(getAssets(), "fonts/ProximaNova-Light.otf");
		from = getIntent().getExtras().getString("from");
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		
		BitmapFactory.Options resizeOptions = new BitmapFactory.Options();
		resizeOptions.inSampleSize = 6; // decrease size 3 times
		resizeOptions.inScaled = true;
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				PhotoScreenOptions.this)
				.denyCacheImageMultipleSizesInMemory()
				.threadPriority(Thread.MAX_PRIORITY)
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
		
		options = new DisplayImageOptions.Builder()
	    .cacheInMemory(false).cacheOnDisc(false)
	    .imageScaleType(ImageScaleType.EXACTLY)
	    .bitmapConfig(Bitmap.Config.RGB_565)
	    .resetViewBeforeLoading(true).considerExifParams(true).build();
		
		db = new DBAdapter(this);
		progressDialog = new ProgressDialog(this);
		
		btnForiconSlider = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnForHamberger = (ImageButton) findViewById(R.id.btnHamberger);
		btnForBack = (ImageButton) findViewById(R.id.backBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		layforPhotoDelete = (RelativeLayout) findViewById(R.id.layForPhotoDelete);
		layForPhotSave = (RelativeLayout) findViewById(R.id.layForPhotoSave);
		layForPhotoBack = (RelativeLayout) findViewById(R.id.layForPhotoBack);
		layForPhotoShare = (RelativeLayout) findViewById(R.id.layForPhotoShare);
		textForPhotoName = (TextView) findViewById(R.id.textForPhotoName);
		editTxtForPhotoName = (EditText) findViewById(R.id.editTextForPhotoName);
		textForBack = (TextView) findViewById(R.id.textForPhotoBack);
		textForSave = (TextView) findViewById(R.id.textForPhotoSave);
		textForDiscard = (TextView) findViewById(R.id.textForPhotoDelete);
		textForShare = (TextView) findViewById(R.id.textForPhotoShare);
		imageView = (ImageView) findViewById(R.id.imageFromCapturing);
		imgForDelete = (ImageView) findViewById(R.id.pictureDelete);
		progressBar = (ProgressBar) findViewById(R.id.progressBarForImage);
		textForHeader.setTypeface(typeFaceHeader);
		textForPhotoName.setTypeface(typeFace);
		textForBack.setTypeface(typeFace);
		textForSave.setTypeface(typeFace);
		textForDiscard.setTypeface(typeFace);
		textForShare.setTypeface(typeFace);
		btnForiconSlider.setVisibility(View.GONE);
		btnForHamberger.setVisibility(View.VISIBLE);
		btnForHamberger.setOnClickListener(this);
		btnForBack.setVisibility(View.VISIBLE);
		btnForBack.setOnClickListener(this);
		layforPhotoDelete.setOnClickListener(this);
		layForPhotSave.setOnClickListener(this);
		layForPhotoBack.setOnClickListener(this);
		layForPhotoShare.setOnClickListener(this);
		textForHeader.setTextColor(Color.parseColor("#FFFFFF"));
		
		if(!(Utils.isOnline())) {
			Toast("Network connection is not available");
		}
		
		//Fetch the twitter HashTag for this school/college and passed along this hashTag with images while sharing
		new TwitterHashTagTask(this, progressDialog).execute();
		
		//Condition for the screen calls from stream grid screen or from camera screen
		if(from.equals("grid")) {
			//from stream grid screen
			fromCheck = true;
			galleryId = getIntent().getExtras().getString("imageId");
			galleryUrl = getIntent().getExtras().getString("imagePath");
			imagePathFrom = getIntent().getExtras().getString("imagePath");
			imagePath = getIntent().getExtras().getString("imagePath");
			pathFrom = getIntent().getExtras().getString("pathFrom");
			streamIdFromStream = getIntent().getExtras().getString("streamId");
			//Condition for the whether it comes from college streams or from Gallery screen
			if(pathFrom.equals("url")) {
				//from college streams
				Log.i("from","url");
				photoFromStream = true;
				fromWhere = true;
				imagePath = imagePathFrom;
				imageName = getIntent().getExtras().getString("imageName");
				imageId = getIntent().getExtras().getString("imageId");
				imageUserId = getIntent().getExtras().getString("imageUserId");
				//If session userId is not equals with the image userId means the report icon is displayed
				if(!(imageUserId.equals(SessionStores.getUserId(PhotoScreenOptions.this)))) {
					textForDiscard.setText("Report");
					imgForDelete.setImageResource(R.drawable.report_icon);
				}
				editTxtForPhotoName.setVisibility(View.GONE);
				textForPhotoName.setVisibility(View.GONE);
				try {
					
					imagePath = imagePath.replace("thumb_", "");
					//Load the api image in imageLoader
					imageLoader.displayImage(imagePath, imageView,
							options, new SimpleImageLoadingListener() {
	
								@Override
								public void onLoadingComplete(String imageUri, View view,
										Bitmap loadedImage) {
									super.onLoadingComplete(imageUri, view, loadedImage);
									progressBar.setVisibility(View.GONE);
								}
	
								@Override
								public void onLoadingFailed(String imageUri, View view,
										FailReason failReason) {
									super.onLoadingFailed(imageUri, view, failReason);
									imageView.setImageResource(R.drawable.icon);
									progressBar.setVisibility(View.GONE);
								}
	
								@Override
								public void onLoadingStarted(String imageUri, View view) {
									super.onLoadingStarted(imageUri, view);
									progressBar.setVisibility(View.VISIBLE);
								}
	
							});
				} catch(Exception e) {
					//Image is not loaded it redirects to stream Grid screen
					imageView.setImageResource(R.drawable.icon);
					Toast.makeText(PhotoScreenOptions.this, "ImagePath is not found", Toast.LENGTH_SHORT).show();
					Intent intentToGrid = new Intent(PhotoScreenOptions.this, StreamScreenGridActivity.class);
					intentToGrid.putExtra("from", "stream");
					intentToGrid.putExtra("streamId", streamIdFromStream);
					startActivity(intentToGrid);
					finish();
				}
			} else {
				//from gallery grid screen
				photoFromStream = true;
				fromWhere = false;
				imageCount = getIntent().getExtras().getString("imageCount");//imagecount calculates as whether it has 1 or more
				editTxtForPhotoName.setVisibility(View.GONE);
				textForPhotoName.setVisibility(View.GONE);
					String pathReplace = imagePath.replace("file:/", "");
					rotated = Utils.getBitmap(pathReplace, PhotoScreenOptions.this);
					
					try {
						Bitmap myBitmap = BitmapFactory.decodeFile(pathReplace);
				        int height = (myBitmap.getHeight() * 512 / myBitmap.getWidth());
				        Bitmap scale = Bitmap.createScaledBitmap(myBitmap, 512, height, true);
				        int rotate = 0;
				        //ExifInterface used to find the Orientation of image
				        ExifInterface exif = null;
						try {
				            exif = new ExifInterface(pathReplace);
				        } catch (IOException e) {
				            // TODO Auto-generated catch block
				            e.printStackTrace();
				        }
				        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
				                ExifInterface.ORIENTATION_UNDEFINED);
				        switch (orientation) {
				        case ExifInterface.ORIENTATION_NORMAL:
				            rotate = 0;
				            break;
				        case ExifInterface.ORIENTATION_ROTATE_270:
				            rotate = 270;
				            break;
				        case ExifInterface.ORIENTATION_ROTATE_180:
				            rotate = 180;
				            break;
				        case ExifInterface.ORIENTATION_ROTATE_90:
				            rotate = 90;
				            break;
				        }
	
				        //According to the potrait mode the orientation has been fixed.
				        Matrix matrix = new Matrix();
				        matrix.postRotate(rotate);
				        rotated = Bitmap.createBitmap(scale, 0, 0, scale.getWidth(),
				                scale.getHeight(), matrix, true);
				        imageView.setImageBitmap(rotated);
					} catch(Exception e) {
						//If image is not present it redirects to Gallery grid screen.
						imageView.setImageResource(R.drawable.icon);
						Toast.makeText(PhotoScreenOptions.this, "ImagePath is not found", Toast.LENGTH_SHORT).show();
						Intent intentToGrid = new Intent(PhotoScreenOptions.this, GalleryScreenCustomActivity.class);
						intentToGrid.putExtra("from", "gallery");
						intentToGrid.putExtra("imageCount", imageCount);
						startActivity(intentToGrid);
						finish();
					}
				
			}
		} else {
			//Image came from Camera activity
			fromCheck = false;
			editTxtForPhotoName.setVisibility(View.GONE);
			textForPhotoName.setVisibility(View.GONE);
			imagePath = PhotoScreenActivity.capturedImagePath;
			Log.i("Image path of current capture??????///",""+imagePath);
			Bitmap myBitmap = Utils.getBitmap(imagePath, PhotoScreenOptions.this);
			rotated = myBitmap;
			imageView.setImageBitmap(rotated);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.btnHamberger) {
			showSecondaryMenu();//right side menu
		}
		
		if(v.getId() == R.id.backBtn) {
			if(from.equals("grid")) {
				if(pathFrom.equals("url")) {
					//return to Stream grid screen
					Intent intentToGrid = new Intent(PhotoScreenOptions.this, StreamScreenGridActivity.class);
					intentToGrid.putExtra("from", "stream");
					intentToGrid.putExtra("streamId", streamIdFromStream);
					startActivity(intentToGrid);
					finish();
				} else {
					//return to gallery grid screen
					Intent intentToGrid = new Intent(PhotoScreenOptions.this, GalleryScreenCustomActivity.class);
					intentToGrid.putExtra("from", "gallery");
					intentToGrid.putExtra("imageCount", "greater");
					startActivity(intentToGrid);
					finish();
				}
			} else {
				//return to camera activity
				Intent intentToGrid = new Intent(PhotoScreenOptions.this, PhotoScreenActivity.class);
				startActivity(intentToGrid);
				finish();
			}
		}
		
		if(v.getId() == R.id.layForPhotoBack) {
			if(from.equals("grid")) {
				if(pathFrom.equals("url")) {
					//return to Stream grid screen
					Intent intentToGrid = new Intent(PhotoScreenOptions.this, StreamScreenGridActivity.class);
					intentToGrid.putExtra("from", "stream");
					intentToGrid.putExtra("streamId", streamIdFromStream);
					startActivity(intentToGrid);
					finish();
				} else {
					//return to gallery grid screen
					Intent intentToGrid = new Intent(PhotoScreenOptions.this, GalleryScreenCustomActivity.class);
					intentToGrid.putExtra("from", "gallery");
					intentToGrid.putExtra("imageCount", "greater");
					startActivity(intentToGrid);
					finish();
				}
			} else {
				//return to camera activity
				Intent intentToGrid = new Intent(PhotoScreenOptions.this, PhotoScreenActivity.class);
				startActivity(intentToGrid);
				finish();
			}
		}
		
		if(v.getId() == R.id.layForPhotoSave) {
			if(Utils.isOnline()) {
				//Save image according to the condition it came from screens.
				if(from.equals("grid")) { //came from grid screen
					if(pathFrom.equals("url")) { //came from college stream grid screen
						//Image saved from api to device.
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								PhotoScreenOptions.this);
	
						// set title
						alertDialogBuilder.setTitle("Save Image");
	
						// set dialog message
						alertDialogBuilder
								.setMessage(
										"Save this image to gallery?")
								.setCancelable(false)
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog, int id) {
												Log.i("streamIdFromStream",""+streamIdFromStream);
												if(imageUserId.equals(SessionStores.getUserId(PhotoScreenOptions.this))) {
													userFlag = "0";
												} else {
													userFlag = "1";
												}
												dialog.cancel();
												downloadImagesToSdCard(galleryUrl, imageName);
											}
										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog, int id) {
												// if this button is clicked, just
												// close
												// the dialog box and do nothing
												dialog.cancel();
											}
										});
						AlertDialog alertDialog = alertDialogBuilder.create();
	
						// show it
						alertDialog.show();
					} else { //came from gallery grid screen
						//Image saved from device to college stream.
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								PhotoScreenOptions.this);
	
						// set title
						alertDialogBuilder.setTitle("Save Image");
	
						// set dialog message
						alertDialogBuilder
								.setMessage(
										"Save this image to school stream?")
								.setCancelable(false)
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog, int id) {
												Log.i("loop enters","gallery");
												//stream name has been fectched and save the image according to the college/school user selected.
												if(Constant.streamArray.size() > 0) {
													streamName = new String[Constant.streamArray.size()];
													streamId = new String[Constant.streamArray.size()];
													for(int i=0; i<streamName.length; i++) {
														String stream = Constant.streamArray.get(i).get("streamName");
														Log.i("School Name????????",""+SessionStores.getSchoolName(PhotoScreenOptions.this));
														Log.i("stream Name????????",""+stream);
														Log.i("stream id////////",""+Constant.streamArray.get(i).get("streamId"));
														if(stream.equals(SessionStores.getSchoolName(PhotoScreenOptions.this))) {
															Log.i("stream id????",""+Constant.streamArray.get(i).get("streamId"));
															Log.i("gallery id",""+galleryId);
															Log.i("gallery Url",""+galleryUrl);
															streamIdValue = Constant.streamArray.get(i).get("streamId");
															//Before the image names are saved with text 90 and 270 according to this the image has been rotated and saved.
															//Now the matrix postRotateDegree has been used at the time of camera captured time itself it saves in correct orientation.
															//90 and 270 are used to display the image for old images which contains these text in names.
															if(imagePath.contains("Camera/")) {
																String[] imagePathSplits = imagePath.split("Camera/");
																String imageName = imagePathSplits[1];
																if(imageName.contains("#$%90")) {
																	imageName = imageName.replace("#$%90", "_");
																	imageName = imageName.replace(".png", "");
																} else if(imageName.contains("#$%270")) {
																	imageName = imageName.replace("#$%270", "_");
																	imageName = imageName.replace(".png", "");
																} else {
																	if(imageName.contains(".png")) {
																		imageName = imageName.replace(".png", "");
																	} else if(imageName.contains(".jpg")) {
																		imageName = imageName.replace(".jpg", "");
																	}
																}
																Log.i("imageName is??????",""+imageName);
																new PhotoUploadTasks(PhotoScreenOptions.this, imageName, streamIdValue, rotated).execute();
															} else {
																String[] imagePathSplits = imagePath.split("/");
																imageName = imagePathSplits[imagePathSplits.length-1];
																if(imageName.contains(".png")) {
																	imageName = imageName.replace(".png", "");
																} else if(imageName.contains(".jpg")) {
																	imageName = imageName.replace(".jpg", "");
																}
																new PhotoUploadTasks(PhotoScreenOptions.this, imageName, streamIdValue, rotated).execute();
															}
															
															Log.i("imageName is??????",""+imageName);
															break;
														}
													}
												} else {
													Utils.ShowAlert(PhotoScreenOptions.this, "No colleges list to show");
												}
												dialog.cancel();
											}
										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog, int id) {
												// if this button is clicked, just
												// close
												// the dialog box and do nothing
												dialog.cancel();
											}
										});
						AlertDialog alertDialog = alertDialogBuilder.create();
	
						// show it
						alertDialog.show();
					}
				} else {
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							PhotoScreenOptions.this);
	
					// set title
					alertDialogBuilder.setTitle("Save Image");
	
					// set dialog message
					alertDialogBuilder
							.setMessage(
									"Save this image to school stream?")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											//stream name has been fectched and save the image according to the college/school user selected.
											if(Constant.streamArray.size() > 0) {
												streamName = new String[Constant.streamArray.size()];
												streamId = new String[Constant.streamArray.size()];
												for(int i=0; i<streamName.length; i++) {
													String stream = Constant.streamArray.get(i).get("streamName");
													Log.i("School Name????????",""+SessionStores.getSchoolName(PhotoScreenOptions.this));
													Log.i("stream Name????????",""+stream);
													Log.i("stream id////////",""+Constant.streamArray.get(i).get("streamId"));
													if(stream.equals(SessionStores.getSchoolName(PhotoScreenOptions.this))) {
														Log.i("stream id????",""+Constant.streamArray.get(i).get("streamId"));
														Log.i("gallery id",""+Constant.galleryId);
														Log.i("gallery Url",""+galleryUrl);
														streamIdValue = Constant.streamArray.get(i).get("streamId");
														Log.i("imagePath is::::::::::",""+imagePath);
														//Before the image names are saved with text 90 and 270 according to this the image has been rotated and saved.
														//Now the matrix postRotateDegree has been used at the time of camera captured time itself it saves in correct orientation.
														//90 and 270 are used to display the image for old images which contains these text in names.
														String[] imagePathSplits = imagePath.split("Camera/");
														String imageName = imagePathSplits[1];
														if(imageName.contains("#$%90")) {
															imageName = imageName.replace("#$%90", "_");
														} else if(imageName.contains("#$%270")) {
															imageName = imageName.replace("#$%270", "_");
														}
														if(imageName.contains(".png")) {
															imageName = imageName.replace(".png", "");
														} else if(imageName.contains(".jpg")) {
															imageName = imageName.replace(".jpg", "");
														}
														Log.i("imageName is??????",""+imageName);
														new PhotoUploadTasks(PhotoScreenOptions.this, imageName, streamIdValue, rotated).execute();
														break;
													}
												}
											} else {
												Utils.ShowAlert(PhotoScreenOptions.this, "No colleges list to show");
											}
											dialog.cancel();
										}
									})
							.setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// if this button is clicked, just
											// close
											// the dialog box and do nothing
											dialog.cancel();
										}
									});
					AlertDialog alertDialog = alertDialogBuilder.create();
	
					// show it
					alertDialog.show();
				}
			} else {
				Toast("Network connection is not available");
			}
		}
		
		if(v.getId() == R.id.layForPhotoShare) {
			if(Utils.isOnline()) {
				if(photoFromStream == true) {
					if(pathFrom.equals("url")) {
						//image path from api the text and link has been concat and shared on social pages.
						Intent intent = new Intent(Intent.ACTION_SEND);
				        intent.setType("text/plain");
				        intent.putExtra(Intent.EXTRA_TEXT, galleryUrl+" "+Constant.hashTagValue);
				        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, ""+Constant.hashTagValue);
				        startActivity(Intent.createChooser(intent, "Share"));
					} else {
						//image path from gallery grid the file:/ has been replaced and call shareImage method because the image is present in device so we directly share the image
						String pathReplace = imagePath.replace("file:/", "");
						shareImage(pathReplace);
					}
				} else {
					//image path from camera activity and call shareImage method because the image is present in device so we directly share the image
					shareImage(imagePath);
				}
			} else {
				Toast("Network connection is not available");
			}
		}
		
		if (v.getId() == R.id.layForPhotoDelete) {
			if(Utils.isOnline()) {
				if (fromCheck == false) {
					//image from camera activity
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							PhotoScreenOptions.this);
	
					// set title
					alertDialogBuilder.setTitle("Discard Image");
	
					// set dialog message
					alertDialogBuilder
							.setMessage(
									"Delete this image from gallery?")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												final DialogInterface dialog, int id) {
											
											String pathReplace = imagePath.replace("file:/", "");
											File imageFile = new File(pathReplace);
											if (imageFile.exists()) {
											    if (imageFile.delete()) {
											        Log.i("file Deleted :" , ""+ imagePath);
											    } else {
											        Log.i("file not Deleted :", ""+ imagePath);
											    }
											} else {
												Log.i("file not found :", ""+ imagePath);
											}
											
											PhotoScreenOptions.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(pathReplace))));
											
											/*try 
										      { 
										         ContentResolver resolver=PhotoScreenOptions.this.getContentResolver(); 

										         // change type to image, otherwise nothing will be deleted 
										         ContentValues contentValues = new ContentValues(); 
										         int media_type = 1; 
										         contentValues.put("media_type", media_type); 
										         resolver.update(Uri.fromFile(imageFile), contentValues, null, null); 

										         boolean value = resolver.delete(Uri.fromFile(imageFile), null, null) > 0; 
										         Log.i("boolean value is>>>>>>>",""+value);
										      } 
										      catch (Throwable e) 
										      { 
										    	  Log.i("boolean false value is>>>>>>>","False"); 
										      } */
											
											//Refresh the /DCIM/Camera/ folder manually to reflect the picture in device Gallery immediately.
											//Normally the device Gallery takes sometime to reflect.
											/*final File mediaStorageDir = new File(
													Environment
															.getExternalStorageDirectory().toString(),
													"/DCIM/Camera");
											//To refresh the Gallery on and above KitKat version 
											if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
												MediaScannerConnection.scanFile(PhotoScreenOptions.this, new String[] {

														mediaStorageDir.getAbsolutePath()},

														null, new MediaScannerConnection.OnScanCompletedListener() {

														public void onScanCompleted(String path, Uri uri)

														{
															Log.i("mediaStorageDir path eleted",""+mediaStorageDir.getAbsolutePath());
															Log.i("ExternalStorage", "Scanned " + path + ":");
											                  Log.i("ExternalStorage", "-> uri=" + uri);
														}

														});
												
												MediaScannerConnection.scanFile(PhotoScreenOptions.this, new String[] {

														Environment.getExternalStorageDirectory().toString()},	

														null, new MediaScannerConnection.OnScanCompletedListener() {

														public void onScanCompleted(String path, Uri uri)

														{
															Log.i("mainPath path eleted",""+Environment.getExternalStorageDirectory().toString());
															Log.i("ExternalStorage", "Scanned " + path + ":");
											                  Log.i("ExternalStorage", "-> uri=" + uri);
														}

														});
												
											} else {
												//To refresh the Gallery below KitKat version
												sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
														 Uri.parse(mediaStorageDir.toString())));
											}*/
											
											
											
											Toast.makeText(PhotoScreenOptions.this, "It takes few seconds to delete, please wait..", Toast.LENGTH_LONG).show();
											//Handler used to focus on current screen for few mins to delete the image from gallery 
											Handler handler2 = new Handler();
											handler2.postDelayed(new Runnable() {
					                            @Override
					                            public void run() {
													dialog.cancel();
													Intent intentToGrid = new Intent(PhotoScreenOptions.this, PhotoScreenActivity.class);
													intentToGrid.putExtra("from", "gallery");
													startActivity(intentToGrid);
													finish();
					                            }
					                        }, 5000);
										}
									})
							.setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// if this button is clicked, just
											// close
											// the dialog box and do nothing
											dialog.cancel();
										}
									});
					AlertDialog alertDialog = alertDialogBuilder.create();
	
					// show it
					alertDialog.show();
				} else {
					//Image from stream grid screen
					if (photoFromStream == true) {
						if(fromWhere == false) {
							//image came from Gallery grid screen
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									PhotoScreenOptions.this);
	
							// set title
							alertDialogBuilder.setTitle("Discard Image");
	
							// set dialog message
							alertDialogBuilder
									.setMessage(
											"Delete this image from gallery?")
									.setCancelable(false)
									.setPositiveButton("Yes",
											new DialogInterface.OnClickListener() {
												public void onClick(
														final DialogInterface dialog, int id) {
													
													String pathReplace = imagePath.replace("file:/", "");
													File imageFile = new File(pathReplace);
													if (imageFile.exists()) {
													    if (imageFile.delete()) {
													        Log.i("file Deleted :" , ""+ imagePath);
													    } else {
													        Log.i("file not Deleted :", ""+ imagePath);
													    }
													} else {
														Log.i("file not found :", ""+ imagePath);
													}
													
													PhotoScreenOptions.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(pathReplace))));
													
													//Refresh the /DCIM/Camera/ folder manually to reflect the picture in device Gallery immediately.
													//Normally the device Gallery takes sometime to reflect.
													final File mediaStorageDir = new File(
															Environment
															.getExternalStorageDirectory().toString(),
													"/DCIM/Camera");
													//To refresh the Gallery on and above KitKat version 
													if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
														MediaScannerConnection.scanFile(PhotoScreenOptions.this, new String[] {

																mediaStorageDir.getAbsolutePath()},

																null, new MediaScannerConnection.OnScanCompletedListener() {

																public void onScanCompleted(String path, Uri uri)

																{
																	Log.i("mediaStorageDir path eleted",""+mediaStorageDir.getAbsolutePath());
																	Log.i("ExternalStorage", "Scanned " + path + ":");
													                  Log.i("ExternalStorage", "-> uri=" + uri);
																}

																});
														
														MediaScannerConnection.scanFile(PhotoScreenOptions.this, new String[] {

																Environment.getExternalStorageDirectory().toString()},

																null, new MediaScannerConnection.OnScanCompletedListener() {

																public void onScanCompleted(String path, Uri uri)

																{
																	Log.i("mainPath path eleted",""+Environment.getExternalStorageDirectory().toString());
																	Log.i("ExternalStorage", "Scanned " + path + ":");
													                  Log.i("ExternalStorage", "-> uri=" + uri);
																}

																});
														
													} else {
														//To refresh the Gallery below KitKat version
														sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
																 Uri.parse(mediaStorageDir.toString())));
													}
													
													
													Toast.makeText(PhotoScreenOptions.this, "It takes few seconds to delete, please wait..", Toast.LENGTH_LONG).show();
													//Handler used to focus on current screen for few mins to delete the image from gallery
													Handler handler2 = new Handler();
													handler2.postDelayed(new Runnable() {
							                            @Override
							                            public void run() {
															dialog.cancel();
															Intent intentToGrid = new Intent(PhotoScreenOptions.this, GalleryScreenCustomActivity.class);
															intentToGrid.putExtra("from", "gallery");
															intentToGrid.putExtra("imageCount", imageCount);
															startActivity(intentToGrid);
															finish();
							                            }
							                        }, 5000);
												}
											})
									.setNegativeButton("No",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog, int id) {
													// if this button is clicked, just
													// close
													// the dialog box and do nothing
													dialog.cancel();
												}
											});
							AlertDialog alertDialog = alertDialogBuilder.create();
	
							// show it
							alertDialog.show();
						} else {
							//image came from college stream grid
							if(imageUserId.equals(SessionStores.getUserId(PhotoScreenOptions.this))) {
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
										PhotoScreenOptions.this);
	
								// set title
								alertDialogBuilder.setTitle("Discard Image");
	
								// set dialog message
								alertDialogBuilder
										.setMessage(
												"Delete this image from stream gallery?")
										.setCancelable(false)
										.setPositiveButton("Yes",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog, int id) {
														//imageId and streamId has been passed in params to delete the iamge in removeApi.
														new StreamPhotoRemoveTask(PhotoScreenOptions.this, imageId, streamIdFromStream).execute();
														dialog.cancel();
													}
												})
										.setNegativeButton("No",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog, int id) {
														// if this button is clicked, just
														// close
														// the dialog box and do nothing
														dialog.cancel();
													}
												});
								AlertDialog alertDialog = alertDialogBuilder.create();
	
								// show it
								alertDialog.show();
							} else {
								showReportDialog();
							}
						}
					} 
				}
			} else {
				Toast("Network connection is not available");
			}
		}
		
		if(v.getId() == R.id.textForInappContent) {
			dialog.cancel();
			//User wants to report the image by clicking the text InappContent the process occurs
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					PhotoScreenOptions.this);

			// set title
			alertDialogBuilder.setTitle("Report Image");

			// set dialog message
			alertDialogBuilder
					.setMessage(
							"Do you really want to report this photo?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {
									//Report api has been called with appropriate params
									new ReportTasks(PhotoScreenOptions.this, SessionStores.getUserId(PhotoScreenOptions.this), imageId, imageUserId, getResources().getString(R.string.inapp_content)).execute();
									dialog.cancel();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {
									// if this button is clicked, just
									// close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
		}
		
		if(v.getId() == R.id.textForCopyrightInfgmnt) {
			dialog.cancel();
			//User wants to report the image by clicking the text CopyrightInfragment the process occurs
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					PhotoScreenOptions.this);

			// set title
			alertDialogBuilder.setTitle("Report Image");

			// set dialog message
			alertDialogBuilder
					.setMessage(
							"Do you really want to report this photo?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {
									//Report api has been called with appropriate params
									new ReportTasks(PhotoScreenOptions.this, SessionStores.getUserId(PhotoScreenOptions.this), imageId, imageUserId, getResources().getString(R.string.copyright_infrgmnt)).execute();
									dialog.cancel();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {
									// if this button is clicked, just
									// close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
		}
		
		if(v.getId() == R.id.textForOther) {
			dialog.cancel();
			//User wants to report the image by clicking the text Other the process occurs
			new ReportTasks(PhotoScreenOptions.this, SessionStores.getUserId(this), imageId, imageUserId, getResources().getString(R.string.other)).execute();
		}
	}
	
	//Diaalog has three options for report the image under these types
	private void showReportDialog() {
		dialog = new Dialog(PhotoScreenOptions.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.report_window);
		dialog.setCanceledOnTouchOutside(false);
		
		TextView textForInappContnt = (TextView)dialog.findViewById(R.id.textForInappContent);
		TextView textForCopyInfgmnt = (TextView)dialog.findViewById(R.id.textForCopyrightInfgmnt);
		TextView textForOther = (TextView)dialog.findViewById(R.id.textForOther);
		textForInappContnt.setOnClickListener(this);
		textForCopyInfgmnt.setOnClickListener(this);
		textForOther.setVisibility(View.GONE);
		dialog.show();
	}
	
	//Download the image from api to sdcard
	private boolean downloadImagesToSdCard(String img_url,String imageName)
 {
		try {
			String fname;
			img_url = img_url.replace("thumb_", "");
			URL url = new URL(img_url);
			File mediaStorageDir = new File(Environment
					.getExternalStorageDirectory().toString(), "/DCIM/Camera/");

			/* if specified not exist create new */
			if (!mediaStorageDir.exists()) {
				Log.i("dir ", "not found");
				mediaStorageDir.mkdir();
				Log.v("", "inside mkdir");
			} else {
				Log.i("dir ", "found");
			}

			/* checks the file and if it already exist delete */
			if (imageName.contains("#$%90")) {
				fname = imageName.replace("#$%90", "");
			} else if (imageName.contains("#$%270")) {
				fname = imageName.replace("#$%270", "");
			} else {
				fname = imageName;
			}

			if (fname.contains(".png")) {
				fname = fname.replace(".png", "");
			} else if (fname.contains(".jpg")) {
				fname = fname.replace(".jpg", "");
			}

			String userId = SessionStores.getUserId(PhotoScreenOptions.this)
					+ "_" + System.currentTimeMillis();
			File file = new File(mediaStorageDir.getPath() + File.separator
					+ userId + ".jpg");
			Log.i("File path is:::", "" + file.getAbsolutePath());

			/* Open a connection */
			URLConnection ucon = url.openConnection();
			InputStream inputStream = null;
			HttpURLConnection httpConn = (HttpURLConnection) ucon;
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				inputStream = httpConn.getInputStream();
			}

			FileOutputStream fos = new FileOutputStream(file);
			int totalSize = httpConn.getContentLength();
			int downloadedSize = 0;
			byte[] buffer = new byte[1024];
			int bufferLength = 0;
			while ((bufferLength = inputStream.read(buffer)) > 0) {
				fos.write(buffer, 0, bufferLength);
				downloadedSize += bufferLength;
				Log.i("Progress:", "downloadedSize:" + downloadedSize
						+ "totalSize:" + totalSize);
			}

			fos.close();
			
			ContentValues values = new ContentValues();
			values.put(Images.Media.DATE_TAKEN,
					System.currentTimeMillis());
			values.put(Images.Media.MIME_TYPE, "image/jpeg");
			values.put(MediaStore.MediaColumns.DATA,
					file.getAbsolutePath());
			PhotoScreenOptions.this.getContentResolver().insert(
					Images.Media.EXTERNAL_CONTENT_URI, values);

			/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				MediaScannerConnection.scanFile(PhotoScreenOptions.this,
						new String[] {

						mediaStorageDir.getAbsolutePath().toString() },

						null,
						new MediaScannerConnection.OnScanCompletedListener() {

							public void onScanCompleted(String path, Uri uri)

							{
								Log.i("mainPath path eleted", ""
										+ Environment
												.getExternalStorageDirectory()
												.toString());
								Log.i("ExternalStorage", "Scanned " + path
										+ ":");
								Log.i("ExternalStorage", "-> uri=" + uri);
							}

						});

				Intent mediaScanIntent = new Intent(
						Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				Uri contentUri = Uri.fromFile(mediaStorageDir);
				mediaScanIntent.setData(contentUri);
				sendBroadcast(mediaScanIntent);
			} else {
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
						Uri.parse(mediaStorageDir.toString())));
			}*/

			db.open();
			db.insertToImagePath("file:/" + file.getAbsolutePath().toString());
			db.close();

			rotated = Utils.getBitmap(file.getAbsolutePath(),
					PhotoScreenOptions.this);
			Toast.makeText(PhotoScreenOptions.this, "Image saved in sdcard..",
					Toast.LENGTH_SHORT).show();
		} catch (IOException io) {
			io.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}           
	
	//Share device image to the social pages are or mail and more
	private void shareImage(String imagePathFrom) {
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("image/*");
		File imageFileToShare = new File(imagePathFrom);
		Uri uri = Uri.fromFile(imageFileToShare);
		share.putExtra(Intent.EXTRA_TEXT, Constant.hashTagValue);
		share.putExtra(Intent.EXTRA_STREAM, uri);
		share.putExtra(android.content.Intent.EXTRA_SUBJECT, ""+Constant.hashTagValue);
		startActivity(Intent.createChooser(share, "Share"));
		}
	
	
	public void Toast(final String message)
    {
    	runOnUiThread(new Runnable(){
    	    public void run() {
    	    	Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
    	    }
    	 });
    }
	
	public void onBackPressed() {
		super.onBackPressed();
		if(from.equals("grid")) {
			if(pathFrom.equals("url")) {
				//return to Stream grid screen
				Intent intentToGrid = new Intent(PhotoScreenOptions.this, StreamScreenGridActivity.class);
				intentToGrid.putExtra("from", "stream");
				intentToGrid.putExtra("streamId", streamIdFromStream);
				startActivity(intentToGrid);
				finish();
			} else {
				//return to Gallery grid screen
				Intent intentToGrid = new Intent(PhotoScreenOptions.this, GalleryScreenCustomActivity.class);
				intentToGrid.putExtra("from", "gallery");
				intentToGrid.putExtra("imageCount", "greater");
				startActivity(intentToGrid);
				finish();
			}
		} else {
			//return to Camera screen
			Intent intentToGrid = new Intent(PhotoScreenOptions.this, PhotoScreenActivity.class);
			startActivity(intentToGrid);
			finish();
		}
	}
}
