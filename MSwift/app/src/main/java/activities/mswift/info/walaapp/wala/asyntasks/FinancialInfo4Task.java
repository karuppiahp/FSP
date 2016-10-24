package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.more.FinancialInfoStep3Activity;
import activities.mswift.info.walaapp.wala.more.FinancialInfoStep4Activity;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 1/9/2016.
 */
public class FinancialInfo4Task {

    private Context context;
    private Map<String, String> params;
    private JSONObject jObject = null;
    private String authorizationKey, result = "", status = "", message = "", debtOweType, debtOwed;
    private FragmentManager fragmentManager;

    public FinancialInfo4Task(Context context, Map<String, String> params, FragmentManager fragmentManager, String debtOweType, String debtOwed) {
        this.context = context;
        this.params = params;
        this.fragmentManager = fragmentManager;
        this.debtOweType = debtOweType;
        this.debtOwed = debtOwed;
        ((TabHostFragments) context).progressBarVisible();
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getApiUrl(Constants.CREATE_ACCOUNT)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
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
                            message = jObject.getString("Message");
                            SessionStores.saveAccNumberStep3(String.valueOf(FinancialInfoStep3Activity.number), context);
                            Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                            SessionStores.saveFinancialStep3("true", context);
                            /**Move to another fragment*/
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            FinancialInfoStep4Activity fragment = new FinancialInfoStep4Activity();
                            fragmentTransaction.replace(R.id.realtabcontent, fragment);
                            fragmentTransaction.commit();
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

    public void DebtOwedTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        long number = (long) Math.floor(Math.random() * 9000000000L) + 1000000000L;
        Map<String, String> params = new HashMap<>();
        params.put("AccountNumber", "" + number);
        params.put("AccountName", "" + Constants.USER_NAME);
        params.put("AccountType", "" + debtOweType);
        params.put("ProductVariant", "");
        params.put("Tip", "");
        params.put("Portfolio", "");
        params.put("Adviser", "");
        params.put("ProdctProvider", "");
        params.put("FinancialItemType", "Liability");
        params.put("Owner", "Self");
        params.put("OpeningBalance", "0.0");
        params.put("CurrentBalance", "" + debtOwed);
        params.put("InterestRate", "0.0");
        params.put("Currency", "");
        new ServerResponse(ApiClass.getApiUrl(Constants.CREATE_ACCOUNT)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                ((TabHostFragments) context).progressBarGone();
                Toast.makeText(context, "Please check your network availability", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        if (jObject.has("Status")) {
                            ((TabHostFragments) context).progressBarGone();
                            message = jObject.getString("Message");
                            Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                            /**Move to another fragment*/
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            FinancialInfoStep4Activity fragment = new FinancialInfoStep4Activity();
                            fragmentTransaction.replace(R.id.realtabcontent, fragment);
                            fragmentTransaction.commit();
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
