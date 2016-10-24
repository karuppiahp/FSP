package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import farmersfridge.farmersfridge.fragment.FragmentMain;
import farmersfridge.farmersfridge.models.ApiCallParams;
import farmersfridge.farmersfridge.models.XmlParserModel;
import farmersfridge.farmersfridge.settings.FAQ;
import farmersfridge.farmersfridge.support.ServerResponse;
import farmersfridge.farmersfridge.support.VolleyResponseListener;
import farmersfridge.farmersfridge.utils.SessionStores;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 6/22/2016.
 */
public class FaqTask {
    private Context context;
    private String url, result, status = "", message = "";
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;
    private FragmentManager fragmentManager;
    private FAQ faqContext;

    public FaqTask(Context context, FAQ faqContext, ApiCallParams apiCallParams, FragmentManager fragmentManager) {
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
            public void onResponse(String response) { // Success response
                result = response.toString();
                try {
                    ((FragmentMain) context).setProgressBarGone();
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        if (jObject.has("url")) { // if url presents then FAQ XML task calls
                            String url = jObject.getString("url");
                            SessionStores.XML_PARSER_MODEL = new XmlParserModel(url);
                            new FaqXmlTask(context, faqContext, SessionStores.XML_PARSER_MODEL.xmlLinkParse(), fragmentManager);
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
