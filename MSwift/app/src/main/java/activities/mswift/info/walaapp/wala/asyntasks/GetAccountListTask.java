package activities.mswift.info.walaapp.wala.asyntasks;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 9/29/2016.
 */
public class GetAccountListTask {
    private Activity context;
    private Map<String, String> params;
    private JSONObject jObject = null;
    private String authorizationKey, result = "", status = "", message = "";
    private ProgressBar progressBar;
    private boolean savingsType = false, debtType = false;

    public GetAccountListTask(Activity context, ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getApiUrl(Constants.GETACCOUNT_LIST)).getJSONObjectfromURL(ServerResponse.RequestType.GET, null, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                String walletNumber = "";
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        if (jObject.has("Status")) {
                            progressBar.setVisibility(View.GONE);
                            message = jObject.getString("Message");

                            JSONArray jsonArray = jObject.getJSONArray("ListOfObjects");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String itemType = jsonObject.getString("FinancialItemType");
                                String email = jsonObject.getString("Member");
                                SessionStores.saveUserEmail(email, context);

                                if (itemType.equals("Asset")) {
                                    if (savingsType == false) {
                                        savingsType = true;
                                        walletNumber = jsonObject.getString("AccountNumber");
                                        SessionStores.saveAccNumberStep2(walletNumber, context);
                                        if (debtType == true) {
                                            break;
                                        }
                                    }
                                } else if (itemType.equals("Liability")) {
                                    if (debtType == false) {
                                        debtType = true;
                                        walletNumber = jsonObject.getString("AccountNumber");
                                        SessionStores.saveAccNumberStep3(walletNumber, context);
                                        if (savingsType == true) {
                                            break;
                                        }
                                    }
                                }
                            }

                            Map<String, String> params = new HashMap<>();
                            params.put("Email", SessionStores.getUserEmail(context));
                            new GetUserDetailsTask(context, params, progressBar);

                        } else {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        });
    }
}
