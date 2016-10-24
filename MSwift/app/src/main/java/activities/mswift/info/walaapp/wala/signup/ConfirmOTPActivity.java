package activities.mswift.info.walaapp.wala.signup;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.asyntasks.ConfirmOTPTask;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.RootUtils;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 12/15/2015.
 */
public class ConfirmOTPActivity extends Activity implements View.OnClickListener {

    private EditText editTxtForOtp;
    private Button btnForFinish;
    private ProgressBar progressBar;
    private int density;
    private TextView txtForOtpHeader;
    private MixpanelAPI mixpanelAPI;
    private String deviceVersion, deviceId, deviceModel, deviceName, deviceManufacturer;
    boolean isJailBroken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.confirm_otp);

        editTxtForOtp = (EditText) findViewById(R.id.editTxtForOtp);
        btnForFinish = (Button) findViewById(R.id.btnForFinish);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtForOtpHeader = (TextView) findViewById(R.id.txtForOtpHeader);

        // mixpanel screen tracking
        mixpanelAPI = MixpanelAPI.getInstance(this, Constants.MIXPANEL_TOKEN);
        mixpanelAPI.track(getString(R.string.mixpanel_confirm_otp_screen));

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
            e.printStackTrace();
        }

        btnForFinish.setOnClickListener(this);

        density = getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_LOW:
                txtForOtpHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.subtitle_text_size));
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                txtForOtpHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.subtitle_text_size));
                break;
            case DisplayMetrics.DENSITY_HIGH:
                txtForOtpHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.subtitle_text_size));
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                txtForOtpHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.textSizeLarge));
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                txtForOtpHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.textSizeLarge));

                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnForFinish) {
            Utils.hideKeyboard(this);
            if (editTxtForOtp.getText().toString().length() > 0) {
                if (editTxtForOtp.getText().toString().length() == 6) {
                    Constants.OTP_VALUE = editTxtForOtp.getText().toString();
                    apiCall();
                } else {
                    Utils.ShowAlert(this, "Please enter Valid OTP");
                }
            } else {
                Utils.ShowAlert(this, "Please enter the OTP");
            }
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

        params.put("PushID", "12345678935426565");
        params.put("SocialProvider", "FACEBOOK");
        params.put("SocialAccessToken", SessionStores.getFbAccessToken(this));

        String token = SessionStores.getRegisterToken(this);
        Log.e("register token>>>", "" + token);
        new ConfirmOTPTask(this, token, progressBar, params);
    }

}
