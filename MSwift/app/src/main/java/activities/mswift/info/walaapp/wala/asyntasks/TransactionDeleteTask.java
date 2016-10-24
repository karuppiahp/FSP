package activities.mswift.info.walaapp.wala.asyntasks;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import activities.mswift.info.walaapp.wala.adapter.ManageTransactionAdapter;
import activities.mswift.info.walaapp.wala.transactions.TransactionManageFragment;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by kruno on 11.02.16..
 */
public class TransactionDeleteTask {
    private Activity context;
    private String url, result, status = "", message = "", registerToken, transId;
    private String authorizationKey;
    private JSONObject request;
    private int itemPosition;
    private JSONArray jsonArray;
    private SwipeMenuListView mListView;

    public TransactionDeleteTask(Activity context, JSONObject request, int itemPosition, JSONArray jsonArray, SwipeMenuListView mListView, String transId) {
        this.context = context;
        this.request = request;
        this.itemPosition = itemPosition;
        this.jsonArray = jsonArray;
        this.mListView = mListView;
        this.transId = transId;
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        String URL = ApiClass.getApiUrl(Constants.TRANSACTION_DELETE);

        JsonObjectRequest jsonObjReq1 = new JsonObjectRequest(Request.Method.POST,
                URL, request,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("Status");
                            if (status.equals("000000")) {

                                if (jsonArray != null) {
                                    jsonArray = deleteJsonObject(jsonArray, itemPosition);
                                    mListView.setAdapter(new ManageTransactionAdapter(jsonArray, context));
                                    TransactionManageFragment.jsonArray = jsonArray;
                                    Toast.makeText(context, "Transaction deleted ", Toast.LENGTH_SHORT).show();
                                    Map<String, String> params1 = new HashMap<>();
                                    params1.put("tipsgo_transaction_id", "" + transId);
                                    new TransactionDeleteBackendTask(context, params1);
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

    //delete jsonObject from array to support api 16
    private JSONArray deleteJsonObject(JSONArray jsonArray, int position) {

        JSONArray list = new JSONArray();
        JSONArray jsonArray1 = jsonArray;
        int len = jsonArray1.length();
        if (jsonArray1 != null) {
            for (int i = 0; i < len; i++) {
                //Excluding the item at position
                if (i != position) {
                    try {
                        list.put(jsonArray1.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return list;
    }
}
