package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import activities.mswift.info.walaapp.wala.more.EditPersonalInfo;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 1/12/2016.
 */
public class UpdatePersonalInfoTask {
    private Context context;
    private Map<String, String> params;
    private JSONObject jObject                  = null;
    private String     authorizationKey, result = "", status = "", message = "";
    private FragmentManager fragmentManager;

    public UpdatePersonalInfoTask(Context context, Map<String, String> params, FragmentManager fragmentManager) {
        this.context = context;
        this.params = params;
        this.fragmentManager = fragmentManager;
        ((TabHostFragments) context).progressBarVisible();
        ResponseTask();
    }
    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getApiUrl(Constants.EDITPERSONAL_INFO)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                ((TabHostFragments) context).progressBarGone();
                Toast.makeText(context, ""+message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        if (jObject.has("Status")) {
                            message = jObject.getString("Message");
                            Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                            /**Move to another fragment*/
                            String country = EditPersonalInfo.txtForCountry.getText().toString();
                            String city = EditPersonalInfo.editTxtForCity.getText().toString();
                            SessionStores.saveCountry(country, context);
                            SessionStores.saveCity(city, context);

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("FirstName", EditPersonalInfo.editTxtForFirstName.getText().toString());
                            params.put("MiddleName", EditPersonalInfo.editTxtForMiddleName.getText().toString());
                            params.put("LastName", EditPersonalInfo.editTxtForLastName.getText().toString());
                            params.put("DateOfBirth", Utils.dateConvertPersonalInfo(EditPersonalInfo.txtForDob.getText().toString()));
                            params.put("Gender", "Male");
                            params.put("DisplayName", SessionStores.getUserName(context));
                            params.put("Email", SessionStores.getUserEmail(context));
                            params.put("PhoneNumber", EditPersonalInfo.editTxtForPhoneNumber.getText().toString());
                            params.put("Country", EditPersonalInfo.txtForCountry.getText().toString());
                            params.put("City", EditPersonalInfo.editTxtForCity.getText().toString());
                            params.put("FbConnect", "0");
                            new UpdatePersonalInfoBackendTask(context, params, fragmentManager);
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
