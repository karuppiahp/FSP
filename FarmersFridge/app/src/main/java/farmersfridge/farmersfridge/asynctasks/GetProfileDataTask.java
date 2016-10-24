package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import farmersfridge.farmersfridge.models.ApiCallParams;
import farmersfridge.farmersfridge.settings.ProfileEditOrRegister;
import farmersfridge.farmersfridge.support.ServerResponse;
import farmersfridge.farmersfridge.support.VolleyResponseListener;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 6/17/2016.
 */
public class GetProfileDataTask {
    private Context context;
    private String result;
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;
    private FragmentManager fragmentManager;
    private ProfileEditOrRegister profileContext;

    public GetProfileDataTask(Context context, ProfileEditOrRegister profileContext, ApiCallParams apiCallParams, FragmentManager fragmentManager) {
        this.context = context;
        this.apiCallParams = apiCallParams;
        this.fragmentManager = fragmentManager;
        this.profileContext = profileContext;
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse(apiCallParams.getUrl()).getJSONObjectfromURL(ServerResponse.RequestType.GET, null, context, new VolleyResponseListener() {
            @Override
            public void onError(String message) { // Error response
                try {
                    jObject = new JSONObject(message.toString());
                    if (jObject != null) {
                        if(jObject.has("message")) {
                            Utils.ShowAlert(context, jObject.getString("message"));
                        }
                    } else {
                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponse(String response) { // Success response
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        if (jObject.has("first_name")) {
                            String firstName = jObject.getString("first_name");
                            String lastName = jObject.getString("last_name");
                            String phNo = jObject.getString("phone_number");
                            String email = jObject.getString("email");
                            // profile details are set by called method on ProfileEditOrRegister class
                            profileContext.setProfileDate(firstName, lastName, phNo, email);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
