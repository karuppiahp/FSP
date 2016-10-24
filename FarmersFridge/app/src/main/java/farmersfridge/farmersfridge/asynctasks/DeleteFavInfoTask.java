package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.activeandroid.query.Delete;

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
public class DeleteFavInfoTask {
    private Context context;
    private String result, category;
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;
    private String name;
    private RecyclerView recMenuDetail;

    public DeleteFavInfoTask(Context context, ApiCallParams apiCallParams, RecyclerView recMenuDetail, String category, String name) {
        this.context = context;
        this.apiCallParams = apiCallParams;
        this.name = name;
        this.recMenuDetail = recMenuDetail;
        this.category = category;
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
                                new Delete().from(Favourites.class).where("favName = ?", new String[]{name}).execute(); // Delete from DB
                                RecyclerView.Adapter menuAdapter = new MenuDetailAdapter(context, MenuItem.getAllItemsByCat(category));
                                recMenuDetail.setAdapter(menuAdapter);
                                menuAdapter.notifyDataSetChanged(); // refresh recycler view
                                MenuDetail.infoFavPresent = false;
                                MenuDetail.imgForInfoFav.setImageResource(R.drawable.fav_unselected_white_bg);
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
