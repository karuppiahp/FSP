package activities.mswift.info.walaapp.wala.signup;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.adapter.SpinnerListAdapter;
import activities.mswift.info.walaapp.wala.asyntasks.SignUpTask;
import activities.mswift.info.walaapp.wala.utils.Analitics;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.RootUtils;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 11/30/2015.
 */
public class SignUpStep2Activity extends Activity implements View.OnClickListener {

    private Button btnForFinish, btnForSave, btnForPolicyGotIt;
    public static EditText editTxtForFirstName, editTxtForMiddleName, editTxtForLastName, editTextForCity, editTxtForOTP, txtForCountry;
    private TextView txtForDob, privacyPolicy, privacyPolicyCntn;
    private ListView listView;
    private RelativeLayout layForCountry, layForDob, layForPolicyOverlay, bottomLayout;
    private LinearLayout layForPrivacy;
    private Calendar cal;
    private int day, month, year, maxYear;
    static final int CAL_DIALOG_ID = 999;
    private String spinnerClicks;
    private String deviceVersion, deviceId, deviceModel, deviceName, deviceManufacturer;
    boolean isJailBroken;
    private ProgressBar progressBar;
    private CheckBox privacycheckicon;
    private MixpanelAPI mixpanelAPI;
    private String privacyOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.sign_up_2);


        editTxtForFirstName = (EditText) findViewById(R.id.editTxtForFirstName);
        editTxtForMiddleName = (EditText) findViewById(R.id.editTxtForMiddleName);
        editTxtForLastName = (EditText) findViewById(R.id.editTxtForLastName);
        editTextForCity = (EditText) findViewById(R.id.editTxtForCity);
        txtForCountry = (EditText) findViewById(R.id.txtForCountrySpinner);
        txtForDob = (TextView) findViewById(R.id.txtForDobSelected);
        layForCountry = (RelativeLayout) findViewById(R.id.layForCountrySpinner);
        listView = (ListView) findViewById(R.id.listViewForSpinner);
        layForPrivacy = (LinearLayout) findViewById(R.id.layForPrivacy);
        privacycheckicon = (CheckBox) findViewById(R.id.privacycheckicon);
        privacyPolicy = (TextView) findViewById(R.id.privacyPolicy);
        btnForFinish = (Button) findViewById(R.id.btnForFinish);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        layForPolicyOverlay = (RelativeLayout) findViewById(R.id.layForPolicyOverlay);
        bottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);
        btnForPolicyGotIt = (Button) findViewById(R.id.btnForPolicyGotIt);
        privacyPolicyCntn = (TextView) findViewById(R.id.privacyPolicyCntn);

        int density = getResources().getDisplayMetrics().densityDpi;
        if (density == DisplayMetrics.DENSITY_HIGH) {
            privacyPolicy.setTextSize(12);
            privacyPolicy.setSingleLine();
            privacyPolicyCntn.setTextSize(12);
            btnForPolicyGotIt.setWidth(100);
            btnForPolicyGotIt.setHeight(40);
            btnForPolicyGotIt.setTextSize(12);

        }

        privacyOverlay = getString(R.string.privacy_policy);
        Spanned htmlAsPrivacy = Html.fromHtml(privacyOverlay);
        TextView privacyTxt = (TextView) findViewById(R.id.privacypolicySet);
        privacyTxt.setText(htmlAsPrivacy);

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH); //Current day
        month = cal.get(Calendar.MONTH);// current month
        year = cal.get(Calendar.YEAR) - 13;// current year

        DatePicker datePicker = new DatePicker(this);
        datePicker.setMaxDate(new Date().getTime());

        // mixpanel screen tracking
        mixpanelAPI = MixpanelAPI.getInstance(this, Constants.MIXPANEL_TOKEN);
        mixpanelAPI.track(getString(R.string.mixpanel_sign_up_screen_step2));

        /**
         * Device deatails are fetched
         */
        try {
            deviceVersion = "" + Build.VERSION.RELEASE;
            deviceId = "" + Build.ID;
            if (deviceId.contains(".")) {
                deviceId.replace(".", "");
            }
            deviceModel = "" + Build.MODEL;
            deviceName = "" + Build.BRAND;
            deviceManufacturer = "" + Build.MANUFACTURER;
            isJailBroken = RootUtils.isDeviceRooted();
            Log.i("DeviceInfo", "" + deviceVersion + "\n" + deviceId + "\n" + deviceModel + "\n" + deviceName + "\n" + deviceManufacturer + "\n" + isJailBroken);
        } catch (Exception e) {

        }

        if (!(Constants.GENDER_ARRAY.size() > 0))
            Constants.GENDER_ARRAY = Utils.getGenderValues(this);

        if (Utils.isOnline()) {
            if (!(Constants.COUNTRY_ARRAY.size() > 0))
                Utils.getCountry();
        } else {
            Utils.ShowAlert(this, "Network connectivity is not available");
        }

        if (Constants.FIRST_NAME.length() > 0) {
            editTxtForFirstName.setText(Constants.FIRST_NAME);
        }

        if (Constants.MIDDLE_NAME.length() > 0) {
            editTxtForMiddleName.setText(Constants.MIDDLE_NAME);
        }

        if (Constants.LAST_NAME.length() > 0) {
            editTxtForLastName.setText(Constants.LAST_NAME);
        }

        if (Constants.COUNTRY.length() > 0) {
            txtForCountry.setText(Constants.COUNTRY);
        }

        if (Constants.CITY.length() > 0) {
            editTextForCity.setText(Constants.CITY);
        }

        if (Constants.DOB.length() > 0) {
            txtForDob.setText(Constants.DOB);
        }

        //Listview for country list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listView.setVisibility(View.GONE);
                if (spinnerClicks.equals("country")) {
                    txtForCountry.setText(Constants.COUNTRY_ARRAY.get(i).getName());
                    Constants.COUNTRY = Constants.COUNTRY_ARRAY.get(i).getName();
                    Constants.COUNTRY_ID = Constants.COUNTRY_ARRAY.get(i).getId();

                    Map<String, String> countries = new HashMap<>();
                    for (String iso : Locale.getISOCountries()) {
                        Locale li = new Locale("", iso);
                        countries.put(li.getDisplayCountry(), iso);
                    }

                    SessionStores.saveCurrencyCode(countries.get(Constants.COUNTRY), SignUpStep2Activity.this);
                    Constants.COUNTRY_CODE = Constants.COUNTRY_ARRAY.get(i).getCountryCode();
                }
            }
        });

        txtForCountry.setFocusable(false);
        txtForCountry.setOnClickListener(this);
        txtForDob.setOnClickListener(this);
        btnForFinish.setOnClickListener(this);
        privacyPolicy.setOnClickListener(this);
        btnForPolicyGotIt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnForFinish) {
            Utils.hideKeyboard(this);
            if (txtForCountry.getText().toString().length() > 0) {
                if (editTextForCity.getText().toString().length() > 0) {
                    if (txtForDob.getText().toString().length() > 0) {
                        if (privacycheckicon.isChecked()) {
                            Constants.FIRST_NAME = editTxtForFirstName.getText().toString();
                            Constants.LAST_NAME = editTxtForLastName.getText().toString();
                            Constants.CITY = editTextForCity.getText().toString();
                            /**
                             * Send request body in json format with multiple json objects
                             */
                            // First json object for device info
                            Map<String, String> params = new HashMap<String, String>();
                            JSONObject jsonobjectMain = new JSONObject();
                            JSONObject jsonobject_one = new JSONObject();
                            try {
                                jsonobject_one.put("Os", "Android");
                                jsonobject_one.put("OsVersion", deviceVersion);
                                jsonobject_one.put("Id", deviceId);
                                jsonobject_one.put("ProductModel", deviceModel);
                                jsonobject_one.put("ProductName", deviceName);
                                jsonobject_one.put("IsJailbroken", isJailBroken);
                                jsonobject_one.put("ProductManufacturer", deviceManufacturer);
                                jsonobjectMain.put("DeviceInfo", jsonobject_one);
                                jsonobjectMain.put("FirstName", Constants.FIRST_NAME);
                                jsonobjectMain.put("MiddleName", editTxtForMiddleName.getText().toString());
                                jsonobjectMain.put("LastName", Constants.LAST_NAME);
                                jsonobjectMain.put("DateOfBirth", Constants.DOB);
                                jsonobjectMain.put("Gender", "Male");
                                jsonobjectMain.put("DisplayName", Constants.FIRST_NAME);
                                jsonobjectMain.put("Email", Constants.EMAIL_ADDRESS);
                                jsonobjectMain.put("PushID", "12345678935426565");
                                jsonobjectMain.put("PhoneNumber", Constants.PHONE_NUMBER);
                                jsonobjectMain.put("Password", Constants.PASSWORD);
                                jsonobjectMain.put("USerName", Constants.USER_NAME);
                                jsonobjectMain.put("CountryCode", Constants.COUNTRY_CODE);
                                jsonobjectMain.put("City", Constants.CITY);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //mixpanel finish button
                            JSONObject jsonObject = Analitics.signUpFinishButton(
                                    Constants.FIRST_NAME, editTxtForMiddleName.getText().toString(),
                                    Constants.LAST_NAME, txtForCountry.getText().toString(), editTextForCity.getText().toString(), Constants.DOB);
                            mixpanelAPI.track(getString(R.string.mixpanel_sign_up_screen_step2_finish_button), jsonObject);
                            /**
                             * Signup task executes
                             */
                            new SignUpTask(this, jsonobjectMain, progressBar);
                        } else {
                            Utils.ShowAlert(this, "Please agree to Wala Privacy Policy");

                        }
                    } else {
                        Utils.ShowAlert(this, "Please enter your date of birth");
                    }
                } else {
                    Utils.ShowAlert(this, "Please enter your city");
                }
            } else {
                Utils.ShowAlert(this, "Please select your country");
            }

        }

        if (v.getId() == R.id.txtForCountrySpinner) {
            Utils.hideKeyboard(this);
            spinnerClicks = "country";
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(new SpinnerListAdapter(this, spinnerClicks));
        }

        if (v.getId() == R.id.txtForDobSelected) {
            Utils.hideKeyboard(this);
            showDialog(CAL_DIALOG_ID); //Date picker dialog calls
        }

        if (v.getId() == R.id.privacyPolicy) {
            Utils.hideKeyboard(this);
            layForPolicyOverlay.setVisibility(View.VISIBLE);
        }

        if (v.getId() == R.id.btnForPolicyGotIt) {
            layForPolicyOverlay.setVisibility(View.GONE);
        }

    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String formattedDay = (String.valueOf(selectedDay));
            String formattedMonth = (String.valueOf(selectedMonth + 1));
            if (selectedDay < 10) {
                formattedDay = "0" + selectedDay;
            }
            if (Integer.parseInt(formattedMonth) < 10) {
                formattedMonth = "0" + (formattedMonth);
            }
            Constants.DOB = formattedDay + "/" + formattedMonth
                    + "/" + selectedYear;
            txtForDob.setText(formattedDay + "/" + formattedMonth
                    + "/" + selectedYear);
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case CAL_DIALOG_ID:

                // set date picker as current date
                DatePickerDialog dateDialog = new DatePickerDialog(this, datePickerListener, year, month,
                        day) {
                    @Override
                    public void onDateChanged(DatePicker view, int mYear, int monthOfYear, int dayOfMonth) {
                        //    view.setMaxDate(new Date().getTime());
                        if (mYear > year)
                            view.updateDate(year, month, day);

                        if (monthOfYear > month && mYear == year)
                            view.updateDate(year, month, day);

                        if (dayOfMonth > day && mYear == year && monthOfYear == month)
                            view.updateDate(year, month, day);

                    }
                };
                Calendar c = Calendar.getInstance();
                c.set(year, month, day);
                dateDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                return dateDialog;
        }
        return null;
    }
}
