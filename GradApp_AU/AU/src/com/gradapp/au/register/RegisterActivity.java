package com.gradapp.au.register;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gradapp.au.AsyncTasks.RegisterTask;
import com.gradapp.au.activities.R;
import com.gradapp.au.utils.Constant;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.Utils;

public class RegisterActivity extends Activity implements OnClickListener {

	Typeface typeFace;
	ImageView imageForHandicapOn, imageForHandicapOff;
	ImageButton btnForiconSlider, btnForHamberger, btnForBack, btnForSave;
	TextView textForHeader, txtForTerms, txtForPolicy, textForBehaviour;
	FrameLayout dobFrame, genderFrame, gradTypeFrame;
	ArrayList<String> genderArray = new ArrayList<String>();
	Spinner genderSpinner, gradTypeSpinner;
	EditText dobSpinner, editTxtForFirstName, editTxtForLastName,
			editTextForEmail, editTxtForStudentName, editTxtForStudentNumber,
			editTxtForNoOfGuest;
	boolean gender, gradType;
	LayoutInflater mInflator;
	static final int DATE_PICKER_ID = 1111;
	String status = "0", studentName = "", userRole, gradTypeId;
	RelativeLayout layoutForHandicapAccess;
	InputFilter filtertxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);

		typeFace = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Regular.otf");
		genderArray.add("Male");
		genderArray.add("Female");

		btnForiconSlider = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnForHamberger = (ImageButton) findViewById(R.id.btnHamberger);
		btnForBack = (ImageButton) findViewById(R.id.backBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		btnForSave = (ImageButton) findViewById(R.id.imgBtnForRegister);
		editTxtForFirstName = (EditText) findViewById(R.id.editTxtForFirstName);
		editTxtForLastName = (EditText) findViewById(R.id.editTxtForLastName);
		dobSpinner = (EditText) findViewById(R.id.spinnerForDob);
		genderSpinner = (Spinner) findViewById(R.id.spinnerForGender);
		editTextForEmail = (EditText) findViewById(R.id.editTxtForEmail);
		imageForHandicapOn = (ImageView) findViewById(R.id.imageForHandicapOn);
		imageForHandicapOff = (ImageView) findViewById(R.id.imageForHandicapOff);
		editTxtForStudentName = (EditText) findViewById(R.id.editTxtForStudentName);
		editTxtForStudentNumber = (EditText) findViewById(R.id.editTxtForStudentNo);
		dobFrame = (FrameLayout) findViewById(R.id.layoutForDob);
		genderFrame = (FrameLayout) findViewById(R.id.layoutForGender);
		gradTypeSpinner = (Spinner) findViewById(R.id.spinnerForStudentType);
		editTxtForNoOfGuest = (EditText) findViewById(R.id.editTxtForNoOfGuest);
		gradTypeFrame = (FrameLayout) findViewById(R.id.layoutForStudentType);
		txtForTerms = (TextView) findViewById(R.id.textForTandC);
		txtForPolicy = (TextView) findViewById(R.id.textForPrivacyPolicy);
		textForBehaviour = (TextView) findViewById(R.id.textForAnd);
		layoutForHandicapAccess = (RelativeLayout) findViewById(R.id.layoutForHandicapAccess);
		btnForiconSlider.setVisibility(View.GONE);
		btnForHamberger.setVisibility(View.GONE);
		btnForBack.setVisibility(View.VISIBLE);
		btnForSave.setOnClickListener(this);
		btnForBack.setOnClickListener(this);
		dobSpinner.setOnClickListener(this);
		imageForHandicapOn.setOnClickListener(this);
		imageForHandicapOff.setOnClickListener(this);
		txtForTerms.setOnClickListener(this);
		txtForPolicy.setOnClickListener(this);
		textForBehaviour.setOnClickListener(this);
		textForHeader.setTypeface(typeFace);
		imageForHandicapOn.setVisibility(View.GONE);
		imageForHandicapOff.setVisibility(View.VISIBLE);

		userRole = SessionStores.getRoleName(this);
		
		// According to the user role matches the edittext boxes has been displayed.
		if (userRole.equals("Guest")) {
			editTxtForStudentName.setVisibility(View.GONE);
			editTxtForStudentNumber.setVisibility(View.GONE);
			editTxtForNoOfGuest.setVisibility(View.GONE);
			gradTypeFrame.setVisibility(View.GONE);
			dobFrame.setVisibility(View.GONE);
			genderFrame.setVisibility(View.GONE);
		} else if (userRole.equals("Faculty")) {
			editTxtForStudentName.setVisibility(View.GONE);
			editTxtForStudentNumber.setVisibility(View.GONE);
			editTxtForNoOfGuest.setVisibility(View.GONE);
			layoutForHandicapAccess.setVisibility(View.GONE);
			gradTypeFrame.setVisibility(View.GONE);
			dobFrame.setVisibility(View.GONE);
			genderFrame.setVisibility(View.GONE);
		} else if (userRole.equals("Graduate")) {
			initUI();
			editTxtForStudentName.setVisibility(View.GONE);
			editTxtForStudentNumber.setVisibility(View.GONE);
			editTxtForNoOfGuest.setVisibility(View.GONE);
			layoutForHandicapAccess.setVisibility(View.GONE);
			gradTypeFrame.setVisibility(View.VISIBLE);
			dobFrame.setVisibility(View.GONE);
			genderFrame.setVisibility(View.GONE);

		} else if (userRole.equals("Student")) {
			initUI();
			editTxtForStudentName.setVisibility(View.GONE);
			editTxtForStudentNumber.setVisibility(View.GONE);
			editTxtForNoOfGuest.setVisibility(View.GONE);
			layoutForHandicapAccess.setVisibility(View.GONE);
			gradTypeFrame.setVisibility(View.VISIBLE);
			dobFrame.setVisibility(View.GONE);
			genderFrame.setVisibility(View.GONE);
		} else {
			editTxtForStudentName.setVisibility(View.GONE);
			editTxtForStudentNumber.setVisibility(View.GONE);
			editTxtForNoOfGuest.setVisibility(View.GONE);
			gradTypeFrame.setVisibility(View.GONE);
			layoutForHandicapAccess.setVisibility(View.GONE);
			dobFrame.setVisibility(View.GONE);
			genderFrame.setVisibility(View.GONE);
		}

		//Used to check whether the edittext have empty space in between text
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
		gradType = false;
		gradTypeSpinner.setAdapter(gradTypeSpinnerAdapter);
		gradTypeSpinner.setOnItemSelectedListener(gradTypeSelectedListener);
		gradTypeSpinner.setOnTouchListener(gradTypeSpinnerTouchListener);
		mInflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
	}

	/*
	 * Custom spinner for Graduate types.
	 */
	private SpinnerAdapter gradTypeSpinnerAdapter = new BaseAdapter() {

		private TextView text;

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflator.inflate(
						R.layout.custom_register_spinner, null);
			}
			text = (TextView) convertView.findViewById(R.id.textForSpinnerItem);
			text.setTypeface(typeFace);
			if (!gradType) {
				text.setText("Student Type");
				// text.setTextColor(Color.parseColor("#a39ea1"));
				text.setTextColor(Color.parseColor("#FFFFFF"));
			} else {
				text.setText(Constant.gradTypeArrayList.get(position).get(
						"gradType"));
				text.setTextColor(Color.parseColor("#FFFFFF"));
			}
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return Constant.gradTypeArrayList.get(position).get("gradType");
		}

		@Override
		public int getCount() {
			return Constant.gradTypeArrayList.size();
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
				convertView = mInflator.inflate(
						R.layout.custom_register_spinner_text, null);
			}
			text = (TextView) convertView
					.findViewById(R.id.textForSpinnerItems);
			text.setText(Constant.gradTypeArrayList.get(position).get(
					"gradType"));
			return convertView;
		};
	};

	/*
	 * Custom spinner OnItemSelectedListener to get graduate id.
	 */
	private OnItemSelectedListener gradTypeSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			gradTypeId = Constant.gradTypeArrayList.get(position).get("gradId");
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};

	/*
	 * Custom spinner OnTouchListener to check whether the item is selected or not.
	 */
	@SuppressLint("ClickableViewAccessibility")
	private OnTouchListener gradTypeSpinnerTouchListener = new OnTouchListener() {

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (Constant.gradTypeArrayList.size() > 0) {
				gradType = true;
			} else {
				gradType = false;
			}
			((BaseAdapter) gradTypeSpinnerAdapter).notifyDataSetChanged();
			return false;
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backBtn) {
			Intent intentBack = new Intent(RegisterActivity.this,
					RegisterTypeActivity.class);
			startActivity(intentBack);
			finish();
		}

		/*
		 * RegisterTask AsyncTask has been called once the register button is clicked with appropriate edittext fields are filled.
		 */
		if (v.getId() == R.id.imgBtnForRegister) {
			if (userRole.equals("Guest")) {
				if (!(editTxtForFirstName.getText().toString().equals(""))) {
					if (!(editTxtForFirstName.getText().toString()
							.contains(" "))) {
						if (!(editTxtForLastName.getText().toString()
								.equals(""))) {
							if (!(editTxtForLastName.getText().toString()
									.contains(" "))) {
								if (!(editTextForEmail.getText().toString()
										.equals(""))) {
									if (Utils.validEmail(editTextForEmail
											.getText().toString())) {
										String univId = SessionStores
												.getUnivId(RegisterActivity.this);
										String roleId = SessionStores
												.getRoleType(RegisterActivity.this);
										String schoolId = SessionStores
												.getSchoolId(RegisterActivity.this);
										new RegisterTask(RegisterActivity.this,
												editTxtForFirstName.getText()
														.toString(),
												editTxtForLastName.getText()
														.toString(), "", "",
												editTextForEmail.getText()
														.toString(), univId,
												roleId, schoolId, status,
												editTxtForStudentName.getText()
														.toString(), "", "", "")
												.execute();
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
			} else if (userRole.equals("Faculty")) {
				if (!(editTxtForFirstName.getText().toString().equals(""))) {
					if (!(editTxtForFirstName.getText().toString()
							.contains(" "))) {
						if (!(editTxtForLastName.getText().toString()
								.equals(""))) {
							if (!(editTxtForLastName.getText().toString()
									.contains(" "))) {
								if (!(editTextForEmail.getText().toString()
										.equals(""))) {
									if (Utils.validEmail(editTextForEmail
											.getText().toString())) {
										String univId = SessionStores
												.getUnivId(RegisterActivity.this);
										String roleId = SessionStores
												.getRoleType(RegisterActivity.this);
										String schoolId = SessionStores
												.getSchoolId(RegisterActivity.this);
										new RegisterTask(RegisterActivity.this,
												editTxtForFirstName.getText()
														.toString(),
												editTxtForLastName.getText()
														.toString(), "", "",
												editTextForEmail.getText()
														.toString(), univId,
												roleId, schoolId, status, "",
												"", "", "").execute();
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
			} else if (userRole.equals("Graduate")) {
				if (!(editTxtForFirstName.getText().toString().equals(""))) {
					if (!(editTxtForFirstName.getText().toString()
							.contains(" "))) {
						if (!(editTxtForLastName.getText().toString()
								.equals(""))) {
							if (!(editTxtForLastName.getText().toString()
									.contains(" "))) {
								if (!(gradType == false)) {
									if (!(editTextForEmail.getText().toString()
											.equals(""))) {
										if (Utils.validEmail(editTextForEmail
												.getText().toString())) {
											String univId = SessionStores
													.getUnivId(RegisterActivity.this);
											String roleId = SessionStores
													.getRoleType(RegisterActivity.this);
											String schoolId = SessionStores
													.getSchoolId(RegisterActivity.this);
											new RegisterTask(
													RegisterActivity.this,
													editTxtForFirstName
															.getText()
															.toString(),
													editTxtForLastName
															.getText()
															.toString(),
													dobSpinner.getText()
															.toString(), "",
													editTextForEmail.getText()
															.toString(),
													univId, roleId, schoolId,
													"", "",
													editTxtForStudentNumber
															.getText()
															.toString(),
													gradTypeId,
													editTxtForNoOfGuest
															.getText()
															.toString())
													.execute();
										} else {
											Toast("Please enter valid Email");
										}
									} else {
										Toast("Email field must not be empty");
									}
								} else {
									Toast("Please select Student type");
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
			} else if (userRole.equals("Student")) {
				if (!(editTxtForFirstName.getText().toString().equals(""))) {
					if (!(editTxtForFirstName.getText().toString()
							.contains(" "))) {
						if (!(editTxtForLastName.getText().toString()
								.equals(""))) {
							if (!(editTxtForLastName.getText().toString()
									.contains(" "))) {
								if (!(gradType == false)) {
									if (!(editTextForEmail.getText().toString()
											.equals(""))) {
										if (Utils.validEmail(editTextForEmail
												.getText().toString())) {
											String univId = SessionStores
													.getUnivId(RegisterActivity.this);
											String roleId = SessionStores
													.getRoleType(RegisterActivity.this);
											String schoolId = SessionStores
													.getSchoolId(RegisterActivity.this);
											new RegisterTask(
													RegisterActivity.this,
													editTxtForFirstName
															.getText()
															.toString(),
													editTxtForLastName
															.getText()
															.toString(),
													dobSpinner.getText()
															.toString(), "",
													editTextForEmail.getText()
															.toString(),
													univId, roleId, schoolId,
													"", "",
													editTxtForStudentNumber
															.getText()
															.toString(),
													gradTypeId,
													editTxtForNoOfGuest
															.getText()
															.toString())
													.execute();
										} else {
											Toast("Please enter valid Email");
										}
									} else {
										Toast("Email field must not be empty");
									}
								} else {
									Toast("Please select Student type");
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
			} else {
				if (!(editTxtForFirstName.getText().toString().equals(""))) {
					if (!(editTxtForFirstName.getText().toString()
							.contains(" "))) {
						if (!(editTxtForLastName.getText().toString()
								.equals(""))) {
							if (!(editTxtForLastName.getText().toString()
									.contains(" "))) {
								if (!(editTextForEmail.getText().toString()
										.equals(""))) {
									if (Utils.validEmail(editTextForEmail
											.getText().toString())) {
										String univId = SessionStores
												.getUnivId(RegisterActivity.this);
										String roleId = SessionStores
												.getRoleType(RegisterActivity.this);
										String schoolId = SessionStores
												.getSchoolId(RegisterActivity.this);
										new RegisterTask(RegisterActivity.this,
												editTxtForFirstName.getText()
														.toString(),
												editTxtForLastName.getText()
														.toString(), "", "",
												editTextForEmail.getText()
														.toString(), univId,
												roleId, schoolId, status, "",
												"", "", "").execute();
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
		}

		if (v.getId() == R.id.textForTandC) {
			CopyReadAssets("GradAPP _Terms_of_Service.pdf");
		}

		if (v.getId() == R.id.textForPrivacyPolicy) {
			CopyReadAssets("GradAPP_Privacy_Policy.pdf");
		}

		if (v.getId() == R.id.textForAnd) {
			CopyReadAssets("GradApp_Behaviour_Policy.pdf");
		}

		if (v.getId() == R.id.imageForHandicapOn) {
			status = "0";
			imageForHandicapOn.setVisibility(View.GONE);
			imageForHandicapOff.setVisibility(View.VISIBLE);
		}

		if (v.getId() == R.id.imageForHandicapOff) {
			status = "1";
			imageForHandicapOn.setVisibility(View.VISIBLE);
			imageForHandicapOff.setVisibility(View.GONE);
		}
	}

	/*
	 * Following method is used to copy and display the PDF files using ACTION_VIEW.
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("WorldReadableFiles")
	private void CopyReadAssets(String pdfFile) {
		AssetManager assetManager = getAssets();

		InputStream in = null;
		OutputStream out = null;
		File file = new File(getFilesDir(), pdfFile);
		try {
			in = assetManager.open(pdfFile);
			out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

			copyFile(in, out);
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(
				Uri.parse("file://" + getFilesDir() + "/" + pdfFile),
				"application/pdf");

		startActivity(intent);
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
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

	public void onBackPressed() {
		super.onBackPressed();
		Intent intentBack = new Intent(RegisterActivity.this,
				RegisterTypeActivity.class);
		startActivity(intentBack);
		finish();
	}
}
