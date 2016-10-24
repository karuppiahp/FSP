package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import activities.mswift.info.walaapp.wala.more.FinancialInfoStep2Activity;
import activities.mswift.info.walaapp.wala.more.FinancialInfoStep3Activity;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by kruno on 19.02.16..
 */
public class AddInitialValue {
    private Context context;
    private Map<String, String> params;
    private JSONObject jObject = null;
    private String authorizationKey, result = "", status = "", message = "", transId, savingsTypeBackend, amount;
    private String walletNumber;
    private FragmentManager fragmentManager;

    public AddInitialValue(Context context, Map<String, String> params, FragmentManager fragmentManager, String savingsTypeBackend, String amount) {
        this.context = context;
        this.params = params;
        this.fragmentManager = fragmentManager;
        this.savingsTypeBackend = savingsTypeBackend;
        this.amount = amount;
        ((TabHostFragments) context).progressBarVisible();
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getApiUrl(Constants.ADD_VALUE)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
            @Override
            public void onError(String message) {

                Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
            }


            @Override
            public void onResponse(String response) {
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        if (jObject.has("Status")) {
                            JSONObject dataObj = jObject.getJSONObject("DataObject");
                            transId = dataObj.getString("TransactionId");
                            message = jObject.getString("Message");
                            Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();

                            if (Constants.FINANCIAL_TYPE == "savings") {
                                Map<String, String> params1 = new HashMap<>();
                                params1.put("user_email", "" + SessionStores.getUserEmail(context));
                                params1.put("saving_type", "" + savingsTypeBackend);
                                params1.put("current_savings_amount", "" + amount);
                                params1.put("tipsgo_transaction_id", "" + transId);
                                params1.put("result", FinancialInfoStep2Activity.resultBackend);
                                new FinancialInfo3BackendTask(context, params1);
                            } else {
                                Map<String, String> params1 = new HashMap<>();
                                params1.put("user_email", "" + SessionStores.getUserEmail(context));
                                params1.put("debt_types", "" + savingsTypeBackend);
                                params1.put("current_debt_amount", "" + amount);
                                params1.put("tipsgo_transaction_id", "" + transId);
                                params1.put("result", FinancialInfoStep3Activity.resultBackend);
                                new FinancialInfo4BackendTask(context, params1);
                            }
                        } else {
                            ((TabHostFragments) context).progressBarGone();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
