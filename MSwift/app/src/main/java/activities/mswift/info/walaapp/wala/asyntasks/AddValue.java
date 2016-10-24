package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.transactions.TransactionsAdd;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by kruno on 11.02.16..
 */
public class AddValue {
    private Context context;
    private Map<String, String> params;
    private JSONObject jObject = null;
    private String authorizationKey, result = "", status = "", message = "", transId;
    private FragmentManager fragmentManager;
    private String[] transBackendMapping;
    private int arrayNumber;

    public AddValue(Context context, Map<String, String> params, FragmentManager fragmentManager, int arrayNumber) {
        this.context = context;
        this.params = params;
        this.fragmentManager = fragmentManager;
        this.arrayNumber = arrayNumber;
        ((TabHostFragments) context).progressBarVisible();
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getApiUrl(Constants.ADD_VALUE)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                ((TabHostFragments) context).progressBarGone();
                Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        if (jObject.has("Status")) {
                            if (jObject.has("DataObject")) {
                                JSONObject dataObj = jObject.getJSONObject("DataObject");
                                transId = dataObj.getString("TransactionId");
                                message = jObject.getString("Message");
                                Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                                ((TabHostFragments) context).removeChild();
                                transBackendMapping = context.getResources().getStringArray(R.array.transBackendMapping);
                                String transBackendMap = transBackendMapping[arrayNumber];
                                Map<String, String> paramsBckend = new HashMap<>();
                                paramsBckend.put("user_email", "" + SessionStores.getUserEmail(context));
                                paramsBckend.put("tipsgo_transaction_id", "" + transId);
                                paramsBckend.put("amount", "" + TransactionsAdd.editTxtForDebt.getText().toString());
                                paramsBckend.put("description", "" + TransactionsAdd.editTxtForDescription.getText().toString());
                                paramsBckend.put("transaction_date", "" + Utils.dateConvert(TransactionsAdd.txtForDotSelected.getText().toString()));
                                paramsBckend.put("transaction_type", "" + transBackendMap);
                                new TransactionAddBackendTask(context, paramsBckend, fragmentManager);
                            } else {
                                ((TabHostFragments) context).progressBarGone();
                                message = jObject.getString("Message");
                                Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            ((TabHostFragments) context).progressBarGone();
                        }
                    }
                } catch (JSONException e) {
                    ((TabHostFragments) context).progressBarGone();
                    e.printStackTrace();
                }
            }
        });
    }
}
