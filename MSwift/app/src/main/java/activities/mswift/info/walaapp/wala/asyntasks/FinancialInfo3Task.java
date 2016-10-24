package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.more.FinancialInfoStep2Activity;
import activities.mswift.info.walaapp.wala.more.FinancialInfoStep3Activity;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 1/8/2016.
 */
public class FinancialInfo3Task {
    private Context context;
    private Map<String, String> params;
    private JSONObject jObject = null;
    private String authorizationKey, result = "", status = "", message = "", savingsType;
    private FragmentManager fragmentManager;

    public FinancialInfo3Task(Context context, Map<String, String> params, FragmentManager fragmentManager, String savingsType) {
        this.context = context;
        this.params = params;
        this.fragmentManager = fragmentManager;
        this.savingsType = savingsType;
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
                            ((TabHostFragments) context).progressBarGone();
                            message = jObject.getString("Message");
                            SessionStores.saveAccNumberStep2(String.valueOf(FinancialInfoStep2Activity.number), context);
                            Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                            SessionStores.saveFinancialStep2("true", context);
                            SessionStores.saveSavingsType(savingsType, context);
                            /**Move to another fragment*/
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            FinancialInfoStep3Activity fragment = new FinancialInfoStep3Activity();
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
