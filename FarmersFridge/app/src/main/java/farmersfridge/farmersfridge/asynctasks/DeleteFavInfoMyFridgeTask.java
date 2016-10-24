package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.query.Delete;

import org.json.JSONException;
import org.json.JSONObject;

import farmersfridge.farmersfridge.database.models.Favourites;
import farmersfridge.farmersfridge.models.ApiCallParams;
import farmersfridge.farmersfridge.my_fridge.MyFridgeList;
import farmersfridge.farmersfridge.support.ServerResponse;
import farmersfridge.farmersfridge.support.VolleyResponseListener;
import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 9/21/2016.
 */
public class DeleteFavInfoMyFridgeTask {
    private Context context;
    private String result;
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;
    private String title;
    private MyFridgeList mContext;

    public DeleteFavInfoMyFridgeTask(Context context, ApiCallParams apiCallParams, MyFridgeList mContext, String title) {
        this.context = context;
        this.apiCallParams = apiCallParams;
        this.mContext = mContext;
        this.title = title;
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse(apiCallParams.getUrl()).getJSONObjectfromURL(ServerResponse.RequestType.DELETE, apiCallParams.getParams(), context, new VolleyResponseListener() {
            @Override
            public void onError(String message) { // Error response
                try {
                    jObject = new JSONObject(message.toString());
                    if (jObject != null) {
                        if(jObject.has("message")) {
                            Utils.ShowAlert(context, jObject.getString("message"));
                        }
                        if(jObject.has("result")) { // if result contains true in error message then UI updates
                            if (jObject.getString("result").equals("true")) {
                                Constants.favArray.remove(Constants.MYFRIDGE_INFO_FAV_POS);
                                mContext.initRecyclerView();
                                MyFridgeList.layForInfo.setVisibility(View.GONE);
                                new Delete().from(Favourites.class).where("favName = ?", new String[]{title}).execute(); // Delete from DB
                            }
                        }
                    } else {
                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponse(String response) {
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                    }
                    if(jObject.has("result")) {
                        if (jObject.getString("result").equals("true")) {

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
