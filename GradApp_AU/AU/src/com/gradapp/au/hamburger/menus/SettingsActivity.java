package com.gradapp.au.hamburger.menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gradapp.au.AsyncTasks.HandicapAccessTask;
import com.gradapp.au.AsyncTasks.NotifyStatusTask;
import com.gradapp.au.AsyncTasks.SettingsCollegeEditTask;
import com.gradapp.au.AsyncTasks.SettingsEditTasks;
import com.gradapp.au.activities.R;
import com.gradapp.au.homescreen.HomeActivity;
import com.gradapp.au.slidingmenu.SlidingMenuActivity;
import com.gradapp.au.support.DatePickerDialogWithMaxMinRange;
import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.twitter.TwitterLoginActivity;
import com.gradapp.au.utils.Constant;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;
import com.gradapp.au.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class SettingsActivity extends SlidingMenuActivity implements
		OnClickListener {

	Typeface typeFace, typeFaceHeader, typeFaceLight;
	ImageButton btnForiconSlider, btnForHamberger, btnForBack, btnForSave,
			btnForTwit;
	ImageView btnForProfileSave, btnForCollegeSave;
	Button btnForProfileEdit, btnForCollegeEdit;
	TextView textForHeader, txtForFirstName, txtForLastName, txtForDob,
			txtForGender, txtForStudentName, txtForMail, txtForRole,
			txtForSchool, txtForStudentNo, txtForStudentType,
			textForStudentTypeLabel, textForHandicapAccess,
			textForMyScheduleAnim, textForCommencementAnim,
			textForNotificationAnim, textForSocialMediaAnim, textForCameraAnim;
	ArrayList<String> genderArray = new ArrayList<String>();
	Spinner genderSpinner, graduateTypeSpinner, pickSchoolSpinner,
			studentTypeSpinner;
	EditText dobSpinner, editTxtForFirstName, editTxtForLastName,
			editTxtForMailId, editTextForEmail, editTxtForStudentName,
			editTxtForStudentNumber;
	RelativeLayout layoutForMainScreen, layoutForUnivIcon, layForMySchedule,
			layForCommencement, layForNotification, layForSocialMedia,
			layoutForFull, layoutForSwipe, layForCamera, layForUserProfileText,
			layForUserProfileTextFields, layForChangeCollegeTxt,
			layForChangeCollegeTxtFields, layForUserEditFileds,
			layForChangeCollegeEdit, layForChangeCollegeEditFields,
			layoutForStudentType, layoutForHandicapAccess;
	RelativeLayout.LayoutParams layout_description;
	ImageView imageForPushOn, imageForPushOff, imageForHandicapAccessOn,
			imageForHandicapAccessOff;
	boolean gender;
	LayoutInflater mInflator;
	private String genderString = "";
	static final int DATE_PICKER_ID = 1111;
	private int year;
	private int month;
	private int day;
	float x1, x2;
	float y1, y2;
	boolean touch = false, fb = false;
	Handler h;
	Runnable runnable;
	float density;
	ArrayList<HashMap<String, String>> roleList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> schoolList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> studentTypeArrayList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> roleListTemp = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> schoolListTemp = new ArrayList<HashMap<String, String>>();
	String roleSpinnerId, roleName = "", schoolName = "", schoolSpinnerId,
			studentTypeId, studentTypeName, gradType;
	boolean type, school, studentType;
	FrameLayout layoutForDob, layoutForStudentTypeFrame;
	View viewForTextViews;
	int lastIndex = 0;
	int count = 0;
	InputFilter filtertxt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		typeFace = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Regular.otf");
		genderArray.add("Male");
		genderArray.add("Female");

		btnForiconSlider = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnForHamberger = (ImageButton) findViewById(R.id.btnHamberger);
		btnForBack = (ImageButton) findViewById(R.id.backBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		layoutForFull = (RelativeLayout) findViewById(R.id.layoutForMaximizeClick);
		layoutForUnivIcon = (RelativeLayout) findViewById(R.id.layoutForunivIconMenus);
		layoutForMainScreen = (RelativeLayout) findViewById(R.id.layoutForSettingsFull);
		layoutForSwipe = (RelativeLayout) findViewById(R.id.layoutForSwipe);
		editTxtForFirstName = (EditText) findViewById(R.id.editTxtForFirstName);
		editTxtForLastName = (EditText) findViewById(R.id.editTxtForLastName);
		dobSpinner = (EditText) findViewById(R.id.spinnerForDob);
		editTxtForStudentName = (EditText) findViewById(R.id.editTxtForStudentName);
		pickSchoolSpinner = (Spinner) findViewById(R.id.spinnerForSchools);
		layForMySchedule = (RelativeLayout) findViewById(R.id.layMyScheduleAnim);
		layForCommencement = (RelativeLayout) findViewById(R.id.layCommencementAnim);
		layForNotification = (RelativeLayout) findViewById(R.id.layNotificationsAnim);
		layForSocialMedia = (RelativeLayout) findViewById(R.id.laySocialMediaAnim);
		layForCamera = (RelativeLayout) findViewById(R.id.layCameraAnim);
		editTextForEmail = (EditText) findViewById(R.id.editTxtForEmail);
		imageForPushOn = (ImageView) findViewById(R.id.imageForPushOn);
		imageForPushOff = (ImageView) findViewById(R.id.imageForPushOff);
		imageForHandicapAccessOn = (ImageView) findViewById(R.id.imageForHandicapOn);
		imageForHandicapAccessOff = (ImageView) findViewById(R.id.imageForHandicapOff);
		txtForFirstName = (TextView) findViewById(R.id.textForSettingsFirstName);
		txtForLastName = (TextView) findViewById(R.id.textForSettingsLastName);
		txtForStudentName = (TextView) findViewById(R.id.textForSettingsStudentName);
		txtForDob = (TextView) findViewById(R.id.textForSettingsDob);
		txtForGender = (TextView) findViewById(R.id.textForSettingsGender);
		txtForMail = (TextView) findViewById(R.id.textForSettingsEmail);
		txtForRole = (TextView) findViewById(R.id.textForSettingsRoleType);
		txtForSchool = (TextView) findViewById(R.id.textForSettingsSchoolName);
		layForUserProfileText = (RelativeLayout) findViewById(R.id.layoutForUserProfile);
		layForUserProfileTextFields = (RelativeLayout) findViewById(R.id.layoutForProfileTextFields);
		layForChangeCollegeTxt = (RelativeLayout) findViewById(R.id.layoutForChangeCollege);
		layForChangeCollegeTxtFields = (RelativeLayout) findViewById(R.id.layoutForChangeCollegeTextFields);
		layForUserEditFileds = (RelativeLayout) findViewById(R.id.layoutForEditTxtFields);
		layForChangeCollegeEdit = (RelativeLayout) findViewById(R.id.layoutForChangeCollegeSave);
		layForChangeCollegeEditFields = (RelativeLayout) findViewById(R.id.layoutForCollegeEditFields);
		btnForProfileEdit = (Button) findViewById(R.id.imageForUserProfileEdit);
		btnForCollegeEdit = (Button) findViewById(R.id.imageForCollegeEditBtn);
		btnForProfileSave = (ImageView) findViewById(R.id.imageForUserProfileSave);
		btnForCollegeSave = (ImageView) findViewById(R.id.imageForCollegeSave);
		graduateTypeSpinner = (Spinner) findViewById(R.id.spinnerForRoleTypr);
		btnForTwit = (ImageButton) findViewById(R.id.btnForTwitter);
		layoutForDob = (FrameLayout) findViewById(R.id.layoutForDob);
		viewForTextViews = (View) findViewById(R.id.viewForLayout);
		txtForStudentNo = (TextView) findViewById(R.id.textForSettingsStudentNo);
		editTxtForStudentNumber = (EditText) findViewById(R.id.editTxtForStudentNo);
		studentTypeSpinner = (Spinner) findViewById(R.id.spinnerForStudentType);
		txtForStudentType = (TextView) findViewById(R.id.textForSettingsStudentType);
		layoutForStudentType = (RelativeLayout) findViewById(R.id.layoutForStudentTypeText);
		layoutForStudentTypeFrame = (FrameLayout) findViewById(R.id.layoutForStudentType);
		textForStudentTypeLabel = (TextView) findViewById(R.id.editTextForSettingsStudentTypeLabel);
		layoutForHandicapAccess = (RelativeLayout) findViewById(R.id.layoutForHandicapAccess);
		textForMyScheduleAnim = (TextView) findViewById(R.id.txtForMySchedule);
		textForCommencementAnim = (TextView) findViewById(R.id.txtForCommenecementPrg);
		textForNotificationAnim = (TextView) findViewById(R.id.txtForNotifications);
		textForSocialMediaAnim = (TextView) findViewById(R.id.txtForSocialMedia);
		textForCameraAnim = (TextView) findViewById(R.id.txtForCamera);
		btnForiconSlider.setVisibility(View.VISIBLE);
		btnForHamberger.setVisibility(View.VISIBLE);
		btnForBack.setVisibility(View.GONE);
		btnForHamberger.setOnClickListener(this);
		btnForiconSlider.setOnClickListener(this);
		layForMySchedule.setOnClickListener(this);
		layForCommencement.setOnClickListener(this);
		layForNotification.setOnClickListener(this);
		layForSocialMedia.setOnClickListener(this);
		layForCamera.setOnClickListener(this);
		imageForPushOn.setOnClickListener(this);
		imageForHandicapAccessOn.setOnClickListener(this);
		imageForPushOff.setOnClickListener(this);
		imageForHandicapAccessOff.setOnClickListener(this);
		textForHeader.setTypeface(typeFace);
		dobSpinner.setFocusable(false);
		layoutForMainScreen.setClickable(false);
		layoutForUnivIcon.setVisibility(View.GONE);
		btnForProfileEdit.setOnClickListener(this);
		btnForCollegeEdit.setOnClickListener(this);
		btnForProfileSave.setOnClickListener(this);
		btnForCollegeSave.setOnClickListener(this);
		btnForTwit.setOnClickListener(this);
		btnForHamberger.setBackgroundResource(R.drawable.hamberger_black);
		textForMyScheduleAnim.setTypeface(typeFace);
		textForCommencementAnim.setTypeface(typeFace);
		textForNotificationAnim.setTypeface(typeFace);
		textForSocialMediaAnim.setTypeface(typeFace);
		textForCameraAnim.setTypeface(typeFace);

		if (SessionStores.getAccessToken(getBaseContext()) != null) {
			btnForTwit.setVisibility(View.GONE);
		} else {
			btnForTwit.setVisibility(View.VISIBLE);
		}

		if (Utils.isOnline()) {
			// Fetch the user profile details using follwing AsyncTask
			new SettingsTasks(this, txtForFirstName, txtForLastName,
					txtForStudentName, txtForDob, txtForGender, txtForMail,
					txtForRole, txtForSchool, imageForPushOn, imageForPushOff,
					imageForHandicapAccessOn, imageForHandicapAccessOff,
					editTxtForFirstName, editTxtForLastName, dobSpinner,
					editTextForEmail).execute();
		} else {
			Toast("Network connection is not available");
		}

		density = getResources().getDisplayMetrics().density;

		filtertxt = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				if (source.equals(" ")) {
					return source;
				}
				for (int i = start; i < end; i++) {
					if (!Character.isLetter(source.charAt(i))
							&& !Character.isSpaceChar(source.charAt(i))) {
						return "";
					}
				}
				return null;
			}
		};

	}

	private void initUI() {
		type = false;
		school = false;
		studentType = false;
		graduateTypeSpinner.setAdapter(typeSpinnerAdapter); //Custom Spinner for role types
		graduateTypeSpinner.setOnItemSelectedListener(typeSelectedListener);
		graduateTypeSpinner.setOnTouchListener(typeSpinnerTouchListener);
		pickSchoolSpinner.setAdapter(pickSchoolSpinnerAdapter); // Custom Spinner for school list
		pickSchoolSpinner.setOnItemSelectedListener(pickSchoolSelectedListener);
		pickSchoolSpinner.setOnTouchListener(pickSchoolSpinnerTouchListener);
		studentTypeSpinner.setAdapter(studentTypeSpinnerAdapter); // Custom Spinner for student types
		studentTypeSpinner
				.setOnItemSelectedListener(studentTypeSelectedListener);
		studentTypeSpinner.setOnTouchListener(studentTypeSpinnerTouchListener);
		mInflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
	}

	/*
	 * SpinnerAdapter for Graduate role types
	 */
	private SpinnerAdapter typeSpinnerAdapter = new BaseAdapter() {

		private TextView text;

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflator.inflate(R.layout.custom_spinner, null);
			}
			text = (TextView) convertView.findViewById(R.id.textForSpinnerItem);
			text.setTypeface(typeFace);
			if (!type) {
				roleSpinnerId = SessionStores
						.getRoleType(SettingsActivity.this);
				roleName = SessionStores.getRoleName(SettingsActivity.this);
				text.setText(roleName);
			} else {
				text.setText(roleList.get(position).get("roleName"));
				roleSpinnerId = roleList.get(position).get("roleId");
				roleName = roleList.get(position).get("roleName");
			}
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return roleList.get(position).get("roleName");
		}

		@Override
		public int getCount() {
			return roleList.size();
		}

		@SuppressLint("InflateParams")
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflator.inflate(R.layout.custom_spinner_text,
						null);
			}
			text = (TextView) convertView
					.findViewById(R.id.textForSpinnerItems);
			if (!type) {
				roleSpinnerId = SessionStores
						.getRoleType(SettingsActivity.this);
				roleName = SessionStores.getRoleName(SettingsActivity.this);
				text.setText(roleName);
			} else {
				text.setText(roleList.get(position).get("roleName"));
			}
			return convertView;
		};
	};

	/*
	 * SpinnerAdapter for School list
	 */
	private SpinnerAdapter pickSchoolSpinnerAdapter = new BaseAdapter() {

		private TextView text;

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflator.inflate(R.layout.custom_spinner, null);
			}
			text = (TextView) convertView.findViewById(R.id.textForSpinnerItem);
			text.setTypeface(typeFace);
			if (!school) {
				schoolSpinnerId = SessionStores
						.getSchoolId(SettingsActivity.this);
				schoolName = SessionStores.getSchoolName(SettingsActivity.this);
				text.setText(schoolName);
			} else {
				text.setText(schoolList.get(position).get("schoolName"));
				schoolSpinnerId = schoolList.get(position).get("schoolId");
				schoolName = schoolList.get(position).get("schoolName");
			}
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return schoolList.get(position).get("schoolName");
		}

		@Override
		public int getCount() {
			return schoolList.size();
		}

		@SuppressLint("InflateParams")
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflator.inflate(R.layout.custom_spinner_text,
						null);
			}
			text = (TextView) convertView
					.findViewById(R.id.textForSpinnerItems);
			text.setText(schoolList.get(position).get("schoolName"));
			return convertView;
		};
	};

	/*
	 * OnItemSelectedListener for Graduate role types
	 */
	private OnItemSelectedListener typeSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			roleSpinnerId = roleList.get(position).get("roleId");
			roleName = roleList.get(position).get("roleName");
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	/*
	 * OnTouchListener for Graduate role types
	 */
	private OnTouchListener typeSpinnerTouchListener = new OnTouchListener() {

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (roleList.size() > 0) {
				type = true;
			} else {
				type = false;
			}
			((BaseAdapter) typeSpinnerAdapter).notifyDataSetChanged();
			return false;
		}
	};

	/*
	 * OnItemSelectedListener for School List
	 */
	private OnItemSelectedListener pickSchoolSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			schoolSpinnerId = schoolList.get(position).get("schoolId");
			schoolName = schoolList.get(position).get("schoolName");
		}

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};

	/*
	 * OnTouchListener for School List
	 */
	private OnTouchListener pickSchoolSpinnerTouchListener = new OnTouchListener() {

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (schoolList.size() > 0) {
				school = true;
			} else {
				school = false;
			}
			((BaseAdapter) pickSchoolSpinnerAdapter).notifyDataSetChanged();
			return false;
		}
	};

	/*
	 * SpinnerAdapter for Student role types
	 */
	private SpinnerAdapter studentTypeSpinnerAdapter = new BaseAdapter() {

		private TextView text;

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflator.inflate(R.layout.custom_spinner, null);
			}
			text = (TextView) convertView.findViewById(R.id.textForSpinnerItem);
			text.setTypeface(typeFace);
			if (!studentType) {
				if (gradType.length() > 0) {
					text.setText(gradType);
				} else {
					text.setText("Select Type");
				}
			} else {
				text.setText(studentTypeArrayList.get(position).get(
						"studentType"));
			}
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return studentTypeArrayList.get(position).get("studentType");
		}

		@Override
		public int getCount() {
			return studentTypeArrayList.size();
		}

		@SuppressLint("InflateParams")
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			((InputMethodManager) getBaseContext().getSystemService(
					Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					editTxtForFirstName.getWindowToken(), 0);
			((InputMethodManager) getBaseContext().getSystemService(
					Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					editTxtForLastName.getWindowToken(), 0);

			if (convertView == null) {
				convertView = mInflator.inflate(R.layout.custom_spinner_text,
						null);
			}
			text = (TextView) convertView
					.findViewById(R.id.textForSpinnerItems);
			text.setText(studentTypeArrayList.get(position).get("studentType"));
			return convertView;
		};
	};

	/*
	 * OnItemSelectedListener for Student role types
	 */
	private OnItemSelectedListener studentTypeSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			studentTypeId = studentTypeArrayList.get(position).get("studentId");
			studentTypeName = studentTypeArrayList.get(position).get(
					"studentType");
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};

	/*
	 * OnTouchListener for Student role types
	 */
	@SuppressLint("ClickableViewAccessibility")
	private OnTouchListener studentTypeSpinnerTouchListener = new OnTouchListener() {

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (studentTypeArrayList.size() > 0) {
				studentType = true;
			} else {
				studentType = false;
			}
			((BaseAdapter) studentTypeSpinnerAdapter).notifyDataSetChanged();
			return false;
		}
	};

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnHamberger) {
			showSecondaryMenu();
		}

		if (v.getId() == R.id.btnSliderMenu) {
			if (h != null) {
				h.removeCallbacks(runnable);
			}
			Intent intentToHome = new Intent(SettingsActivity.this,
					HomeActivity.class);
			startActivity(intentToHome);
			finish();
		}

		//PushNotification ON button clicks 
		if (v.getId() == R.id.imageForPushOn) {
			Utils.hideKeyboard(SettingsActivity.this);
			imageForPushOn.setVisibility(View.GONE);
			imageForPushOff.setVisibility(View.VISIBLE);
			new NotifyStatusTask(SettingsActivity.this, "0").execute();
		}

		//PushNotification OFF button clicks 
		if (v.getId() == R.id.imageForPushOff) {
			Utils.hideKeyboard(SettingsActivity.this);
			imageForPushOn.setVisibility(View.VISIBLE);
			imageForPushOff.setVisibility(View.GONE);
			new NotifyStatusTask(SettingsActivity.this, "1").execute();
		}

		//HandicapAccess ON button clicks 
		if (v.getId() == R.id.imageForHandicapOn) {
			imageForHandicapAccessOn.setVisibility(View.GONE);
			imageForHandicapAccessOff.setVisibility(View.VISIBLE);
			new HandicapAccessTask(SettingsActivity.this, "0").execute();
		}

		//HandicapAccess OFF button clicks
		if (v.getId() == R.id.imageForHandicapOff) {
			imageForHandicapAccessOn.setVisibility(View.VISIBLE);
			imageForHandicapAccessOff.setVisibility(View.GONE);
			new HandicapAccessTask(SettingsActivity.this, "1").execute();
		}

		//User profile Edit button clicks
		if (v.getId() == R.id.imageForUserProfileEdit) {
			Utils.hideKeyboard(SettingsActivity.this);
			layForUserProfileText.setVisibility(View.GONE);
			layForUserProfileTextFields.setVisibility(View.GONE);
			layForUserEditFileds.setVisibility(View.VISIBLE);
			initUI();
		}

		//College tab edit button
		if (v.getId() == R.id.imageForCollegeEditBtn) {
			Utils.hideKeyboard(SettingsActivity.this);
			layForChangeCollegeTxt.setVisibility(View.GONE);
			layForChangeCollegeTxtFields.setVisibility(View.GONE);
			layForChangeCollegeEdit.setVisibility(View.VISIBLE);
			layForChangeCollegeEditFields.setVisibility(View.VISIBLE);
			initUI();
		}

		//User profile Save button
		if (v.getId() == R.id.imageForUserProfileSave) {
			Utils.hideKeyboard(SettingsActivity.this);
			if (!(editTxtForFirstName.getText().toString().equals(""))) {
				if (!(editTxtForFirstName.getText().toString().contains(" "))) {
					if (!(editTxtForLastName.getText().toString().equals(""))) {
						if (!(editTxtForLastName.getText().toString()
								.contains(" "))) {
							if (!(editTextForEmail.getText().toString()
									.equals(""))) {
								if (Utils.validEmail(editTextForEmail.getText()
										.toString())) {
									if (studentType == false) {
										for (int i = 0; i < studentTypeArrayList
												.size(); i++) {
											if (studentTypeArrayList.get(i)
													.get("studentType")
													.equals(gradType)) {
												studentTypeId = studentTypeArrayList
														.get(i)
														.get("studentId");
												break;
											}
										}
									} else {
										gradType = studentTypeName;
									}
									
									//All the required fields are filled the AsyncTask has been called
									new SettingsEditTasks(
											SettingsActivity.this,
											editTxtForFirstName.getText()
													.toString(),
											editTxtForLastName.getText()
													.toString(), dobSpinner
													.getText().toString(),
											genderString, editTextForEmail
													.getText().toString(),
											editTxtForStudentName.getText()
													.toString(), studentTypeId)
											.execute();
									layForUserProfileText
											.setVisibility(View.VISIBLE);
									layForUserProfileTextFields
											.setVisibility(View.VISIBLE);
									layForUserEditFileds
											.setVisibility(View.GONE);
									txtForFirstName.setText(editTxtForFirstName
											.getText().toString());
									txtForLastName.setText(editTxtForLastName
											.getText().toString());
									txtForStudentType.setText(gradType);

									//According to the role type the changes will be reflect in updated fields
									if (roleName.equals("Guest")) {
										editTxtForStudentName
												.setVisibility(View.GONE);
										layoutForDob.setVisibility(View.GONE);
										layoutForStudentType
												.setVisibility(View.GONE);
										layoutForStudentTypeFrame
												.setVisibility(View.GONE);
										textForStudentTypeLabel
												.setVisibility(View.GONE);
										layoutForHandicapAccess
												.setVisibility(View.VISIBLE);
									} else if (roleName.equals("Faculty")) {
										editTxtForStudentName
												.setVisibility(View.GONE);
										layoutForDob.setVisibility(View.GONE);
										viewForTextViews
												.setVisibility(View.GONE);
										layoutForStudentType
												.setVisibility(View.GONE);
										layoutForStudentTypeFrame
												.setVisibility(View.GONE);
										textForStudentTypeLabel
												.setVisibility(View.GONE);
										layoutForHandicapAccess
												.setVisibility(View.GONE);
									} else if (roleName.equals("Graduate")) {
										editTxtForStudentName
												.setVisibility(View.GONE);
										editTxtForStudentNumber
												.setVisibility(View.GONE);
										txtForStudentNo
												.setVisibility(View.GONE);
										layoutForDob.setVisibility(View.GONE);
										layoutForStudentType
												.setVisibility(View.VISIBLE);
										layoutForStudentTypeFrame
												.setVisibility(View.VISIBLE);
										textForStudentTypeLabel
												.setVisibility(View.VISIBLE);
										layoutForHandicapAccess
												.setVisibility(View.GONE);
									} else if (roleName.equals("Student")) {
										editTxtForStudentName
												.setVisibility(View.GONE);
										editTxtForStudentNumber
												.setVisibility(View.GONE);
										txtForStudentNo
												.setVisibility(View.GONE);
										layoutForDob.setVisibility(View.GONE);
										layoutForStudentType
												.setVisibility(View.VISIBLE);
										layoutForStudentTypeFrame
												.setVisibility(View.VISIBLE);
										textForStudentTypeLabel
												.setVisibility(View.VISIBLE);
										layoutForHandicapAccess
												.setVisibility(View.GONE);
									} else {
										editTxtForStudentName
												.setVisibility(View.GONE);
										layoutForDob.setVisibility(View.GONE);
										layoutForStudentType
												.setVisibility(View.GONE);
										layoutForStudentTypeFrame
												.setVisibility(View.GONE);
										textForStudentTypeLabel
												.setVisibility(View.GONE);
										layoutForHandicapAccess
												.setVisibility(View.GONE);
									}

									initUI();
									txtForMail.setText(editTextForEmail
											.getText().toString());
								} else {
									Toast("Please enter valid Email");
								}
							} else {
								Toast("Email field must not be empty");
							}
						} else {
							Toast("Lastname must not contain space");
						}
					} else {
						Toast("Lastname field must not be empty");
					}
				} else {
					Toast("Firstname must not contain space");
				}
			} else {
				Toast("Firstname field must not be empty");
			}
		}

		//College Save button functionality
		if (v.getId() == R.id.imageForCollegeSave) {
			Utils.hideKeyboard(SettingsActivity.this);

			if (type == false) {
				roleName = txtForRole.getText().toString();
				for (int i = 0; i < roleList.size(); i++) {
					if (roleList.get(i).get("roleName").equals(roleName)) {
						roleSpinnerId = roleList.get(i).get("roleId");
						roleName = roleList.get(i).get("roleName");
						break;
					}
				}
			}

			if (school == false) {
				schoolName = txtForSchool.getText().toString();
				for (int i = 0; i < schoolList.size(); i++) {
					if (schoolList.get(i).get("schoolName").equals(schoolName)) {
						schoolSpinnerId = schoolList.get(i).get("schoolId");
						schoolName = schoolList.get(i).get("schoolName");
						break;
					}
				}
			}

			//Save the college changes once the fields are changed the AsyncTask is called
			new SettingsCollegeEditTask(SettingsActivity.this, roleSpinnerId,
					schoolSpinnerId).execute();
			SessionStores.saveRoleType(roleSpinnerId, SettingsActivity.this);
			SessionStores.saveRoleName(roleName, SettingsActivity.this);
			SessionStores.saveSchoolId(schoolSpinnerId, SettingsActivity.this);
			SessionStores.saveSchoolName(schoolName, SettingsActivity.this);

			roleListTemp.clear();
			for (int i = 0; i < roleList.size(); i++) {
				String roleId = roleList.get(i).get("roleId");
				String roleNameValue = roleList.get(i).get("roleName");

				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("roleId", roleId);
				hashMap.put("roleName", roleNameValue);
				roleListTemp.add(hashMap);
			}

			roleList.clear();
			for (int i = 0; i < roleListTemp.size(); i++) {
				String roleId = roleListTemp.get(i).get("roleId");
				String roleNameValue = roleListTemp.get(i).get("roleName");

				HashMap<String, String> hashMap = new HashMap<String, String>();

				if (roleNameValue.equals(roleName)) {
					hashMap.put("roleId", roleId);
					hashMap.put("roleName", roleName);
					roleList.add(0, hashMap);
				} else {
					hashMap.put("roleId", roleId);
					hashMap.put("roleName", roleNameValue);
					roleList.add(hashMap);
				}
			}

			schoolListTemp.clear();
			for (int i = 0; i < schoolList.size(); i++) {
				String schoolId = schoolList.get(i).get("schoolId");
				String schoolName = schoolList.get(i).get("schoolName");

				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("schoolId", schoolId);
				hashMap.put("schoolName", schoolName);
				schoolListTemp.add(hashMap);
			}

			schoolList.clear();
			for (int i = 0; i < schoolListTemp.size(); i++) {
				String schoolId = schoolListTemp.get(i).get("schoolId");
				String schoolNameValue = schoolListTemp.get(i)
						.get("schoolName");

				HashMap<String, String> hashMap = new HashMap<String, String>();

				if (schoolNameValue.equals(schoolName)) {
					hashMap.put("schoolId", schoolId);
					hashMap.put("schoolName", schoolName);
					schoolList.add(0, hashMap);
				} else {
					hashMap.put("schoolId", schoolId);
					hashMap.put("schoolName", schoolNameValue);
					schoolList.add(hashMap);
				}
			}

			layForChangeCollegeTxt.setVisibility(View.VISIBLE);
			layForChangeCollegeTxtFields.setVisibility(View.VISIBLE);
			layForChangeCollegeEdit.setVisibility(View.GONE);
			layForChangeCollegeEditFields.setVisibility(View.GONE);
			txtForRole.setText(roleName);
			txtForSchool.setText(schoolName);

			txtForStudentType.setText(gradType);
			initUI();

			//According to the role types the fields has been displayed.
			if (roleName.equals("Guest")) {
				editTxtForStudentName.setVisibility(View.GONE);
				layoutForDob.setVisibility(View.GONE);
				layoutForStudentType.setVisibility(View.GONE);
				layoutForStudentTypeFrame.setVisibility(View.GONE);
				textForStudentTypeLabel.setVisibility(View.GONE);
				layoutForHandicapAccess.setVisibility(View.VISIBLE);
			} else if (roleName.equals("Faculty")) {
				editTxtForStudentName.setVisibility(View.GONE);
				layoutForDob.setVisibility(View.GONE);
				viewForTextViews.setVisibility(View.GONE);
				layoutForStudentType.setVisibility(View.GONE);
				layoutForStudentTypeFrame.setVisibility(View.GONE);
				textForStudentTypeLabel.setVisibility(View.GONE);
				layoutForHandicapAccess.setVisibility(View.GONE);
			} else if (roleName.equals("Graduate")) {
				editTxtForStudentName.setVisibility(View.GONE);
				editTxtForStudentNumber.setVisibility(View.GONE);
				txtForStudentNo.setVisibility(View.GONE);
				layoutForDob.setVisibility(View.GONE);
				layoutForStudentType.setVisibility(View.VISIBLE);
				layoutForStudentTypeFrame.setVisibility(View.VISIBLE);
				textForStudentTypeLabel.setVisibility(View.VISIBLE);
				layoutForHandicapAccess.setVisibility(View.GONE);
			} else if (roleName.equals("Student")) {
				editTxtForStudentName.setVisibility(View.GONE);
				editTxtForStudentNumber.setVisibility(View.GONE);
				txtForStudentNo.setVisibility(View.GONE);
				layoutForDob.setVisibility(View.GONE);
				layoutForStudentType.setVisibility(View.VISIBLE);
				layoutForStudentTypeFrame.setVisibility(View.VISIBLE);
				textForStudentTypeLabel.setVisibility(View.VISIBLE);
				layoutForHandicapAccess.setVisibility(View.GONE);
			} else {
				editTxtForStudentName.setVisibility(View.GONE);
				layoutForDob.setVisibility(View.GONE);
				layoutForStudentType.setVisibility(View.GONE);
				layoutForStudentTypeFrame.setVisibility(View.GONE);
				textForStudentTypeLabel.setVisibility(View.GONE);
				layoutForHandicapAccess.setVisibility(View.GONE);
			}
		}

		// Twitter button click functionality
		if (v.getId() == R.id.btnForTwitter) {
			SessionStores.saveConsumerKey(Constant.consumer_key,
					SettingsActivity.this);
			SessionStores.saveConsumerSecretKey(Constant.secret_key,
					SettingsActivity.this);

			Fragment login = new TwitterLoginActivity();
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.content_frame, login);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(null);
			ft.commit();
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_ID:

			return new DatePickerDialogWithMaxMinRange(this,
					new OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							dobSpinner.setText(new StringBuilder()
									.append(monthOfYear + 1).append("-")
									.append(dayOfMonth).append("").append("-")
									.append(year));
							try {
							} catch (Exception e) {

							}

						}

					}, 1900, 0, 1, year, month, day);
		}
		return null;
	}

	/*
	 * AsyncTask used to fetch the User Profile details and placed in fields.
	 */
	public class SettingsTasks extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog;
		Activity activity;
		String status, message, lastName, firstName, email, dob,
				notifyStatus = "", handicapStatus = "", studentName, userRole,
				studentNumber;
		Spinner spinnerGender;
		EditText editTxtFirstName, editTxtLastName, spinnerDob, editTxtMail;
		TextView txtForFirstName, txtForLastName, txtForStudentName, txtForDob,
				txtForGeder, txtForMail, txtForRole, txtForSchool;
		ImageView imageForPushOn, imageForPushOff, imgForHandicapOn,
				imgForHandicapOff;

		public SettingsTasks(Activity context, TextView textForFirstName,
				TextView textForLastName, TextView textForStudentName,
				TextView textForDob, TextView textForGender,
				TextView textForMailId, TextView textForRole,
				TextView textForSchool, ImageView imageForpushOn,
				ImageView imageForpushOff, ImageView imageForHandicapAccessOn,
				ImageView imageForHandicapAccessOff,
				EditText editTxtForFirstName, EditText editTxtForLastName,
				EditText dobSpinner, EditText editTxtForMailId) {
			activity = context;
			txtForFirstName = textForFirstName;
			txtForLastName = textForLastName;
			txtForStudentName = textForStudentName;
			txtForDob = textForDob;
			txtForGeder = textForGender;
			txtForMail = textForMailId;
			txtForRole = textForRole;
			txtForSchool = textForSchool;
			spinnerGender = genderSpinner;
			imageForPushOn = imageForpushOn;
			imageForPushOff = imageForpushOff;
			imgForHandicapOn = imageForHandicapAccessOn;
			imgForHandicapOff = imageForHandicapAccessOff;
			editTxtFirstName = editTxtForFirstName;
			editTxtLastName = editTxtForLastName;
			spinnerDob = dobSpinner;
			editTxtMail = editTxtForMailId;
			dialog = new ProgressDialog(activity);
			dialog.setMessage("Loading..");
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();

			dialog.show();
			dialog.setCancelable(false);
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		protected Boolean doInBackground(Void... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			nameValuePairs.add(new BasicNameValuePair("university_id",
					SessionStores.getUnivId(activity)));
			nameValuePairs.add(new BasicNameValuePair("school_id",
					SessionStores.getSchoolId(activity)));
			nameValuePairs.add(new BasicNameValuePair("id", SessionStores
					.getUserId(activity)));
			JSONObject jsonObj = new ServerResponse(UrlGenerator.settingsView())
					.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
			try {
				if (jsonObj != null) {
					roleList.clear();
					schoolList.clear();
					status = jsonObj.getString("status");
					message = jsonObj.getString("msg");
					if (status.equals("1")) {
						JSONObject userDetailsObj = jsonObj
								.getJSONObject("User Details");
						firstName = userDetailsObj.getString("Firstname");
						lastName = userDetailsObj.getString("Lastname");
						studentName = userDetailsObj.getString("Student Name");
						email = userDetailsObj.getString("Email");
						dob = userDetailsObj.getString("Dob");
						schoolName = userDetailsObj.getString("Schoolname");
						roleName = userDetailsObj.getString("UserRole");
						studentNumber = userDetailsObj
								.getString("Student Number");
						notifyStatus = userDetailsObj
								.getString("Notification status");
						handicapStatus = userDetailsObj
								.getString("HandicapAccess status");
						gradType = userDetailsObj.getString("Grad type id");

						//Array list for role types
						JSONArray jsonRoleArray = jsonObj
								.getJSONArray("Role Details");
						for (int i = 0; i < jsonRoleArray.length(); i++) {
							String roleId = jsonRoleArray.getJSONObject(i)
									.getString("id");
							String roleName = jsonRoleArray.getJSONObject(i)
									.getString("name");

							String roleIdSession = SessionStores
									.getRoleType(SettingsActivity.this);
							String roleNameSession = SessionStores
									.getRoleName(SettingsActivity.this);

							HashMap<String, String> hashMap = new HashMap<String, String>();

							if (roleNameSession.equals(roleName)) {
								hashMap.put("roleId", roleIdSession);
								hashMap.put("roleName", roleNameSession);
								roleList.add(0, hashMap);
							} else {
								hashMap.put("roleId", roleId);
								hashMap.put("roleName", roleName);
								roleList.add(hashMap);
							}
						}

						//Array list for School list
						JSONArray jsonSchoolArray = jsonObj
								.getJSONArray("School Details");
						for (int i = 0; i < jsonSchoolArray.length(); i++) {
							String schoolId = jsonSchoolArray.getJSONObject(i)
									.getString("id");
							String schoolName = jsonSchoolArray
									.getJSONObject(i).getString("name");

							String schoolIdSession = SessionStores
									.getSchoolId(SettingsActivity.this);
							String schoolNameSession = SessionStores
									.getSchoolName(SettingsActivity.this);

							HashMap<String, String> hashMap = new HashMap<String, String>();

							if (schoolNameSession.equals(schoolName)) {
								hashMap.put("schoolId", schoolIdSession);
								hashMap.put("schoolName", schoolNameSession);
								schoolList.add(0, hashMap);
							} else {
								hashMap.put("schoolId", schoolId);
								hashMap.put("schoolName", schoolName);
								schoolList.add(hashMap);
							}
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			initUI(); //To initialize the spinner arrays
			txtForFirstName.setText(firstName);
			txtForLastName.setText(lastName);
			txtForStudentName.setText(studentName);
			txtForStudentNo.setText(studentNumber);
			txtForStudentType.setText(gradType);

			/*
			 * Display the Student types the values are fetch from SessionStores getStudentType method which is saved on register activity.
			 */			
			String studentTypeSession = SessionStores
					.getStudentType(SettingsActivity.this);
			ArrayList<HashMap<String, String>> studentSplitList = new ArrayList<HashMap<String, String>>();
			if (studentTypeSession != null) {
				while (lastIndex != -1) {

					lastIndex = studentTypeSession.indexOf(">", lastIndex);

					if (lastIndex != -1) {
						count++;
						lastIndex += ">".length();
					}
				}

				for (int i = 0; i < count; i++) {
					String[] stringSplit = studentTypeSession.split(">");
					String studentType = stringSplit[i + 1];

					HashMap<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("splitString", studentType);
					studentSplitList.add(hashMap);
				}

				for (int i = 0; i < studentSplitList.size(); i++) {
					String[] stringSplit = studentSplitList.get(i)
							.get("splitString").split("<");
					String studentType = stringSplit[0];
					String studentId = stringSplit[1];

					HashMap<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("studentType", studentType);
					hashMap.put("studentId", studentId);
					studentTypeArrayList.add(hashMap);
				}
			}

			if (roleName.length() > 0) {
				if (roleName.equals("Guest")) {
					editTxtForStudentName.setVisibility(View.GONE);
					editTxtForStudentNumber.setVisibility(View.GONE);
					txtForStudentNo.setVisibility(View.GONE);
					layoutForDob.setVisibility(View.GONE);
					layoutForStudentType.setVisibility(View.GONE);
					layoutForStudentTypeFrame.setVisibility(View.GONE);
					textForStudentTypeLabel.setVisibility(View.GONE);
					layoutForHandicapAccess.setVisibility(View.VISIBLE);
				} else if (roleName.equals("Faculty")) {
					editTxtForStudentName.setVisibility(View.GONE);
					editTxtForStudentNumber.setVisibility(View.GONE);
					txtForStudentNo.setVisibility(View.GONE);
					layoutForDob.setVisibility(View.GONE);
					viewForTextViews.setVisibility(View.GONE);
					layoutForStudentType.setVisibility(View.GONE);
					layoutForStudentTypeFrame.setVisibility(View.GONE);
					textForStudentTypeLabel.setVisibility(View.GONE);
					layoutForHandicapAccess.setVisibility(View.GONE);
				} else if (roleName.equals("Graduate")) {
					editTxtForStudentName.setVisibility(View.GONE);
					editTxtForStudentNumber.setVisibility(View.GONE);
					txtForStudentNo.setVisibility(View.VISIBLE);
					layoutForDob.setVisibility(View.GONE);
					layoutForStudentType.setVisibility(View.VISIBLE);
					layoutForStudentTypeFrame.setVisibility(View.VISIBLE);
					textForStudentTypeLabel.setVisibility(View.VISIBLE);
					layoutForHandicapAccess.setVisibility(View.GONE);
				} else if (roleName.equals("Student")) {
					editTxtForStudentName.setVisibility(View.GONE);
					editTxtForStudentNumber.setVisibility(View.GONE);
					txtForStudentNo.setVisibility(View.GONE);
					layoutForDob.setVisibility(View.GONE);
					layoutForStudentType.setVisibility(View.VISIBLE);
					layoutForStudentTypeFrame.setVisibility(View.VISIBLE);
					textForStudentTypeLabel.setVisibility(View.VISIBLE);
					layoutForHandicapAccess.setVisibility(View.GONE);
				} else {
					editTxtForStudentName.setVisibility(View.GONE);
					editTxtForStudentNumber.setVisibility(View.GONE);
					txtForStudentNo.setVisibility(View.GONE);
					layoutForDob.setVisibility(View.GONE);
					layoutForStudentType.setVisibility(View.GONE);
					layoutForStudentTypeFrame.setVisibility(View.GONE);
					textForStudentTypeLabel.setVisibility(View.GONE);
					layoutForHandicapAccess.setVisibility(View.GONE);
				}
			}
			txtForMail.setText(email);
			txtForRole.setText(roleName);
			txtForSchool.setText(schoolName);

			editTxtFirstName.setText(firstName);
			editTxtLastName.setText(lastName);
			spinnerDob.setText(dob);
			editTxtMail.setText(email);
			editTxtForStudentName.setText(studentName);
			editTxtForStudentNumber.setText(studentNumber);

			//Notification status will be handled
			if (notifyStatus.length() > 0) {
				if (notifyStatus.equals("0")) {
					imageForPushOn.setVisibility(View.GONE);
					imageForPushOff.setVisibility(View.VISIBLE);
				} else {
					imageForPushOn.setVisibility(View.VISIBLE);
					imageForPushOff.setVisibility(View.GONE);
				}
			} else {
				imageForPushOn.setVisibility(View.VISIBLE);
				imageForPushOff.setVisibility(View.GONE);
			}

			//Handicaped Access status will be handled
			if (handicapStatus.length() > 0) {
				if (handicapStatus.equals("0")) {
					imgForHandicapOn.setVisibility(View.GONE);
					imgForHandicapOff.setVisibility(View.VISIBLE);
				} else {
					imgForHandicapOn.setVisibility(View.VISIBLE);
					imgForHandicapOff.setVisibility(View.GONE);
				}
			} else {
				imgForHandicapOn.setVisibility(View.VISIBLE);
				imgForHandicapOff.setVisibility(View.GONE);
			}

		}
	}

	public void Toast(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (h != null) {
			h.removeCallbacks(runnable);
		}
		Intent intentToHome = new Intent(SettingsActivity.this,
				HomeActivity.class);
		startActivity(intentToHome);
		finish();
		super.onBackPressed();
	}
}
