package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.fragment.FragmentMain;
import farmersfridge.farmersfridge.home.DealsFragment;
import farmersfridge.farmersfridge.login_register.ThanksNote;
import farmersfridge.farmersfridge.models.ApiCallParams;
import farmersfridge.farmersfridge.models.MenuModel;
import farmersfridge.farmersfridge.support.ServerResponse;
import farmersfridge.farmersfridge.support.VolleyResponseListener;
import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.SessionStores;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 6/16/2016.
 */
public class StartSessionTask {
    private Context context;
    private String url, result, status = "", message = "";
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;
    private FragmentManager fragmentManager;

    public StartSessionTask(Context context, ApiCallParams apiCallParams, FragmentManager fragmentManager) {
        this.context = context;
        this.apiCallParams = apiCallParams;
        this.fragmentManager = fragmentManager;
        ((FragmentMain) context).setProgressBarVisibile();
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse(apiCallParams.getUrl()).getJSONObjectfromURL(ServerResponse.RequestType.POST, apiCallParams.getParams(), context, new VolleyResponseListener() {
            @Override
            public void onError(String message) { //Error response
                ((FragmentMain) context).setProgressBarGone();
                try {
                    jObject = new JSONObject(message.toString());
                    if (jObject != null) {
                        if(jObject.has("message")) {
                            Utils.ShowAlert(context, jObject.getString("message"));
                        }
                    } else {
                        Toast.makeText(context, ""+message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponse(String response) { //Success response
                result = response.toString();
                ((FragmentMain) context).setProgressBarGone();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        if (jObject.has("access_token")) {
                            new SessionStores(context).saveAccessToken("" + jObject.getString("access_token"));
                            new SessionStores(context).saveLoginStatus("true");
                            //Favourites fetch api calls
                            SessionStores.MENU_MODEL = new MenuModel();
                            new FetchFavouritesTask(context, "Session", SessionStores.MENU_MODEL.getFavouritesRes());
                            ((FragmentMain) context).setLoginBtnGone(); //hide login button
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            if(Constants.REGISTER.length() > 0) { //if screen from register screen thanks note screen displayed
                                ThanksNote fragment = new ThanksNote();
                                fragmentTransaction.replace(R.id.realtabcontent, fragment);
                            } else { //else deals screen displayed
                                DealsFragment fragment = new DealsFragment();
                                fragmentTransaction.replace(R.id.realtabcontent, fragment);
                            }
                            fragmentTransaction.commit();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
