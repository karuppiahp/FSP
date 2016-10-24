package activities.mswift.info.walaapp.wala.signup;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.asyntasks.FbRegisterTask;
import activities.mswift.info.walaapp.wala.asyntasks.SignupStep1Task;
import activities.mswift.info.walaapp.wala.utils.Analitics;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.RootUtils;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 11/27/2015.
 */
public class SignUpActivity extends Activity implements View.OnClickListener {

    private Button btnForNextStep;
    public static EditText editTxtForEmail, editTxtForPhNo, editTxtForUsername, editTxtForPswd;
    private ProgressBar progressBar;

    // FaceBook
    private Session.StatusCallback statusCallback = new SessionStatusCallback();
    private List<String> permissions = new ArrayList<String>();
    private String deviceVersion, deviceId, deviceModel, deviceName, deviceManufacturer;
    boolean isJailBroken;
    private MixpanelAPI mixpanelAPI;
    private String countryCode = "UK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.sign_up);

        editTxtForEmail = (EditText) findViewById(R.id.editTxtForEmailAddress);
        editTxtForPhNo = (EditText) findViewById(R.id.editTxtForPhoneNo);
        editTxtForUsername = (EditText) findViewById(R.id.editTxtForUserName);
        editTxtForPswd = (EditText) findViewById(R.id.editTxtForPwd);
        btnForNextStep = (Button) findViewById(R.id.btnForNextStep);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnForNextStep.setOnClickListener(this);

        // mixpanel screen tracking
        mixpanelAPI = MixpanelAPI.getInstance(this, Constants.MIXPANEL_TOKEN);
        mixpanelAPI.track(getString(R.string.mixpanel_sign_up_screen_step1));

        //getting country code
        TelephonyManager tm = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        countryCode = tm.getNetworkCountryIso();

        if (countryCode != null && countryCode.length() != 2) {
            countryCode = this.getResources().getConfiguration().locale.getDefault().getCountry().toLowerCase();
            String countryDisplay = this.getResources().getConfiguration().locale.getDefault().getDisplayCountry().toLowerCase();
            Utils.ShowAlert(this, "The country " + countryDisplay + " has been selected in your device language. This country code has been taken as validation in your phone number entry, if it doesn't match please change your language according to your country.");
        }

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Constants.FB_BUTTON_CLICKS == true) {
            //    Session session = Session.getActiveSession();
            Session session = null;
            if (session != null && !session.isOpened()
                    && !session.isClosed()) {
                Session.OpenRequest request = new Session.OpenRequest(
                        this);
                request.setPermissions(permissions);
                request.setCallback(statusCallback);
                session.openForRead(request);
            } else {
                Session.openActiveSession(this, true,
                        statusCallback);
            }
        }

        if (Constants.EMAIL_ADDRESS.length() > 0) {
            editTxtForEmail.setText(Constants.EMAIL_ADDRESS);
        }

        if (Constants.PHONE_NUMBER.length() > 0) {
            editTxtForPhNo.setText(Constants.PHONE_NUMBER);
        }
        if (Constants.USER_NAME.length() > 0) {
            editTxtForUsername.setText(Constants.USER_NAME);
        }

        if (Constants.PASSWORD.length() > 0) {
            editTxtForPswd.setText(Constants.PASSWORD);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnForNextStep) {
            Utils.hideKeyboard(this);
            if (editTxtForEmail.getText().toString().length() > 0) {
                if (Utils.isEmailValid(editTxtForEmail.getText().toString())) {
                    if (editTxtForPhNo.getText().toString().length() > 0) {
                        if (Utils.isValidMobile(editTxtForPhNo.getText().toString(), countryCode)) {
                            if (editTxtForUsername.getText().toString().length() > 0) {
                                if (editTxtForPswd.getText().toString().length() > 0) {
                                    if (Utils.PasswordValidator(editTxtForPswd.getText().toString())) {

                                        Constants.EMAIL_ADDRESS = editTxtForEmail.getText().toString();
                                        Constants.USER_NAME = editTxtForUsername.getText().toString();
                                        Constants.PASSWORD = editTxtForPswd.getText().toString();
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("Email", editTxtForEmail.getText().toString());
                                        params.put("UserName", editTxtForUsername.getText().toString());
                                        params.put("PhoneNumber", Constants.PHONE_NUMBER);
                                        params.put("Password", editTxtForPswd.getText().toString());
                                        params.put("SocialProvider", "");
                                        params.put("SocialAccessToken", "");
                                        params.put("SocialSecretKey", "");

                                        //mixpanel tracking for next button
                                        JSONObject jsonObject = Analitics.signUpStep1(
                                                editTxtForEmail.getText().toString(), editTxtForPhNo.getText().toString(),
                                                editTxtForUsername.getText().toString(), editTxtForPswd.getText().toString());
                                        mixpanelAPI.track(getString(R.string.mixpanel_sign_up_screen_step1_next_button), jsonObject);

                                        if (Constants.FB_BUTTON_CLICKS == false) {
                                            new SignupStep1Task(this, params, progressBar);
                                        } else if (Constants.FB_BUTTON_CLICKS == true && SessionStores.getFbAccessToken(this) != null && SessionStores.getFbAccessToken(this).length() > 0) {
                                            Map<String, String> paramsFb = new HashMap<String, String>();
                                            paramsFb.put("Email", editTxtForEmail.getText().toString());
                                            paramsFb.put("UserName", editTxtForUsername.getText().toString());
                                            paramsFb.put("PhoneNumber", Constants.PHONE_NUMBER);
                                            paramsFb.put("Password", editTxtForPswd.getText().toString());
                                            paramsFb.put("SocialProvider", "FACEBOOK");
                                            paramsFb.put("SocialAccessToken", "" + SessionStores.getFbAccessToken(this));
                                            paramsFb.put("SocialSecretKey", "");

                                            new SignupStep1Task(this, paramsFb, progressBar);
                                        } else {
                                            Utils.ShowAlert(this, "Facebook token is not retrieved please go back and sign up with FB");
                                        }
                                    } else {
                                        editTxtForPswd.setError("Your password must be at least 6 characters long, using uppercase, lowercase, numbers and special characters (!, @, ?, -, +).");
                                    }
                                } else {
                                    Utils.ShowAlert(this, "Password field must not be empty");
                                }
                            } else {
                                Utils.ShowAlert(this, "User name field must not be empty");
                            }
                        } else {
                            Utils.ShowAlert(this, "Please enter a valid Phone number, the phone number must contain valid country code without (+) after that your phone number will be follows.");
                        }
                    } else {
                        Utils.ShowAlert(this, "Phone number field must not be empty");
                    }
                } else {
                    Utils.ShowAlert(this, "Please enter a valid Email address");
                }
            } else {
                Utils.ShowAlert(this, "Email address field must not be empty");
            }
        }
    }

    public class SessionStatusCallback implements Session.StatusCallback {

        @Override
        public void call(final Session session, SessionState state,
                         Exception exception) {

            Request request = new Request(session, "/me", null, HttpMethod.GET,
                    new Request.Callback() {

                        @Override
                        public void onCompleted(Response response) {
                            Constants.FB_ACCESS_TOKEN = session.getAccessToken();
                            SessionStores.saveFbAccessToken(session.getAccessToken(), SignUpActivity.this);

                            try {

                                JSONObject userDetails = response
                                        .getGraphObject().getInnerJSONObject();
                                Log.e("fb login json response",
                                        userDetails.toString());
                                Log.e("facebook user id=",
                                        userDetails.getString("id"));

                                if (userDetails.has("name")) {
                                    Log.e("facebook user name=",
                                            userDetails.getString("name"));
                                }

                                if (userDetails.has("email")) {
                                    Log.e("facebook user email=",
                                            userDetails.getString("email"));
                                }

                                if (userDetails.has("first_name")) {
                                    Log.e("facebook first_name=", userDetails.getString("first_name"));
                                }

                                if (userDetails.has("last_name")) {
                                    Log.e("facebook last_name=", userDetails.getString("last_name"));
                                }

                                //    apiCall();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    });
            request.executeAsync();
        }
    }

    public void apiCall() {
        // First json object for device info
        Map<String, String> params = new HashMap<String, String>();
        JSONObject jsonobject_one = new JSONObject();
        try {
            jsonobject_one.put("Os", "Android");
            jsonobject_one.put("OsVersion", deviceVersion);
            jsonobject_one.put("Id", deviceId);
            jsonobject_one.put("ProductModel", deviceModel);
            jsonobject_one.put("ProductName", deviceName);
            jsonobject_one.put("IsJailbroken", isJailBroken);
            jsonobject_one.put("ProductManufacturer", deviceManufacturer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, Object> paramsObj = new HashMap<String, Object>();
        paramsObj.put("DeviceInfo", jsonobject_one); //parse first json object t key DeviceInfo

        for (Map.Entry<String, Object> entry : paramsObj.entrySet()) {
            try {
                params.put(entry.getKey(), (String) entry.getValue());
                Log.e("params size?//", "" + params.size());
            } catch (ClassCastException cce) {
                cce.printStackTrace();
            }
        }

        //    params = (Map) paramsObj;

        params.put("PushID", "12345678935426565");
        params.put("SocialProvider", "FACEBOOK");
        params.put("SocialAccessToken", Constants.FB_ACCESS_TOKEN);

        new FbRegisterTask(SignUpActivity.this, params, progressBar);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Constants.FB_BUTTON_CLICKS == true) {
            Session.getActiveSession().addCallback(statusCallback);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Constants.FB_BUTTON_CLICKS == true) {
            Session.getActiveSession().removeCallback(statusCallback);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (Constants.FB_BUTTON_CLICKS == true) {
            Session session = Session.getActiveSession();
            Session.saveSession(session, outState);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.FB_BUTTON_CLICKS == true) {
            Session.getActiveSession().onActivityResult(this, requestCode,
                    resultCode, data);
        }
    }

}
