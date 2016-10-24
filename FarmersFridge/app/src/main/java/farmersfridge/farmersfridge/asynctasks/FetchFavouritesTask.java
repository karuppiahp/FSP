package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.widget.Toast;

import com.activeandroid.query.Delete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import farmersfridge.farmersfridge.database.models.Favourites;
import farmersfridge.farmersfridge.fragment.FragmentMain;
import farmersfridge.farmersfridge.models.ApiCallParams;
import farmersfridge.farmersfridge.my_fridge.MyFridge;
import farmersfridge.farmersfridge.support.ServerResponse;
import farmersfridge.farmersfridge.support.VolleyResponseListener;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 8/3/2016.
 */
public class FetchFavouritesTask {
    private Context context;
    private String result, from = "";
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;
    private MyFridge myFridgeContext;

    public FetchFavouritesTask(Context context, MyFridge myFridgeContext, ApiCallParams apiCallParams) {
        this.context = context;
        this.apiCallParams = apiCallParams;
        this.myFridgeContext = myFridgeContext;
        ResponseTask();
    }

    public FetchFavouritesTask(Context context, String from, ApiCallParams apiCallParams) {
        this.context = context;
        this.apiCallParams = apiCallParams;
        this.from = from;
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
                    new Delete().from(Favourites.class).execute();
                    if (jObject != null) {
                        JSONArray favArray = jObject.getJSONArray("favorites");
                        for(int i=0; i<favArray.length(); i++) {
                            String fav = favArray.getString(i);
                            //Favourites added in DB
                            Favourites item = new Favourites();
                            item.remoteId = i;
                            item.favName = fav;
                            item.save();
                        }
                    }

                    if(from.length() == 0) {
                        myFridgeContext.updateUi();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
