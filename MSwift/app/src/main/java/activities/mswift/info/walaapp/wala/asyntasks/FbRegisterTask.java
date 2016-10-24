package activities.mswift.info.walaapp.wala.asyntasks;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import activities.mswift.info.walaapp.wala.signup.SignupSuccessActivity;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 3/21/2016.
 */
public class FbRegisterTask {
    private Activity context;
    private Map<String, String> params;
    private JSONObject jObject = null;
    private String authorizationKey, result = "", status = "", message = "";
    private ProgressBar progressBar;

    public FbRegisterTask(Activity context, Map<String, String> params, ProgressBar progressBar) {
        this.context = context;
        this.params = params;
        this.progressBar = progressBar;
        progressBar.setVisibility(View.VISIBLE);
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = Constants.BEARER_KEY;

        new ServerResponse(ApiClass.getApiUrl(Constants.FB_REGISTER)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        status = jObject.getString("Status");
                        message = jObject.getString("Message");
                        if (jObject.has("DataObject")) {
                            JSONObject dataObj = jObject.getJSONObject("DataObject");
                            String installationId = dataObj.getString("InstallationID");
                            SessionStores.saveInstallationId(installationId, context);
                            SessionStores.saveFbInstallationId(installationId, context);
                            SessionStores.saveRegViaFb("true", context);
                            progressBar.setVisibility(View.GONE);

                            Intent intentToSuccess = new Intent(context, SignupSuccessActivity.class);
                            context.startActivity(intentToSuccess);
                            context.finish();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            SessionStores.saveRegisterToken(null, context);
                            SessionStores.saveInstallationId(null, context);
                            Utils.IntroActivity(context, "This Facebook account was registered by another member");
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        SessionStores.saveRegisterToken(null, context);
                        SessionStores.saveInstallationId(null, context);
                        Utils.IntroActivity(context, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    SessionStores.saveRegisterToken(null, context);
                    SessionStores.saveInstallationId(null, context);
                    Utils.IntroActivity(context, message);
                }
            }
        });
    }
}
