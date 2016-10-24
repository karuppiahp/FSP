package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import farmersfridge.farmersfridge.fragment.FragmentMain;
import farmersfridge.farmersfridge.models.ApiCallParams;
import farmersfridge.farmersfridge.models.SettingsMainModel;
import farmersfridge.farmersfridge.settings.FAQ;
import farmersfridge.farmersfridge.support.ServerResponse;
import farmersfridge.farmersfridge.support.VolleyResponseListener;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 6/22/2016.
 */
public class FaqXmlTask {
    private Context context;
    private String url, result, status = "", message = "";
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;
    private FragmentManager fragmentManager;
    private FAQ faqContext;

    public FaqXmlTask(Context context, FAQ faqContext, ApiCallParams apiCallParams, FragmentManager fragmentManager) {
        this.context = context;
        this.apiCallParams = apiCallParams;
        this.fragmentManager = fragmentManager;
        this.faqContext = faqContext;
        ((FragmentMain) context).setProgressBarVisibile();
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse(apiCallParams.getUrl()).getJSONObjectfromURL(ServerResponse.RequestType.GET, null, context, new VolleyResponseListener() {
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
            public void onResponse(String response) { // Success Response
                result = response.toString();
                try {
                    ((FragmentMain) context).setProgressBarGone();
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        if (jObject.has("questions")) {
                            JSONObject quesObj = jObject.getJSONObject("questions");
                            JSONArray quesArray = quesObj.getJSONArray("question");
                            for (int i = 0; i < quesArray.length(); i++) {
                                String ques = quesArray.getJSONObject(i).getString("content");
                                String ans = quesArray.getJSONObject(i).getString("answer");

                                //Ques and ans set in model class
                                SettingsMainModel settingsModel = new SettingsMainModel();
                                settingsModel.setQues(ques);
                                settingsModel.setAns(ans);
                                FAQ.faqArray.add(settingsModel);
                                //Listview items are set
                                faqContext.setListItems();
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
