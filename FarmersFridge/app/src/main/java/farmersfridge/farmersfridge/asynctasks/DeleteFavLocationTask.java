package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.query.Delete;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import farmersfridge.farmersfridge.adapter.LocationsMenuDetailAdapter;
import farmersfridge.farmersfridge.adapter.LocationsMenuHoriztlAdapter;
import farmersfridge.farmersfridge.adapter.LocationsMenuListAdapter;
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
public class DeleteFavLocationTask {
    private Context context;
    private String result, from;
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;
    private View view, imageView;
    private List<MenuItemModel> menuList;
    private int position, arrayPosition;
    private String favName;

    public DeleteFavLocationTask(Context context, ApiCallParams apiCallParams, View view, View imageView, List<MenuItemModel> menuList, int position, int arrayPosition, String favName, String from) {
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
        new ServerResponse(apiCallParams.getUrl()).getJSONObjectfromURL(ServerResponse.RequestType.DELETE, apiCallParams.getParams(), context, new VolleyResponseListener() {
            @Override
            public void onError(String message) { // Error response
                try {
                    jObject = new JSONObject(message.toString());
                    if (jObject != null) {
                        if(jObject.has("message")) {
                            Utils.ShowAlert(context, jObject.getString("message"));
                        }
                        if(jObject.has("result")) {
                            if (jObject.getString("result").equals("true")) { // if result contains true in error message then UI updates
                                if(from.equals("list")) { // UI update on Menu list screen
                                    LocationsMenuHoriztlAdapter.favImgUpdated(view, imageView, favName);
                                    MenuItemModel menuItemModel = menuList.get(position);
                                    menuItemModel.setIsFavourite(false);
                                    new Delete().from(Favourites.class).where("favName = ?", new String[]{LocationsMenuHoriztlAdapter.favName}).execute();
                                } else { // UI update on Menu Detail screen
                                    LocationsMenuDetailAdapter.favImgUpdated(view, imageView, favName);
                                    MenuItemModel menuItemModel = menuList.get(position);
                                    menuItemModel.setIsFavourite(false);
                                    new Delete().from(Favourites.class).where("favName = ?", new String[]{LocationsMenuDetailAdapter.favName}).execute();
                                }
                                //Remove item from model class
                                MenuItemModel menu = menuList.get(position);
                                menu.setTitle(menu.getTitle());
                                menu.setDescription(menu.getDescription());
                                menu.setDescription_long(menu.getDescription_long());
                                menu.setImageUrl(menu.getImageUrl());
                                menu.setInfoPath(menu.getInfoPath());
                                menu.setCategory(menu.getCategory());
                                menu.setIsFavourite(false);
                                menuList.remove(position);
                                menuList.add(position, menu);
                                Constants.locMenuArray.add(arrayPosition, menuList);
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
                            LocationsMenuListAdapter.favImgUpdated(view);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
