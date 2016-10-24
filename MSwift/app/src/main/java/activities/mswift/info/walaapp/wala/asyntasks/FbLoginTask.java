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
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 3/22/2016.
 */
public class FbLoginTask {
    private Activity context;
    private Map<String, String> params;
    private JSONObject jObject = null;
    private String authorizationKey, result = "", status = "", message = "";
    private ProgressBar progressBar;

    public FbLoginTask(Activity context, Map<String, String> params, ProgressBar progressBar) {
        this.context = context;
        this.params = params;
        this.progressBar = progressBar;
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = Constants.BEARER_KEY;

        new ServerResponse(ApiClass.getApiUrl(Constants.FBLOGIN)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, authorizationKey, context, "", new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        status = jObject.getString("Status");
                        message = jObject.getString("Message");
                        if (jObject.has("DataObject")) {
                            JSONObject dataObj = jObject.getJSONObject("DataObject");
                            String accessToken = dataObj.getString("access_token");
                            String refreshToken = dataObj.getString("refresh_token");
                            String expiresIn = dataObj.getString("expires_in");
                            //get current date for calculate expires time in
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            String formattedDate = df.format(c.getTime());
                            SessionStores.saveAccessToken(accessToken, context);
                            SessionStores.saveRefreshToken(refreshToken, context);
                            SessionStores.saveTimeExpiresIn(expiresIn, context);
                            SessionStores.saveLoginDate(formattedDate, context);
                            SessionStores.saveFbPresent(null, context);
                            if (Constants.USER_LOGIN == false) {
                                //user coming from sign up
                                Constants.userRegister = true;
                                progressBar.setVisibility(View.GONE);
                            }

                            Intent intentToHome = new Intent(context, TabHostFragments.class);
                            context.startActivity(intentToHome);
                            context.finish();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, "Please check your network connectivity", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, "Please check your network connectivity", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }
}
