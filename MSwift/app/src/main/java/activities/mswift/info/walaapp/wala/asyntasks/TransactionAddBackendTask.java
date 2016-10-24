package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.transactions.TransactionActivity;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 4/19/2016.
 */
public class TransactionAddBackendTask {
    private Context context;
    private JSONObject jObject = null;
    private Map<String, String> params;
    private String authorizationKey, result = "", status = "", message = "";
    private FragmentManager fragmentManager;

    public TransactionAddBackendTask(Context context, Map<String, String> params, FragmentManager fragmentManager) {
        this.context = context;
        this.params = params;
        this.fragmentManager = fragmentManager;
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getBackendApiUrl(Constants.ADDTRANS_BACK)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
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
                        if (jObject.has("status")) {
                            ((TabHostFragments) context).progressBarGone();
                            message = jObject.getString("message");
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            TransactionActivity fragment = new TransactionActivity();
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
