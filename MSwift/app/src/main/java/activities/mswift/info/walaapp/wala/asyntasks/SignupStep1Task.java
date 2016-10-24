package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import activities.mswift.info.walaapp.wala.signup.SignUpActivity;
import activities.mswift.info.walaapp.wala.signup.SignUpStep2Activity;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 2/29/2016.
 */
public class SignupStep1Task {

    private SignUpActivity context;
    private String url, result, status = "", message = "";
    private JSONObject jObject = null;
    private Map<String, String> params;
    private ProgressBar progressBar;

    public SignupStep1Task(SignUpActivity context, Map<String, String> params, ProgressBar progressBar) {
        this.context = context;
        this.params = params;
        this.progressBar = progressBar;
        progressBar.setVisibility(View.VISIBLE);
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse(ApiClass.getApiUrl(Constants.SIGNUP_VALIDATE)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, Constants.BEARER_KEY, context, "", new VolleyResponseListener() {
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
                        if (jObject.has("Status")) {
                            status = jObject.getString("Status");
                            message = jObject.getString("Message");

                            if (status.equals("000000")) {
                                progressBar.setVisibility(View.GONE);
                                SessionStores.saveUserName(SignUpActivity.editTxtForUsername.getText().toString(), context);
                                SessionStores.saveUserPwd(SignUpActivity.editTxtForPswd.getText().toString(), context);

                                /* Intent to Confirm OTP class */
                                Intent intentToOtp = new Intent(context, SignUpStep2Activity.class);
                                context.startActivity(intentToOtp);
                                context.finish();
                            } else if (status.equals("000310")) {
                                Utils.ShowAlertSignupActivity(context, "This Facebook account was registered by another member");
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Utils.ShowAlertSignupActivity(context, message);
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