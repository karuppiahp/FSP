package activities.mswift.info.walaapp.wala.asyntasks;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
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
 * Created by karuppiah on 12/17/2015.
 */
public class LoginTask {

    private Activity context;
    private Map<String, String> params;
    private JSONObject jObject = null;
    private String result = "", status = "", message = "", authorizationKey;
    private ProgressBar progressBar;

    public LoginTask(Activity context, Map<String, String> params, ProgressBar progressBar) {
        this.context = context;
        this.params = params;
        this.progressBar = progressBar;
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse(Constants.TOKEN_API).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, Constants.BASIC_KEY, context, "", new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, ""+"Please check your credentials or contact support@wala.com", Toast.LENGTH_LONG).show();
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
                            progressBar.setVisibility(View.GONE);
                            Intent intentToHome = new Intent(context, TabHostFragments.class);
                            context.startActivity(intentToHome);
                            context.finish();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
