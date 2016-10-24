package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.activeandroid.query.Delete;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.adapter.LocationsMenuDetailAdapter;
import farmersfridge.farmersfridge.database.models.Favourites;
import farmersfridge.farmersfridge.map.LocationsMenuDetail;
import farmersfridge.farmersfridge.models.ApiCallParams;
import farmersfridge.farmersfridge.models.MenuItemModel;
import farmersfridge.farmersfridge.support.ServerResponse;
import farmersfridge.farmersfridge.support.VolleyResponseListener;
import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 9/16/2016.
 */
public class DeleteFavInfoLoctnTask {
    private Context context;
    private String url, result, status = "", message = "";
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;
    private List<MenuItemModel> menuList;
    private String favName;
    private RecyclerView recyclerView;

    public DeleteFavInfoLoctnTask(Context context, ApiCallParams apiCallParams, RecyclerView recyclerView, List<MenuItemModel> menuList, String favName) {
        this.context = context;
        this.apiCallParams = apiCallParams;
        this.menuList = menuList;
        this.favName = favName;
        this.recyclerView = recyclerView;
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse(apiCallParams.getUrl()).getJSONObjectfromURL(ServerResponse.RequestType.DELETE, apiCallParams.getParams(), context, new VolleyResponseListener() {
            @Override
            public void onError(String message) { //Error response
                try {
                    jObject = new JSONObject(message.toString());
                    if (jObject != null) {
                        if(jObject.has("message")) {
                            Utils.ShowAlert(context, jObject.getString("message"));
                        }
                        if(jObject.has("result")) {
                            if (jObject.getString("result").equals("true")) { // if result contains true in error message then UI updates
                                MenuItemModel menuItemModel = menuList.get(Constants.INFO_ITEM_POS);
                                menuItemModel.setIsFavourite(false);
                                new Delete().from(Favourites.class).where("favName = ?", new String[]{favName}).execute(); // Delete from DB

                                //Removed the item from model class
                                MenuItemModel menu = menuList.get(Constants.INFO_ITEM_POS);
                                menu.setTitle(menu.getTitle());
                                menu.setDescription(menu.getDescription());
                                menu.setDescription_long(menu.getDescription_long());
                                menu.setImageUrl(menu.getImageUrl());
                                menu.setInfoPath(menu.getInfoPath());
                                menu.setCategory(menu.getCategory());
                                menu.setIsFavourite(false);
                                menuList.remove(Constants.INFO_ITEM_POS); // removed from model
                                menuList.add(Constants.INFO_ITEM_POS, menu);
                                Constants.locMenuArray.add(LocationsMenuDetail.arrayPos, menuList);
                                //Refresh the adapter class
                                LocationsMenuDetailAdapter adapter = new LocationsMenuDetailAdapter(context, menuList);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                LocationsMenuDetail.imgForInfoFav.setImageResource(R.drawable.fav_unselected_white_bg);
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
