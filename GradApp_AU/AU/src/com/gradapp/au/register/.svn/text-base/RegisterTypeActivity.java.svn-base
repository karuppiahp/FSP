package com.gradapp.au.register;

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
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gradapp.au.activities.R;
import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.Constant;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;

public class RegisterTypeActivity extends Activity implements OnClickListener {

	Typeface typeFace;
	ImageButton btnForiconSlider, btnForHamberger, btnForBack, btnForSave;
	ArrayList<String> graduatetypes = new ArrayList<String>();
	ArrayList<String> pickSchool = new ArrayList<String>();
	Spinner graduateTypeSpinner, pickSchoolSpinner;
	boolean type, school;
	LayoutInflater mInflator;
	private String roleSpinner, schoolSpinner, roleName, schoolName, studentTypeSession = "";
	TextView textForHeader;
	ArrayList<HashMap<String, String>> userRoleArrayList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> schoolArrayList = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_type);

		typeFace = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Regular.otf");
		graduatetypes.add("Guest");
		graduatetypes.add("Graduate");

		btnForiconSlider = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnForHamberger = (ImageButton) findViewById(R.id.btnHamberger);
		btnForBack = (ImageButton) findViewById(R.id.backBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		graduateTypeSpinner = (Spinner) findViewById(R.id.spinnerForRegisterType);
		pickSchoolSpinner = (Spinner) findViewById(R.id.spinnerForPickSchool);
		btnForSave = (ImageButton) findViewById(R.id.imgBtnForSave);
		btnForiconSlider.setVisibility(View.GONE);
		btnForHamberger.setVisibility(View.GONE);
		btnForBack.setVisibility(View.GONE);
		btnForSave.setOnClickListener(this);
		textForHeader.setTypeface(typeFace);

		//Fetch the school list from api the asynctask class has been called
		new SchoolListGet().execute();
	}
	
	private void initUI() {
		type = false;
		graduateTypeSpinner.setAdapter(typeSpinnerAdapter); // Spinner for Grad types.
		pickSchoolSpinner.setAdapter(pickSchoolSpinnerAdapter); //Spinner for Schools list.
		graduateTypeSpinner.setOnItemSelectedListener(typeSelectedListener);
		pickSchoolSpinner.setOnItemSelectedListener(pickSchoolSelectedListener);
		graduateTypeSpinner.setOnTouchListener(typeSpinnerTouchListener);
		pickSchoolSpinner.setOnTouchListener(pickSchoolSpinnerTouchListener);
		mInflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
	}

	/* 
	 * Custom spinner adapter for Grad types the following method has been used.
	 * UserRole array list has been loaded in spinner items with custom text.
	 */
	private SpinnerAdapter typeSpinnerAdapter = new BaseAdapter() {

		private TextView text;

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflator.inflate(R.layout.custom_register_spinner, null);
			}
			text = (TextView) convertView.findViewById(R.id.textForSpinnerItem);
			text.setTypeface(typeFace);
			if (!type) {
				text.setText("Guest/Graduate");
			} else {
				text.setText(userRoleArrayList.get(position).get("roleName"));
			}
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return userRoleArrayList.get(position).get("roleName");
		}

		@Override
		public int getCount() {
			return userRoleArrayList.size();
		}

		@SuppressLint("InflateParams")
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflator.inflate(R.layout.custom_register_spinner_text,
						null);
			}
			text = (TextView) convertView
					.findViewById(R.id.textForSpinnerItems);
			text.setText(userRoleArrayList.get(position).get("roleName"));
			return convertView;
		};
	};

	/* 
	 * Custom spinner adapter for Schools list the following method has been used.
	 * School array list has been loaded in spinner items with custom text.
	 */
	private SpinnerAdapter pickSchoolSpinnerAdapter = new BaseAdapter() {

		private TextView text;

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflator.inflate(R.layout.custom_register_spinner, null);
			}
			text = (TextView) convertView.findViewById(R.id.textForSpinnerItem);
			text.setTypeface(typeFace);
			if (!school) {
				text.setText("Pick a School");
			} else {
				text.setText(schoolArrayList.get(position).get("schoolName"));
			}
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return schoolArrayList.get(position).get("schoolName");
		}

		@Override
		public int getCount() {
			return schoolArrayList.size();
		}

		@SuppressLint("InflateParams")
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflator.inflate(R.layout.custom_register_spinner_text,
						null);
			}
			text = (TextView) convertView
					.findViewById(R.id.textForSpinnerItems);
			text.setText(schoolArrayList.get(position).get("schoolName"));
			return convertView;
		};
	};

	/* 
	 * OnItemClick for Grad types the roleId and roleName has been get.
	 * RoleId has to be send to api as parameter
	 * RoleName is to display the name in spinner box.
	 */
	private OnItemSelectedListener typeSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			roleSpinner = userRoleArrayList.get(position).get("roleId");
			roleName = userRoleArrayList.get(position).get("roleName");

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};

	/* 
	 * OnItemClick for Schools List the schoolId and schollName has been get.
	 * schoolId has to be send to api as parameter
	 * schollName is to display the name in spinner box.
	 */
	private OnItemSelectedListener pickSchoolSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			schoolSpinner = schoolArrayList.get(position).get("schoolId");
			schoolName = schoolArrayList.get(position).get("schoolName");

		}

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};

	/* 
	 * Return boolean types to check whether the Grad Types item is selected or not.
	 */
	private OnTouchListener typeSpinnerTouchListener = new OnTouchListener() {

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (userRoleArrayList.size() > 0) {
				type = true;
			} else {
				type = false;
			}
			((BaseAdapter) typeSpinnerAdapter).notifyDataSetChanged();
			return false;
		}
	};

	/* 
	 * Return boolean types to check whether the Schools item is selected or not.
	 */
	private OnTouchListener pickSchoolSpinnerTouchListener = new OnTouchListener() {

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			school = true;
			((BaseAdapter) pickSchoolSpinnerAdapter).notifyDataSetChanged();
			return false;
		}
	};

	/*
	 * In Session the roleId, roleName, schoolId and schoolName has been maintained.
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.imgBtnForSave) {
			if (type == false) {
				Toast("Please select register type");
			} else {
				if (school == false) {
					Toast("Please pick a school to register");
				} else {
					SessionStores.saveRoleType(roleSpinner,
							RegisterTypeActivity.this);
					SessionStores.saveRoleName(roleName,
							RegisterTypeActivity.this);
					SessionStores.saveSchoolId(schoolSpinner,
							RegisterTypeActivity.this);
					SessionStores.saveSchoolName(schoolName,
							RegisterTypeActivity.this);
					Intent intentToRegister2 = new Intent(
							RegisterTypeActivity.this,
							RegisterActivity.class);
					startActivity(intentToRegister2);
					finish();
				}
			}
		}
	}

	public class SchoolListGet extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog;
		String status = "", error;
		JSONObject jsonObj;

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(RegisterTypeActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}
		
		/*
		 * Access api from XMl the UrlGenerator class been used and passed the url to ServerResponse class constructor.
		 * Here the University name has been passed as params in POST method. 
		 * UserRole and Schools list has been maintained in arraylist to display in spinner boxes.
		 */
		@Override
		protected Boolean doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("name",
					"Arizona University"));
			JSONObject jsonObj = new ServerResponse(UrlGenerator.getUserRole())
					.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
			try {
				if (jsonObj != null) {
					Constant.gradTypeArrayList.clear();
					String status = jsonObj.getString("status");
					String message = jsonObj.getString("msg");
					if (status.equals("1")) {
						String univId = jsonObj.getString("university_id");
						SessionStores.saveUnivId(univId,
								RegisterTypeActivity.this);
						JSONArray userRoleArray = jsonObj
								.getJSONArray("user_details");
						for (int i = 0; i < userRoleArray.length(); i++) {
							String roleId = userRoleArray.getJSONObject(i)
									.getString("id");
							String roleName = userRoleArray.getJSONObject(i)
									.getString("name");

							HashMap<String, String> hashValue = new HashMap<String, String>();
							hashValue.put("roleId", roleId);
							hashValue.put("roleName", roleName);
							userRoleArrayList.add(hashValue);
						}

						JSONArray schoolArray = jsonObj
								.getJSONArray("school_details");
						for (int i = 0; i < schoolArray.length(); i++) {
							String schoolId = schoolArray.getJSONObject(i)
									.getString("id");
							String schoolName = schoolArray.getJSONObject(i)
									.getString("name");

							HashMap<String, String> hashValue = new HashMap<String, String>();
							hashValue.put("schoolId", schoolId);
							hashValue.put("schoolName", schoolName);
							schoolArrayList.add(hashValue);
						}
						
						JSONArray gradTypeArray = jsonObj
								.getJSONArray("grad_type_details");
						for (int i = 0; i < gradTypeArray.length(); i++) {
							String gradId = gradTypeArray.getJSONObject(i)
									.getString("id");
							String gradType = gradTypeArray.getJSONObject(i)
									.getString("type");
							
							studentTypeSession = studentTypeSession + ">" + gradType + "<" + gradId;
							SessionStores.saveStudentType(studentTypeSession, RegisterTypeActivity.this);

							HashMap<String, String> hashValue = new HashMap<String, String>();
							hashValue.put("gradId", gradId);
							hashValue.put("gradType", gradType);
							Constant.gradTypeArrayList.add(hashValue); //this arraylist has been maintained in Constant because to access its values in other classes.
						}
					} else {
						Toast(message);
					}
				} else {
					Toast("Network speed is slow");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			initUI(); //After successfully fetched the UI has been initialized.
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
}
