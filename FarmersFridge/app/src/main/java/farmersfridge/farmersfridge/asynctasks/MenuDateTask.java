package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import farmersfridge.farmersfridge.fragment.FragmentMain;
import farmersfridge.farmersfridge.menu.MenuMain;
import farmersfridge.farmersfridge.models.ApiCallParams;
import farmersfridge.farmersfridge.models.XmlParserModel;
import farmersfridge.farmersfridge.support.ServerResponse;
import farmersfridge.farmersfridge.support.VolleyResponseListener;
import farmersfridge.farmersfridge.utils.SessionStores;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 9/14/2016.
 */
public class MenuDateTask {
    private Context context;
    private MenuMain menuContext;
    private String result;
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;

    public MenuDateTask(Context context, MenuMain menuContext, ApiCallParams apiCallParams) {
        this.context = context;
        this.menuContext = menuContext;
        this.apiCallParams = apiCallParams;
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
                        if(jObject.has("url")) { //if XML url available then MenuXmlDateTask calls
                            String url = jObject.getString("url");
                            SessionStores.XML_PARSER_MODEL = new XmlParserModel(url);
                            new MenuXmlDateTask(context, menuContext, SessionStores.XML_PARSER_MODEL.xmlLinkParse());
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
