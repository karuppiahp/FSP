package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.widget.Toast;

import com.activeandroid.query.Delete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import farmersfridge.farmersfridge.database.models.MenuCategory;
import farmersfridge.farmersfridge.database.models.MenuItem;
import farmersfridge.farmersfridge.fragment.FragmentMain;
import farmersfridge.farmersfridge.menu.MenuMain;
import farmersfridge.farmersfridge.models.ApiCallParams;
import farmersfridge.farmersfridge.support.ServerResponse;
import farmersfridge.farmersfridge.support.VolleyResponseListener;
import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.SessionStores;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 9/14/2016.
 */
public class MenuXmlDateTask {
    private Context context;
    private MenuMain menuContext;
    private String result, category = "", daysToExpiry = "", price = "", sortKey = "", vendItemName = "", iconName = "", iconPath = "", nutritionName = "", nutritionPath = "";
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;

    public MenuXmlDateTask(Context context, MenuMain menuContext, ApiCallParams apiCallParams) {
        this.context = context;
        this.menuContext = menuContext;
        this.apiCallParams = apiCallParams;
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse(apiCallParams.getUrl()).getJSONObjectfromURL(ServerResponse.RequestType.GET, null, context, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
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
            public void onResponse(String response) {
                if (new SessionStores(context).getApiDate() != null && new SessionStores(context).getApiDate().length() > 0) {
                    if (new SessionStores(context).getApiDate().equals(Constants.API_DATE)) {
                        ((FragmentMain) context).setProgressBarGone();
                        menuContext.initRecyclerViews();
                    } else {
                        parserResponse(response);
                    }
                } else {
                    parserResponse(response);
                }
            }
        });
    }

    public void parserResponse(String response) {
        new SessionStores(context).saveApiDate(Constants.API_DATE);
        result = response.toString();
        try {
            jObject = new JSONObject(result);
            if (jObject != null) {
                JSONObject vendItemsObj = jObject.getJSONObject("vendItems");
                JSONArray itemsArray = vendItemsObj.getJSONArray("item");
                new Delete().from(MenuItem.class).execute();
                new Delete().from(MenuCategory.class).execute();
                for (int i = 0; i < itemsArray.length(); i++) { // Items JSON array
                    category = itemsArray.getJSONObject(i).getString("category");
                    daysToExpiry = itemsArray.getJSONObject(i).getString("daysToExpiry");
                    price = itemsArray.getJSONObject(i).getString("price");
                    sortKey = itemsArray.getJSONObject(i).getString("sortKey");
                    vendItemName = itemsArray.getJSONObject(i).getString("vendItemName");
                    //Object for icon
                    JSONObject iconObj = itemsArray.getJSONObject(i).getJSONObject("icon");
                    iconName = iconObj.getString("name");
                    iconPath = iconObj.getString("path");
                    // checks if nutrition present or not for nutrition chart
                    if (itemsArray.getJSONObject(i).has("nutrition")) {
                        JSONObject nutritionObj = itemsArray.getJSONObject(i).getJSONObject("nutrition");
                        nutritionName = nutritionObj.getString("name");
                        nutritionPath = nutritionObj.getString("path");
                    }

                    // Values added in model class
                    MenuItem item = new MenuItem();
                    item.remoteId = i;
                    item.category = category;
                    item.daysToExpiry = daysToExpiry;
                    item.price = price;
                    item.sortKey = sortKey;
                    item.vendName = vendItemName;
                    item.iconName = iconName;
                    item.iconPath = iconPath;
                    item.nutritionName = nutritionName;
                    item.nutritionPath = nutritionPath;
                    item.save();

                    // Categories are fetched and stored in array list
                    boolean categoryPresent = false;
                    MenuCategory categorymodel = new MenuCategory();
                    if (categorymodel.getAllRows().size() > 0) { // checks if array list size is not empty
                        for (int j = 0; j < categorymodel.getAllRows().size(); j++) {
                            // checks if category is already present or not
                            if (categorymodel.getAllRows().get(j).category.equals(category)) {
                                categoryPresent = true;
                                break;
                            }

                            // if length is reached final and category is not in array means then save it in array
                            if (j == categorymodel.getAllRows().size() - 1) {
                                if (categoryPresent == false) {
                                    categorymodel.category = category;
                                    categorymodel.save();
                                }
                            }
                        }
                    } else { // else the category added as first in array
                        categorymodel.category = category;
                        categorymodel.save();
                    }
                }

                //Menu items are placed in recycler view
                ((FragmentMain) context).setProgressBarGone();
                menuContext.initRecyclerViews();
            }
        } catch (JSONException e) {
            ((FragmentMain) context).setProgressBarGone();
            e.printStackTrace();
        }
    }
}
