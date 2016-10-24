package activities.mswift.info.walaapp.wala.asyntasks;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import activities.mswift.info.walaapp.wala.signup.SignUpStep2Activity;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 12/5/2015.
 */
public class SignUpTask {
    private SignUpStep2Activity context;
    private String url, result, status = "", message = "", fbConnect, installationId, authorizationKey, referralId;
    private JSONObject jObject = null;
    private JSONObject params;
    private ProgressBar progressBar;

    public SignUpTask(SignUpStep2Activity context, JSONObject params, ProgressBar progressBar) {
        this.context = context;
        this.params = params;
        this.progressBar = progressBar;
        progressBar.setVisibility(View.VISIBLE);
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = Constants.BEARER_KEY;
        String URL = ApiClass.getApiUrl(Constants.SIGNUP_REGISTER);
        JsonObjectRequest jsonObjReq1 = new JsonObjectRequest(Request.Method.POST,
                URL, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        result = response.toString();
                        try {
                            jObject = new JSONObject(result);
                            if (jObject != null) {
                                if (jObject.has("Status")) {
                                    status = jObject.getString("Status");
                                    message = jObject.getString("Message");

                                    if (status.equals("000000")) {
                                        JSONObject jsonObj = jObject.getJSONObject("DataObject");
                                        String registerToken = jsonObj.getString("RegistrationToken");
                                        SessionStores.saveRegisterToken(registerToken, context);
                                        SessionStores.saveCountry(Constants.COUNTRY, context);
                                        SessionStores.saveCity(Constants.CITY, context);
                                        SessionStores.saveUserEmail(Constants.EMAIL_ADDRESS, context);

                                        if (Constants.FB_BUTTON_CLICKS == true) {
                                            fbConnect = "1";
                                        } else {
                                            fbConnect = "0";
                                        }

                                        if (SessionStores.getReferralEmailId(context) == null) {
                                            referralId = "";
                                        } else {
                                            referralId = SessionStores.getReferralEmailId(context);
                                        }

                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("FirstName", Constants.FIRST_NAME);
                                        params.put("MiddleName", SignUpStep2Activity.editTxtForMiddleName.getText().toString());
                                        params.put("LastName", Constants.LAST_NAME);
                                        params.put("DateOfBirth", Utils.dateConvertPersonalInfo(Constants.DOB));
                                        params.put("Gender", "Male");
                                        params.put("DisplayName", Constants.FIRST_NAME);
                                        params.put("Email", Constants.EMAIL_ADDRESS);
                                        params.put("PhoneNumber", Constants.PHONE_NUMBER);
                                        params.put("UserName", Constants.USER_NAME);
                                        params.put("Country", Constants.COUNTRY);
                                        params.put("City", Constants.CITY);
                                        params.put("FbConnect", fbConnect);
                                        params.put("referral", referralId);
                                        new AddUserTask(context, params, progressBar);
                                    } else {
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
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Please check your network availability", Toast.LENGTH_LONG).show();
            }
        }) {


            /** Passing some request headers*/

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", authorizationKey);
                headers.put("AppId", "WalaAndroidApp");
                headers.put("DeviceId", "132432345332456578798");
                headers.put("InstallationId", "");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(jsonObjReq1);
    }
}
