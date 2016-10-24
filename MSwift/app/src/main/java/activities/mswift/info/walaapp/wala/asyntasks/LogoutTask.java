package activities.mswift.info.walaapp.wala.asyntasks;

import android.app.Activity;
import android.widget.Toast;

import java.util.Map;

import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 5/4/2016.
 */
public class LogoutTask {
    private Activity context;
    private Map<String, String> params;

    public LogoutTask(Activity context, Map<String, String> params) {
        this.context = context;
        this.params = params;
        ((TabHostFragments) context).progressBarVisible();
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse(Constants.TOKEN_API_LOGOUT).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, Constants.BASIC_KEY, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                ((TabHostFragments) context).progressBarGone();
                Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                ((TabHostFragments) context).progressBarGone();
                SessionStores.saveLoginDate(null, context);
                ((TabHostFragments) context).exitApp();
            }
        });
    }
}
