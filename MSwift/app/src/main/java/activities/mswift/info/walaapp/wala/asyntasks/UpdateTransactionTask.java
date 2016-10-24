package activities.mswift.info.walaapp.wala.asyntasks;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.progress.BalancesActivity;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.transactions.TransactionActivity;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by kruno on 11.02.16..
 */
public class UpdateTransactionTask {

    private Activity context;
    private String url, result, status = "", message = "", registerToken;
    private JSONObject jObject = null;
    private String authorizationKey;
    private JSONObject request;
    private FragmentManager fragmentManager;
    private Map<String, String> params;
    int TAG = 0;

    public UpdateTransactionTask(Activity context, JSONObject request, FragmentManager fragmentManager, int TAG, Map<String, String> params) {
        this.context = context;
        this.request = request;
        this.TAG = TAG;
        this.fragmentManager = fragmentManager;
        this.params = params;

        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        String URL = ApiClass.getApiUrl(Constants.UPDATE_TRANSACTION);

        JsonObjectRequest jsonObjReq1 = new JsonObjectRequest(Request.Method.POST,
                URL, request,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("Status");
                            if (status.equals("000000")) {

                                new TransEditBackendTask(context, params);
                                ((TabHostFragments) context).removeChild();
                                if (TAG == 0) {
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    TransactionActivity fragment = new TransactionActivity();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("viewPagerPosition", 1);
                                    fragment.setArguments(bundle);
                                    fragmentTransaction.replace(R.id.realtabcontent, fragment);
                                    fragmentTransaction.commit();
                                }
                                if (TAG == 1) {
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    BalancesActivity fragment = new BalancesActivity();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("viewPagerPosition", 0);
                                    fragment.setArguments(bundle);
                                    fragmentTransaction.replace(R.id.realtabcontent, fragment);
                                    fragmentTransaction.commit();
                                }
                            } else {
                                Toast.makeText(context, response.getString("Message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", authorizationKey);
                headers.put("AppId", "WalaAndroidApp");
                headers.put("DeviceId", "132432345332456578798");
                headers.put("InstallationId", SessionStores.getInstallationId(context));
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(jsonObjReq1);
    }
}
