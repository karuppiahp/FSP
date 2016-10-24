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
import activities.mswift.info.walaapp.wala.more.FinancialInfoStep2Activity;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 1/11/2016.
 */
public class FinancialInfo2Task {
    private Context context;
    private Map<String, String> params, paramsBackend;
    private JSONObject jObject = null;
    private String authorizationKey, result = "", status = "", message = "", empStatus;
    private FragmentManager fragmentManager;

    public FinancialInfo2Task(Context context, Map<String, String> params, FragmentManager fragmentManager, String empStatus, Map<String, String> paramsBackend) {
        this.context = context;
        this.params = params;
        this.fragmentManager = fragmentManager;
        this.empStatus = empStatus;
        this.paramsBackend = paramsBackend;
        ((TabHostFragments) context).progressBarVisible();
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getApiUrl(Constants.INCOME_UPDATE)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
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
                            EmpStatusTask();
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

    /*
      0 - Unknown
     */
    public void EmpStatusTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        Map<String, String> params = new HashMap<>();
        params.put("RelationshipStatus", "0");
        params.put("CareerStatus", "" + empStatus);
        params.put("FamilyStatus", "0");
        params.put("PropertyOwnershipStatus", "0");
        params.put("VehicleOwnershipStatus", "0");
        params.put("CaringStatus", "0");
        new ServerResponse(ApiClass.getApiUrl(Constants.EMPLOYEMNTLIFE_UPDATE)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
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
                            SessionStores.saveFinancialStep1("true", context);
                            /**Move to another fragment*/
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            FinancialInfoStep2Activity fragment = new FinancialInfoStep2Activity();
                            fragmentTransaction.replace(R.id.realtabcontent, fragment);
                            fragmentTransaction.commit();
                            new FinancialInfo2BackendTask(context, paramsBackend);
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
