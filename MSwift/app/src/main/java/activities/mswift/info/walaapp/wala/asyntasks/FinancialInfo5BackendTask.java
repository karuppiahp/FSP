package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import activities.mswift.info.walaapp.wala.more.FinancialInfoStep4Activity;
import activities.mswift.info.walaapp.wala.support.CustomRequest;
import activities.mswift.info.walaapp.wala.support.GoalsGenerateService;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 4/12/2016.
 */
public class FinancialInfo5BackendTask {
    private Context context;
    private JSONObject jObject = null;
    private String authorizationKey, result = "", status = "", message = "", request;

    public FinancialInfo5BackendTask(Context context, String request) {
        this.context = context;
        this.request = request;
        ((TabHostFragments) context).progressBarVisible();
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, ApiClass.getBackendApiUrl(Constants.ADDEXPENSE_BACK), request, authorizationKey, SessionStores.getInstallationId(context), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // the response is already constructed as a JSONObject!
                try {
                    Log.e("addexperesponse>>>>>>>>", "" + response);
                    if (response != null) {
                        if (response.has("status")) {
                            String status = response.getString("status");
                            message = response.getString("message");
                            Log.e("result backend??", "" + FinancialInfoStep4Activity.resultBackend);
                            if (FinancialInfoStep4Activity.resultBackend.equals("1")) {
                                Log.e("inside result backend??", "loop");
                                Intent serviceIntent = new Intent(context, GoalsGenerateService.class);
                                context.startService(serviceIntent);
                            }
                        } else {
                            ((TabHostFragments) context).progressBarGone();
                        }
                    } else {
                        ((TabHostFragments) context).progressBarGone();
                    }
                } catch (JSONException e) {
                    ((TabHostFragments) context).progressBarGone();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                ((TabHostFragments) context).progressBarGone();
            }
        });

        Volley.newRequestQueue(context).add(jsonRequest);

    }
}
