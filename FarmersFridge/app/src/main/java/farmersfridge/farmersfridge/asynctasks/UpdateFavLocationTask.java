package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import farmersfridge.farmersfridge.adapter.LocationsMenuDetailAdapter;
import farmersfridge.farmersfridge.adapter.LocationsMenuHoriztlAdapter;
import farmersfridge.farmersfridge.database.models.Favourites;
import farmersfridge.farmersfridge.models.ApiCallParams;
import farmersfridge.farmersfridge.models.MenuItemModel;
import farmersfridge.farmersfridge.support.ServerResponse;
import farmersfridge.farmersfridge.support.VolleyResponseListener;
import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 8/12/2016.
 */
public class UpdateFavLocationTask {
    private Context context;
    private String result, from;
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;
    private View view, imageView;
    private List<MenuItemModel> menuList;
    private int position, arrayPosition;
    private String favName;

    public UpdateFavLocationTask(Context context, ApiCallParams apiCallParams, View view, View imageView, List<MenuItemModel> menuList, int position, int arrayPosition, String favName, String from) {
        this.context = context;
        this.apiCallParams = apiCallParams;
        this.view = view;
        this.imageView = imageView;
        this.menuList = menuList;
        this.position = position;
        this.arrayPosition = arrayPosition;
        this.favName = favName;
        this.from = from;
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
            public void onResponse(String response) { //Success response
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                    }
                    if(jObject.has("result")) {
                        if (jObject.getString("result").equals("true")) {
                            if(from.equals("list")) { //if update came from list UI updated on list screen
                                LocationsMenuHoriztlAdapter.favImgUpdated(view, imageView, favName);
                                Favourites item = new Favourites();
                                int count = item.getAllRow().size();
                                item.remoteId = count;
                                item.favName = LocationsMenuHoriztlAdapter.favName;
                                item.save();
                            } else { //else UI updated on detail screen
                                LocationsMenuDetailAdapter.favImgUpdated(view, imageView, favName);
                                Favourites item = new Favourites();
                                int count = item.getAllRow().size();
                                item.remoteId = count;
                                item.favName = LocationsMenuDetailAdapter.favName;
                                item.save();
                            }

                            //Values added in model class
                            MenuItemModel menu = menuList.get(position);
                            menu.setTitle(menu.getTitle());
                            menu.setDescription(menu.getDescription());
                            menu.setDescription_long(menu.getDescription_long());
                            menu.setImageUrl(menu.getImageUrl());
                            menu.setInfoPath(menu.getInfoPath());
                            menu.setCategory(menu.getCategory());
                            menu.setIsFavourite(true);
                            menuList.remove(position);
                            menuList.add(position, menu);
                            Constants.locMenuArray.add(arrayPosition, menuList); //updated fav item updates in array list
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
