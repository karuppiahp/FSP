package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.widget.Toast;

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
 * Created by karuppiah on 5/5/2016.
 */
public class UpdateGoalResultTask {

    private Context context;
    private JSONObject jObject = null;
    private Map<String, String> params;
    private String authorizationKey, result = "", status = "", message = "";

    public UpdateGoalResultTask(Context context, Map<String, String> params) {
        this.context = context;
        this.params = params;
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getBackendApiUrl(Constants.UPDATEGOAL_RESULT)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
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
                        if (jObject.has("status")) {
                            status = jObject.getString("status");
                            message = jObject.getString("message");
                            if (status.equals("success")) {
                                Map<String, String> params = new HashMap<>();
                                params.put("user_email", "" + SessionStores.getUserEmail(context));
                                new GoalsGenerateTask(context, params);
                            }
                        } else {
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
