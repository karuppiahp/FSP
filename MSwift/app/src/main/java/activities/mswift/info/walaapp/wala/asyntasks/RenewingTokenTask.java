package activities.mswift.info.walaapp.wala.asyntasks;

import android.app.Activity;
import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 4/2/2016.
 */
public class RenewingTokenTask {

    private Activity context;
    private Map<String, String> params, params2;
    private JSONObject jObject = null;
    private String result = "", status = "", message = "";

    public RenewingTokenTask(Activity context, Map<String, String> params, Map<String, String> params2, ProgressBar progressBar) {
        this.context = context;
        this.params = params;
        this.params2 = params2;
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse(Constants.TOKEN_API).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, Constants.BASIC_KEY, context, "", new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                if (SessionStores.getFbAccessToken(context) != null && SessionStores.getFbAccessToken(context).length() > 0) {
                    new RenewFbLoginTask(context, params2);
                } else {
                    new RenewLoginTask(context, params2);
                }
            }

            @Override
            public void onResponse(String response) {
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        if (jObject.has("access_token")) {
                            String accessToken = jObject.getString("access_token");
                            String refreshToken = jObject.getString("refresh_token");
                            String expiresIn = jObject.getString("expires_in");
                            //get current date for calculate expires time in
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            String formattedDate = df.format(c.getTime());
                            SessionStores.saveAccessToken(accessToken, context);
                            SessionStores.saveRefreshToken(refreshToken, context);
                            SessionStores.saveTimeExpiresIn(expiresIn, context);
                            SessionStores.saveLoginDate(formattedDate, context);
                            Constants.USER_NAME = params.get("username");
                            Intent intentToHome = new Intent(context, TabHostFragments.class);
                            context.startActivity(intentToHome);
                            context.finish();
                        } else {
                            Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Please check your network availability", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
