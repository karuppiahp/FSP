package com.fsp.blacksheep;





import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Web_Info extends Activity {
	TextView tv;

	WebView webvw;
	private int mProgressStatus = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.info_web);
	
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.bs_header);

		/* Enabling strict mode */
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		tv = (TextView) findViewById(R.id.header_text1);
		String san_rough = "    ";
		tv.setTextSize(18);

		// tv.setText(san_rough+"The Black Sheep Online");
		tv.setText("Black Sheep");
		tv.setVisibility(View.VISIBLE);

		try {

			webvw = (WebView) findViewById(R.id.blacksheep_web);
			webvw.setInitialScale(50);
			webvw.loadUrl("http://www.theblacksheeponline.com/");
			new Web_Task().execute();
			webvw.getSettings().setBuiltInZoomControls(true);
		} catch (Exception e) {

		}

	}

	private class Web_Task extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(Web_Info.this);

		// can use UI thread here
		protected void onPreExecute() {
			this.dialog.setMessage("Loading...");
			this.dialog.setCancelable(false);
			this.dialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				while (mProgressStatus < 100) {
					mProgressStatus = webvw.getProgress();
				}
			} catch (Exception e) {

			}
			return null;

		}

		protected void onPostExecute(Void result) {

			if (this.dialog.isShowing()) {
				this.dialog.dismiss();

			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

}
