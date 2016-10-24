package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import activities.mswift.info.walaapp.wala.support.CustomRequest;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 1/4/2016.
 */
public class FinancialInfo5Task {

    private Context context;
    private String authorizationKey, result = "", status = "", message = "", request, requestBackend;
    private FragmentManager fragmentManager;

    public FinancialInfo5Task(Context context, FragmentManager fragmentManager, String request, String requestBackend) {
        this.context = context;
        this.request = request;
        this.requestBackend = requestBackend;
        this.fragmentManager = fragmentManager;
        ((TabHostFragments) context).progressBarVisible();
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, ApiClass.getApiUrl(Constants.MONTHLY_EXPENSES), request, authorizationKey, SessionStores.getInstallationId(context), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // the response is already constructed as a JSONObject!
                try {
                    if (response != null) {
                        if (response.has("Status")) {
                            String status = response.getString("Status");
                            message = response.getString("Message");
                            Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                            SessionStores.saveFinancialStep4("true", context);
                            ((TabHostFragments) context).progressBarGone();
                            ((TabHostFragments) context).removeChild();
                            ((TabHostFragments) context).setcurrent(2);
                            new FinancialInfo5BackendTask(context, requestBackend);
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
