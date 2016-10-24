package activities.mswift.info.walaapp.wala.asyntasks;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by kruno on 22.01.16..
 */
public class GetWalletList {
    private Activity context;
    private Map<String, String> params;
    private JSONObject jObject = null;
    private String authorizationKey, result = "", status = "", message = "";
    private ProgressBar progressBar;

    public GetWalletList(Activity context, ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getApiUrl(Constants.GETWALLET_LIST)).getJSONObjectfromURL(ServerResponse.RequestType.GET, null, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
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
                                String name = jsonObject.getString("Name");
                                String email = jsonObject.getString("Member");
                                SessionStores.saveUserEmail(email, context);

                                if (name.equals("debt")) {
                                    walletNumber = jsonObject.getString("WalletCode");
                                    SessionStores.saveDebtWalletNumber(walletNumber, context);
                                } else if (name.equals("savings")) {
                                    walletNumber = jsonObject.getString("WalletCode");
                                    SessionStores.saveSavingsWalletNumber(walletNumber, context);
                                } else {
                                    walletNumber = jsonObject.getString("WalletCode");
                                    SessionStores.saveWalletNumber(walletNumber, context);

                                }
                            }

                            Intent intentToHome = new Intent(context, TabHostFragments.class);
                            context.startActivity(intentToHome);
                            context.finish();

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
