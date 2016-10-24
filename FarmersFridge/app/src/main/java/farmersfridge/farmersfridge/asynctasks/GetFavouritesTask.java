package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.query.Delete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import farmersfridge.farmersfridge.SplashActivity;
import farmersfridge.farmersfridge.database.models.Favourites;
import farmersfridge.farmersfridge.models.ApiCallParams;
import farmersfridge.farmersfridge.models.MenuModel;
import farmersfridge.farmersfridge.support.ServerResponse;
import farmersfridge.farmersfridge.support.VolleyResponseListener;
import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.SessionStores;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 7/2/2016.
 */
public class GetFavouritesTask {
    private Context context;
    private String url, result, status = "", message = "";
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;

    public GetFavouritesTask(Context context, ApiCallParams apiCallParams) {
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
            public void onResponse(String response) { // Success response
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        JSONArray favArray = jObject.getJSONArray("favorites");
                        if (favArray.length() > 0) {
                            Favourites favDb = new Favourites();
                            // delete favourites table if it contains rows
                            if (favDb.getAllRow().size() > 0) {
                                new Delete().from(Favourites.class).execute();
                            }
                        }
                        for (int i = 0; i < favArray.length(); i++) {
                            String fav = favArray.getString(i);
                            //Favourites added in DB
                            Favourites item = new Favourites();
                            item.remoteId = i;
                            item.favName = fav;
                            item.save();
                            Constants.FAV_ENTERS = true;
                        }

                        // Menu api calls
                        SessionStores.MENU_MODEL = new MenuModel();
                        new MenuTask((SplashActivity) context, SessionStores.MENU_MODEL.menu());
                    }
                } catch (JSONException e) {
                    SplashActivity.progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        });
    }
}
