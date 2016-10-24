package activities.mswift.info.walaapp.wala.asyntasks;

import android.app.Activity;

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

import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by kruno on 19.02.16..
 */
public class CreateWalletTask {
    private Activity context;
    private String authorizationKey;

    public CreateWalletTask(Activity context) {
        this.context = context;

        ResponseTask();
    }

    public void ResponseTask() {

        JSONObject request = getJsonWalletName("savings");
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        String URL = ApiClass.getApiUrl(Constants.CREATEDEPT_WALLET);

        JsonObjectRequest jsonObjReq1 = new JsonObjectRequest(Request.Method.POST,
                URL, request,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        if (response.has("DataObject")) {

                            JSONObject jsonObject1 = null;
                            try {
                                jsonObject1 = response.getJSONObject("DataObject");

                                String savingWalletCode = jsonObject1.getString("WalletCode");
                                SessionStores.saveSavingsWalletNumber(savingWalletCode, context);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

    public JSONObject getJsonWalletName(String name) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("WalletName", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
