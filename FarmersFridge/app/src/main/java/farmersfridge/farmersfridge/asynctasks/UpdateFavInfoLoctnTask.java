package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

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
public class UpdateFavInfoLoctnTask {
    private Context context;
    private String url, result, status = "", message = "";
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;
    private List<MenuItemModel> menuList;
    private String favName;
    private RecyclerView recyclerView;

    public UpdateFavInfoLoctnTask (Context context, ApiCallParams apiCallParams, RecyclerView recyclerView, List<MenuItemModel> menuList, String favName) {
        this.context = context;
        this.apiCallParams = apiCallParams;
        this.menuList = menuList;
        this.favName = favName;
        this.recyclerView = recyclerView;
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse(apiCallParams.getUrl()).getJSONObjectfromURL(ServerResponse.RequestType.POST, apiCallParams.getParams(), context, new VolleyResponseListener() {
            @Override
            public void onError(String message) { //Error response
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
                            //Favourites saved in favourite table of DB
                            Favourites item = new Favourites();
                            int count = item.getAllRow().size();
                            item.remoteId = count;
                            item.favName = favName;
                            item.save();

                            //Favourite items added in model class
                            MenuItemModel menu = menuList.get(Constants.INFO_ITEM_POS);
                            menu.setTitle(menu.getTitle());
                            menu.setDescription(menu.getDescription());
                            menu.setDescription_long(menu.getDescription_long());
                            menu.setImageUrl(menu.getImageUrl());
                            menu.setInfoPath(menu.getInfoPath());
                            menu.setCategory(menu.getCategory());
                            menu.setIsFavourite(true);
                            menuList.remove(Constants.INFO_ITEM_POS);
                            menuList.add(Constants.INFO_ITEM_POS, menu);
                            Constants.locMenuArray.add(LocationsMenuDetail.arrayPos, menuList); //changes in arraylist of favourites in constants
                            //Adapter of recyclerview updated
                            LocationsMenuDetailAdapter adapter = new LocationsMenuDetailAdapter(context, menuList);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            LocationsMenuDetail.imgForInfoFav.setImageResource(R.drawable.fav_selected_white_bg);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
