package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.more.MoreActivity;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 2/9/2016.
 */
public class ChangePasswordTask {

    private Context context;
    private Map<String, String> params;
    private JSONObject jObject = null;
    private String result = "", status = "", message = "", authorizationKey;
    private FragmentManager fragmentManager;

    public ChangePasswordTask(Context context, Map<String, String> params, FragmentManager fragmentManager) {
        this.context = context;
        this.params = params;
        this.fragmentManager = fragmentManager;
        ((TabHostFragments) context).progressBarVisible();
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getApiUrl(Constants.CHANGE_PSWD)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
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
                            if (jObject.getString("Status").equals("000000")) {
                                Toast.makeText(context, "Your password has been updated successfully.", Toast.LENGTH_SHORT).show();
                                SessionStores.saveUserPwd(params.get("NewPassword"), context);
                                ((TabHostFragments) context).progressBarGone();
                                ((TabHostFragments) context).removeChild();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                MoreActivity fragment = new MoreActivity();
                                fragmentTransaction.replace(R.id.realtabcontent, fragment);
                                fragmentTransaction.commit();
                            } else {
                                ((TabHostFragments) context).progressBarGone();
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        ((TabHostFragments) context).progressBarGone();
                    }
                } catch (JSONException e) {
                    ((TabHostFragments) context).progressBarGone();
                    e.printStackTrace();
                }
            }
        });
    }
}
