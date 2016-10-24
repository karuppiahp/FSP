package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.activeandroid.query.Delete;

import org.json.JSONException;
import org.json.JSONObject;

import farmersfridge.farmersfridge.database.models.Favourites;
import farmersfridge.farmersfridge.models.ApiCallParams;
import farmersfridge.farmersfridge.support.ServerResponse;
import farmersfridge.farmersfridge.support.VolleyResponseListener;
import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 8/1/2016.
 */
public class DeleteFavMyFridgeTask {
    private Context context;
    private String result;
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;
    private RecyclerView.Adapter adapter;
    private int position;
    private String title;

    public DeleteFavMyFridgeTask(Context context, ApiCallParams apiCallParams, RecyclerView.Adapter adapter, String title, int position) {
        this.context = context;
        this.apiCallParams = apiCallParams;
        this.adapter = adapter;
        this.position = position;
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
                        if (jObject.has("message")) {
                            Utils.ShowAlert(context, jObject.getString("message"));
                        }
                        if (jObject.has("result")) {
                            if (jObject.getString("result").equals("true")) {
                                Constants.favArray.remove(position);
                                adapter.notifyItemRemoved(position);
                                adapter.notifyItemRangeChanged(position, Constants.favArray.size()); // Recycler view refreshed
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
            public void onResponse(String response) { // Success response
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                    }
                    if (jObject.has("result")) {
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
