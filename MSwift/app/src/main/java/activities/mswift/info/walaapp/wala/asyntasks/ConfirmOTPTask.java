package activities.mswift.info.walaapp.wala.asyntasks;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import activities.mswift.info.walaapp.wala.signup.SignupSuccessActivity;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 12/14/2015.
 */
public class ConfirmOTPTask {

    private Activity context;
    private String url, result, status = "", message = "", registerToken;
    private JSONObject jObject = null;
    private ProgressBar progressBar;
    private Map<String, String> params2;

    public ConfirmOTPTask(Activity context, String registerToken, ProgressBar progressBar) {
        this.context = context;
        this.registerToken = registerToken;
        this.progressBar = progressBar;
        progressBar.setVisibility(View.VISIBLE);
        ResponseTask();
    }

    public ConfirmOTPTask(Activity context, String registerToken, ProgressBar progressBar, Map<String, String> params2) {
        this.context = context;
        this.registerToken = registerToken;
        this.progressBar = progressBar;
        this.params2 = params2;
        progressBar.setVisibility(View.VISIBLE);
        ResponseTask();
    }

    public void ResponseTask() {
        Map<String, String> params = new HashMap<>();
        params.put("OTPValue",Constants.OTP_VALUE);
        params.put("RegistrationToken", registerToken);
        new ServerResponse(ApiClass.getApiUrl(Constants.CONFIRM_OTP)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, Constants.BEARER_KEY, context, "", new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                SessionStores.saveRegisterToken(null, context);
                SessionStores.saveInstallationId(null, context);
                Utils.IntroActivity(context, message);
            }

            @Override
            public void onResponse(String response) {
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        if (jObject.has("Status")) {
                            status = jObject.getString("Status");
                            message = jObject.getString("Message");
                            if (status.equals("000000")) { //Success state
                                JSONObject jsonObj = jObject.getJSONObject("DataObject");
                                String installationId = jsonObj.getString("InstallationID");
                                if(SessionStores.getRegViaFb(context) == null) {
                                    SessionStores.saveInstallationId(installationId, context);
                                }
                                //user coming from sign up
                                Constants.userRegister = true;

                                if(Constants.FB_BUTTON_CLICKS == true) {
                                    new FbRegisterTask(context, params2, progressBar);
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Intent intentToSuccess = new Intent(context, SignupSuccessActivity.class);
                                    context.startActivity(intentToSuccess);
                                    context.finish();
                                }
                            } else if(status.equals("900009")){
                                Utils.OtpLoginActivity(context,message);
                            } else if(status.equals("000016")){
                                Utils.OtpActivity(context,message);
                            }else { //Fails state
                                progressBar.setVisibility(View.GONE);
                                SessionStores.saveRegisterToken(null, context);
                                SessionStores.saveInstallationId(null, context);
                                Utils.IntroActivity(context, message);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    SessionStores.saveRegisterToken(null, context);
                    SessionStores.saveInstallationId(null, context);
                    Utils.IntroActivity(context, message);
                }
            }
        });
    }
}