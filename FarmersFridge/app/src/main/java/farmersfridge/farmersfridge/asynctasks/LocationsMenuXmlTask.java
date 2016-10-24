package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.fragment.FragmentMain;
import farmersfridge.farmersfridge.map.LocationsMenu;
import farmersfridge.farmersfridge.models.ApiCallParams;
import farmersfridge.farmersfridge.models.CathegoryModel;
import farmersfridge.farmersfridge.models.MenuItemModel;
import farmersfridge.farmersfridge.support.ServerResponse;
import farmersfridge.farmersfridge.support.VolleyResponseListener;
import farmersfridge.farmersfridge.utils.MenuItemFetches;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 8/11/2016.
 */
public class LocationsMenuXmlTask {
    private Context context;
    private String result, category = "", daysToExpiry = "", price = "", sortKey = "", vendItemName = "", iconName = "", iconPath = "", nutritionName = "", nutritionPath = "";
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;
    private List<MenuItemModel> locMenuArray = new ArrayList<>();
    private List<CathegoryModel> locCategoryArray = new ArrayList<>();
    private FragmentManager fragmentManager;

    public LocationsMenuXmlTask(Context context, ApiCallParams apiCallParams, FragmentManager fragmentManager) {
        this.context = context;
        this.apiCallParams = apiCallParams;
        this.fragmentManager = fragmentManager;
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
                        JSONObject vendItemsObj = jObject.getJSONObject("vendItems");
                        //Array for items
                        JSONArray itemsArray = vendItemsObj.getJSONArray("item");
                        for (int i = 0; i < itemsArray.length(); i++) {
                            category = itemsArray.getJSONObject(i).getString("category");
                            daysToExpiry = itemsArray.getJSONObject(i).getString("daysToExpiry");
                            price = itemsArray.getJSONObject(i).getString("price");
                            sortKey = itemsArray.getJSONObject(i).getString("sortKey");
                            vendItemName = itemsArray.getJSONObject(i).getString("vendItemName");
                            //Object for icon
                            JSONObject iconObj = itemsArray.getJSONObject(i).getJSONObject("icon");
                            iconName = iconObj.getString("name");
                            iconPath = iconObj.getString("path");
                            if (itemsArray.getJSONObject(i).has("nutrition")) { // checks if nutrition present or not for nutrition chart
                                JSONObject nutritionObj = itemsArray.getJSONObject(i).getJSONObject("nutrition");
                                nutritionName = nutritionObj.getString("name");
                                nutritionPath = nutritionObj.getString("path");
                            }

                            // Values added in model class
                            MenuItemModel menu = new MenuItemModel();
                            menu.setTitle(vendItemName);
                            menu.setDescription(vendItemName);
                            menu.setDescription_long(vendItemName + " " + vendItemName);
                            menu.setImageUrl("http://d3uz0b82u39oll.cloudfront.net/" + iconPath + "/" + iconName);
                            menu.setInfoPath("http://d3uz0b82u39oll.cloudfront.net/" + nutritionPath + "/" + nutritionName);
                            menu.setCategory(category);
                            menu.setPrice(price);
                            locMenuArray.add(menu); // Model class added to locations array

                            // Categories are fetched and stored in array list
                            boolean categoryPresent = false;
                            if (locCategoryArray.size() > 0) { // checks if array list size is not empty
                                for (int j = 0; j < locCategoryArray.size(); j++) {
                                    // checks if category is already present or not
                                    if (locCategoryArray.get(j).getName().equals(category)) {
                                        categoryPresent = true;
                                        break;
                                    }

                                    // if length is reached final and category is not in array means then save it in array
                                    if (j == locCategoryArray.size() - 1) {
                                        if (categoryPresent == false) {
                                            CathegoryModel categoryModel = new CathegoryModel();
                                            categoryModel.setName(category);
                                            locCategoryArray.add(categoryModel);
                                        }
                                    }
                                }
                            } else { // else the category added as first in array
                                CathegoryModel categoryModel = new CathegoryModel();
                                categoryModel.setName(category);
                                locCategoryArray.add(categoryModel);
                            }
                        }

                        //Separate the menu items according to the categories
                        MenuItemFetches.getLocCathegory(locCategoryArray);
                        MenuItemFetches.locMenuItems(locMenuArray);

                        //Fragment replaces with LocationsMenu fragment
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        LocationsMenu fragment = new LocationsMenu();
                        fragmentTransaction.replace(R.id.realtabcontent, fragment);
                        fragmentTransaction.commit();
                        ((FragmentMain) context).setProgressBarGone();

                    }
                } catch (JSONException e) {
                    ((FragmentMain) context).setProgressBarGone();
                    e.printStackTrace();
                }
            }
        });
    }
}
