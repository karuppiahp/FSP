package farmersfridge.farmersfridge.asynctasks;

import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import farmersfridge.farmersfridge.SplashActivity;
import farmersfridge.farmersfridge.models.ApiCallParams;
import farmersfridge.farmersfridge.models.XmlParserModel;
import farmersfridge.farmersfridge.support.ServerResponse;
import farmersfridge.farmersfridge.support.VolleyResponseListener;
import farmersfridge.farmersfridge.utils.SessionStores;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 6/27/2016.
 */
public class MenuTask {
    private SplashActivity context;
    private String url, result, status = "", message = "";
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;

    public MenuTask(SplashActivity context, ApiCallParams apiCallParams) {
        this.context = context;
        this.apiCallParams = apiCallParams;
        SplashActivity.progressBar.setVisibility(View.VISIBLE);
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse(apiCallParams.getUrl()).getJSONObjectfromURL(ServerResponse.RequestType.GET, null, context, new VolleyResponseListener() {
            @Override
            public void onError(String message) { // Error response
                SplashActivity.progressBar.setVisibility(View.GONE);
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
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponse(String response) { //Success response
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        if (jObject.has("url")) { //if XML url available for menu then MenuXmlTask calls
                            String url = jObject.getString("url");
                            SessionStores.XML_PARSER_MODEL = new XmlParserModel(url);
                            new MenuXmlTask(context, SessionStores.XML_PARSER_MODEL.xmlLinkParse());
                        }
                    }
                } catch (JSONException e) {
                    SplashActivity.progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        });
    }
}
