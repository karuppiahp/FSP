package com.gradapp.au.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gradapp.au.AsyncTasks.HamburgerNameTasks;
import com.gradapp.au.register.RegisterTypeActivity;
import com.gradapp.au.utils.SessionStores;

public class SplashActivity extends Activity {

	Typeface typeFace, typeFaceGraduate;
	TextView textForUnivName, textForUnivSubName;
	RelativeLayout layoutForBg;
	ImageView imageForLogo;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);

		typeFace = Typeface
				.createFromAsset(getAssets(), "fonts/Time-Roman.ttf");
		typeFaceGraduate = Typeface.createFromAsset(getAssets(),
				"fonts/Graduate-Regular.ttf");
		progressDialog = new ProgressDialog(this);

		Timer t = new Timer();

		/*
		 * Session has been maintained for save the login type.
		 * Session is not null it is forwarded to RegisterType activity or else to HamburgerNameTask.
		 */
		
		if (SessionStores.getLoginPref(SplashActivity.this) == null) {
			TimerTask timerTask = new TimerTask() {
				@Override
				public void run() {
					Intent intent = new Intent(SplashActivity.this,
							RegisterTypeActivity.class);
					startActivity(intent);
					finish();
				}
			};
			t.schedule(timerTask, 3000);
		} else {
			new HamburgerNameTasks(SplashActivity.this, "splash",
					progressDialog).execute();
		}

	}

}
