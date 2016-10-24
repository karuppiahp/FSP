package activities.mswift.info.walaapp.wala.signup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import activities.mswift.info.walaapp.wala.asyntasks.FbLoginTask;
import activities.mswift.info.walaapp.wala.asyntasks.LoginRecoverTask;
import activities.mswift.info.walaapp.wala.asyntasks.LoginTask;
import activities.mswift.info.walaapp.wala.asyntasks.RenewingTokenTask;
import activities.mswift.info.walaapp.wala.support.GoalsGenerateService;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.RootUtils;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 12/16/2015.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText editTxtForEmail, editTxtForPwd;
    private Button btnForNext;
    private ProgressBar progressBar;
    private TextView signUpTxt;
    private RelativeLayout layForSignUpTxt;
    private String installationId;
    private RelativeLayout btnForFb;
    MixpanelAPI mixpanelAPI;
    private TextView txtForForgotPwd;
    private boolean loginStatus = false;
    // FaceBook
    private Session.StatusCallback statusCallback = new SessionStatusCallback();
    private List<String> permissions = new ArrayList<String>();
    private String deviceVersion, deviceId, deviceModel, deviceName, deviceManufacturer;
    boolean isJailBroken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);

        Intent serviceIntent = new Intent(this, GoalsGenerateService.class);
        startService(serviceIntent);

        if (SessionStores.getLoginDate(this) != null) {
            if (Utils.calculateExpiresTime(SessionStores.getLoginDate(this), this) == true) {
                Intent intentToHome = new Intent(this, TabHostFragments.class);
                startActivity(intentToHome);
                finish();
            } else {
                Map<String, String> params = new HashMap<>();
                params.put("grant_type", "refresh_token");
                params.put("refresh_token", SessionStores.getRefreshToken(this));
                params.put("scope", "PRODUCTION");
                Map<String, String> params2 = new HashMap<>();
                if (SessionStores.getFbAccessToken(this) != null && SessionStores.getFbAccessToken(this).length() > 0) {
                    params2.put("UserName", SessionStores.getInstallationId(this));
                    params2.put("ExternalAccessToken", SessionStores.getFbAccessToken(this));
                    params2.put("LoginProvider", "FACEBOOK");
                    params2.put("BasicKey", Constants.BASIC_KEY_FB);
                } else {
                    params2.put("grant_type", "password");
                    params2.put("username", SessionStores.getUserName(this));
                    params2.put("password", SessionStores.getUserPwd(this));
                    params2.put("scope", "PRODUCTION");
                }
                new RenewingTokenTask(this, params, params2, progressBar);
            }
        } else {
            setContentView(R.layout.login);
            editTxtForEmail = (EditText) findViewById(R.id.editTxtForEmailAddress);
            editTxtForPwd = (EditText) findViewById(R.id.editTxtForPwd);
            btnForNext = (Button) findViewById(R.id.btnForLogin);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            signUpTxt = (TextView) findViewById(R.id.signup);
            layForSignUpTxt = (RelativeLayout) findViewById(R.id.layForSignUpTxt);
            btnForFb = (RelativeLayout) findViewById(R.id.btnForFbLogin);
            txtForForgotPwd = (TextView) findViewById(R.id.txtForFrgtPwd);

            mixpanelAPI = MixpanelAPI.getInstance(this, Constants.MIXPANEL_TOKEN);
            mixpanelAPI.track(getString(R.string.mixpanel_login_screen));

            if (SessionStores.getFbAccessToken(this) != null && SessionStores.getFbAccessToken(this).length() > 0) {
                btnForFb.setVisibility(View.VISIBLE);
            } else {
                btnForFb.setVisibility(View.GONE);
            }

            if (SessionStores.getFbPresent(this) != null) {
                btnForFb.setVisibility(View.VISIBLE);

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

            btnForNext.setOnClickListener(this);
            signUpTxt.setOnClickListener(this);
            btnForFb.setOnClickListener(this);
            txtForForgotPwd.setOnClickListener(this);

            installationId = SessionStores.getInstallationId(this);

            if (installationId != null && installationId.length() > 0) {
                layForSignUpTxt.setVisibility(View.GONE);
            } else {
                layForSignUpTxt.setVisibility(View.VISIBLE);
            }

            signUpTxt.setPaintFlags(signUpTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            if (!(Constants.COUNTRY_ARRAY.size() > 0)) {
                Utils.getCountry();
            }

            if (SessionStores.getInstallationId(this) != null && SessionStores.getFbAccessToken(this) != null && SessionStores.getFbAccessToken(this).length() > 0) {
                loginStatus = true;
            }

        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnForLogin) {
            Utils.hideKeyboard(this);
            if (loginStatus == false) {
                if (editTxtForEmail.getText().toString().length() > 0) {
                    if ((SessionStores.getUserName(this) == null) || editTxtForEmail.getText().toString().equals(SessionStores.getUserName(this))) {
                        if (editTxtForPwd.getText().toString().length() > 0) {
                            Map<String, String> params = new HashMap<>();
                            params.put("grant_type", "password");
                            params.put("username", editTxtForEmail.getText().toString());
                            params.put("password", editTxtForPwd.getText().toString());
                            params.put("scope", "PRODUCTION");

                            //mixpanel tracking
                            mixpanelAPI.track(getString(R.string.mixpanel_login_screen_email_button));

                            if (installationId != null && installationId.length() > 0) {
                                /** Login task executes */
                                new LoginTask(this, params, progressBar);
                            } else {
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
                                    Log.e("DeviceInfo", "" + deviceVersion + "\n" + deviceId + "\n" + deviceModel + "\n" + deviceName + "\n" + deviceManufacturer + "\n" + isJailBroken);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                /**
                                 * Send request body in json format with multiple json objects
                                 */
                                // First json object for device info
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
                                    jsonobjectMain.put("PushID", "12345678899797979797");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                new LoginRecoverTask(this, params, jsonobjectMain, progressBar);
                            }
                        } else {
                            Utils.ShowAlert(this, "Password field must not be empty");
                        }
                    } else {
                        Utils.ShowAlert(this, "Entered username is not match with the current signed user account, please try with valid username");
                    }
                } else {
                    Utils.ShowAlert(this, "Username field must not be empty");
                }
            } else {
                Utils.ShowAlert(this, "Your previous login is with facebook, so will you please try that?");
            }
        }

        if (v.getId() == R.id.signup) {
            Utils.hideKeyboard(this);
            Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(i);
            finish();
        }

        if (v.getId() == R.id.btnForFbLogin) {
            Utils.hideKeyboard(this);
            if (installationId != null && installationId.length() > 0) {
                Constants.USER_LOGIN = true;
                //    progressBar.setVisibility(View.VISIBLE);
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserName", SessionStores.getInstallationId(this));
                params.put("ExternalAccessToken", SessionStores.getFbAccessToken(this));
                params.put("LoginProvider", "FACEBOOK");
                params.put("BasicKey", Constants.BASIC_KEY_FB);

                //mixpanel tracking
                mixpanelAPI.track(getString(R.string.mixpanel_login_screen_facebook_button));

                new FbLoginTask(this, params, progressBar);
            } else {
            }
        }

        if (v.getId() == R.id.txtForFrgtPwd) {
            Utils.hideKeyboard(this);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(ApiClass.getBackendApiUrl(Constants.FORGOT_PSWD)));
            startActivity(i);
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
                            SessionStores.saveFbAccessToken(session.getAccessToken(), LoginActivity.this);

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

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    });
            request.executeAsync();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (SessionStores.getFbPresent(this) != null) {
            Session.getActiveSession().addCallback(statusCallback);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (SessionStores.getFbPresent(this) != null) {
            Session.getActiveSession().removeCallback(statusCallback);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (SessionStores.getFbPresent(this) != null) {
            Session session = Session.getActiveSession();
            Session.saveSession(session, outState);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (SessionStores.getFbPresent(this) != null) {
            Session.getActiveSession().onActivityResult(this, requestCode,
                    resultCode, data);
        }
    }
}


