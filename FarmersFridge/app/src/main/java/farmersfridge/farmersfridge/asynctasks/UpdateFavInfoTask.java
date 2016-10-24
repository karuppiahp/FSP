package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.adapter.MenuDetailAdapter;
import farmersfridge.farmersfridge.database.models.Favourites;
import farmersfridge.farmersfridge.database.models.MenuItem;
import farmersfridge.farmersfridge.menu.MenuDetail;
import farmersfridge.farmersfridge.models.ApiCallParams;
import farmersfridge.farmersfridge.support.ServerResponse;
import farmersfridge.farmersfridge.support.VolleyResponseListener;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 9/12/2016.
 */
public class UpdateFavInfoTask {
    private Context context;
    private String result, category;
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;
    private String name;
    private RecyclerView recMenuDetail;

    public UpdateFavInfoTask(Context context, ApiCallParams apiCallParams, RecyclerView recMenuDetail, String category, String name) {
        this.context = context;
        this.apiCallParams = apiCallParams;
        this.name = name;
        this.recMenuDetail = recMenuDetail;
        this.category = category;
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse(apiCallParams.getUrl()).getJSONObjectfromURL(ServerResponse.RequestType.POST, apiCallParams.getParams(), context, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
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
            public void onResponse(String response) {
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                    }
                    if(jObject.has("result")) {
                        if (jObject.getString("result").equals("true")) {
                            //Favourites added in favourites table DB
                            Favourites item = new Favourites();
                            int count = item.getAllRow().size();
                            item.remoteId = count;
                            item.favName = name;
                            item.save();
                            //Recycler view updated favourite view
                            RecyclerView.Adapter menuAdapter = new MenuDetailAdapter(context, MenuItem.getAllItemsByCat(category));
                            recMenuDetail.setAdapter(menuAdapter);
                            menuAdapter.notifyDataSetChanged();
                            MenuDetail.infoFavPresent = true;
                            MenuDetail.imgForInfoFav.setImageResource(R.drawable.fav_selected_white_bg);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}