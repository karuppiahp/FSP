package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import activities.mswift.info.walaapp.wala.signup.ConfirmOTPActivity;
import activities.mswift.info.walaapp.wala.signup.SignUpStep2Activity;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 4/5/2016.
 */
public class AddUserTask {
    private SignUpStep2Activity context;
    private String url, result, status = "", message = "";
    private JSONObject jObject = null;
    private Map<String, String> params;
    private ProgressBar progressBar;

    public AddUserTask(SignUpStep2Activity context, Map<String, String> params, ProgressBar progressBar) {
        this.context = context;
        this.params = params;
        this.progressBar = progressBar;
        progressBar.setVisibility(View.VISIBLE);
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse(ApiClass.getBackendApiUrl(Constants.ADDUSER_BACKEND)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, Constants.BEARER_KEY, context, "", new VolleyResponseListener() {
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
                        if (jObject.has("status")) {
                            status = jObject.getString("status");
                            if (status.equals("success")) {
                                progressBar.setVisibility(View.GONE);
                                /* Intent to Confirm OTP class */
                                Intent intentToOtp = new Intent(context, ConfirmOTPActivity.class);
                                context.startActivity(intentToOtp);
                                context.finish();
                            } else {
                                message = jObject.getString("message");
                                progressBar.setVisibility(View.GONE);
                                Utils.ShowAlert(context, message);
                            }
                        }
                    }
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        });
    }
}
