package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import farmersfridge.farmersfridge.fragment.FragmentMain;
import farmersfridge.farmersfridge.models.ApiCallParams;
import farmersfridge.farmersfridge.support.ServerResponse;
import farmersfridge.farmersfridge.support.VolleyResponseListener;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 6/22/2016.
 */
public class FeedbackTask {
    private Context context;
    private String url, result, status = "", message = "";
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;
    private FragmentManager fragmentManager;

    public FeedbackTask(Context context, ApiCallParams apiCallParams, FragmentManager fragmentManager) {
        this.context = context;
        this.apiCallParams = apiCallParams;
        this.fragmentManager = fragmentManager;
        ((FragmentMain) context).setProgressBarVisibile();
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse(apiCallParams.getUrl()).getJSONObjectfromURL(ServerResponse.RequestType.POST, apiCallParams.getParams(), context, new VolleyResponseListener() {
            @Override
            public void onError(String message) { // Error response
                ((FragmentMain) context).setProgressBarGone();
                try {
                    jObject = new JSONObject(message.toString());
                    if (jObject != null) {
                        if (jObject.has("message")) {
                            Utils.ShowAlert(context, jObject.getString("message"));
                        }
                    } else {
                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    ((FragmentMain) context).setProgressBarGone();
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponse(String response) { // Success response
                result = response.toString();
                try {
                    ((FragmentMain) context).setProgressBarGone();
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        if (jObject.has("result")) {
                            if (jObject.getString("result").equals("true")) { // if feedback response result true alert displayed
                                Utils.ShowAlert(context, "Thanks for your Feedback");
                            }
                        }
                    }
                } catch (JSONException e) {
                    ((FragmentMain) context).setProgressBarGone();
                    e.printStackTrace();
                }
            }
        });
    }
}
