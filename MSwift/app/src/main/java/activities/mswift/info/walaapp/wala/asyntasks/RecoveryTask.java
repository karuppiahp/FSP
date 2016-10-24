package activities.mswift.info.walaapp.wala.asyntasks;

import android.app.Activity;
import android.content.Intent;
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

import activities.mswift.info.walaapp.wala.signup.OTPLoginActivity;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 9/27/2016.
 */
public class RecoveryTask {
    private Activity context;
    private String url, result, status = "", message = "";
    private JSONObject jObject = null;
    JSONObject params;
    private ProgressBar progressBar;
    String authorizationKey;

    public RecoveryTask(Activity context, JSONObject params, ProgressBar progressBar) {
        this.context = context;
        this.params = params;
        this.progressBar = progressBar;
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = Constants.BEARER_KEY;
        String URL = ApiClass.getApiUrl(Constants.RECOVERY);
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
                                    if (status.equals("000000")) { //Success state
                                        progressBar.setVisibility(View.GONE);
                                        Intent intentToOtp = new Intent(context, OTPLoginActivity.class);
                                        context.startActivity(intentToOtp);
                                        context.finish();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Please check your network availability", Toast.LENGTH_LONG).show();
            }
        }) {


            /** Passing some request headers*/
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + SessionStores.getAccessToken(context));
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
