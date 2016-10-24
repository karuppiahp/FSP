package activities.mswift.info.walaapp.wala.asyntasks;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 9/29/2016.
 */
public class ConfirmOTPLoginTask {
    private Activity context;
    private String url, result, status = "", message = "", registerToken;
    private JSONObject jObject = null;
    private ProgressBar progressBar;
    private Map<String, String> params2;

    public ConfirmOTPLoginTask(Activity context, String registerToken, ProgressBar progressBar, Map<String, String> params2) {
        this.context = context;
        this.registerToken = registerToken;
        this.progressBar = progressBar;
        this.params2 = params2;
        progressBar.setVisibility(View.VISIBLE);
        ResponseTask();
    }

    public void ResponseTask() {
        String authorisationKey = "Bearer " + SessionStores.getAccessToken(context);
        Map<String, String> params = new HashMap<>();
        params.put("OTPValue", Constants.OTP_VALUE);
        params.put("Action", "RecoveryMember");
        new ServerResponse(ApiClass.getApiUrl(Constants.CONFIRM_OTP)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, authorisationKey, context, "", new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                SessionStores.saveRegisterToken(null, context);
                SessionStores.saveInstallationId(null, context);
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
                                String fbPresent = jsonObj.getString("HasFacebookLinking");
                                SessionStores.saveInstallationId(installationId, context);
                                if (fbPresent.equals("true")) {
                                    SessionStores.saveFbPresent(fbPresent, context);
                                }
                                new GetAccountListTask(context, progressBar);
                            } else if (status.equals("900009")) {
                                progressBar.setVisibility(View.GONE);
                                Utils.OtpLoginActivity(context, message);
                            } else if (status.equals("000016")) {
                                progressBar.setVisibility(View.GONE);
                                Utils.OtpLoginActivity(context, message);
                            } else { //Fails state
                                progressBar.setVisibility(View.GONE);
                                SessionStores.saveRegisterToken(null, context);
                                SessionStores.saveInstallationId(null, context);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    SessionStores.saveRegisterToken(null, context);
                    SessionStores.saveInstallationId(null, context);
                }
            }
        });
    }
}
