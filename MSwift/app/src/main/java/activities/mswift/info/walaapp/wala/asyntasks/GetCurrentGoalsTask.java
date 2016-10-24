package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Map;

import activities.mswift.info.walaapp.wala.adapter.ViewPagerAdapterGoals;
import activities.mswift.info.walaapp.wala.home.GoalsActivity;
import activities.mswift.info.walaapp.wala.model.CurrentGoalModel;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 4/26/2016.
 */
public class GetCurrentGoalsTask {
    private Context context;
    private JSONObject jObject = null;
    private JSONArray jsonArray;
    private Map<String, String> params;
    private String authorizationKey, result = "", status = "", message = "", mode = "", name = "", daysLeft = "", type = "", currentValue = "", goalValue = "", arraySize;
    private ArrayList<CurrentGoalModel> goalsArray = new ArrayList<>();
    private ViewPagerAdapterGoals pagerAdapter;
    private ViewPager viewPager;
    private GoalsActivity activity;

    public GetCurrentGoalsTask(Context context, GoalsActivity activity, Map<String, String> params, ArrayList<CurrentGoalModel> goalsArray, ViewPagerAdapterGoals pagerAdapter, ViewPager viewPager) {
        this.context = context;
        this.params = params;
        this.goalsArray = goalsArray;
        this.pagerAdapter = pagerAdapter;
        this.viewPager = viewPager;
        this.activity = activity;
        goalsArray.clear();
        ((TabHostFragments) context).progressBarVisible();
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getBackendApiUrl(Constants.CURRENTGOAL_BACK)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                activity.overlayView(false);
                ((TabHostFragments) context).progressBarGone();
                Toast.makeText(context, "Please check your network availability", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        if (jObject.has("status")) {
                            status = jObject.getString("status");
                            message = jObject.getString("message");
                            if (status.equals("success")) {
                                jsonArray = jObject.getJSONArray("goals");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    activity.overlayView(true);
                                    mode = jsonArray.getJSONObject(i).getString("mode");
                                    name = jsonArray.getJSONObject(i).getString("name");
                                    daysLeft = jsonArray.getJSONObject(i).getString("days_left");
                                    type = jsonArray.getJSONObject(i).getString("type");
                                    currentValue = jsonArray.getJSONObject(i).getString("current_value");
                                    goalValue = jsonArray.getJSONObject(i).getString("goal_value");

                                    BigDecimal myNumber = new BigDecimal(goalValue);
                                    double myDouble = myNumber.doubleValue();
                                    NumberFormat formatter = new DecimalFormat("#################");
                                    formatter.setMaximumFractionDigits(4);
                                    String currentGoalValue = (formatter.format(myDouble));

                                    CurrentGoalModel goalModel = new CurrentGoalModel();
                                    goalModel.setMode(mode);
                                    goalModel.setName(name);
                                    goalModel.setDaysLeft(daysLeft);
                                    goalModel.setType(type);
                                    goalModel.setCurrentValue(currentValue);
                                    goalModel.setGoalValue(currentGoalValue);
                                    goalsArray.add(goalModel);
                                }
                                if (goalsArray.size() <= 0) {
                                    arraySize = "0";

                                } else {
                                    arraySize = "1";
                                }
                                Map<String, String> paramsHistory = GoalsActivity.goalsHistory();
                                new GoalsHistoryTask(context, GoalsActivity.listViewForHistory, paramsHistory, arraySize);
                            } else {
                                activity.overlayView(false);
                                ((TabHostFragments) context).progressBarGone();
                            }
                            pagerAdapter = new ViewPagerAdapterGoals(context, goalsArray.size());
                            viewPager.setAdapter(pagerAdapter);
                        } else {
                            activity.overlayView(false);
                            ((TabHostFragments) context).progressBarGone();
                        }
                    }
                } catch (JSONException e) {
                    activity.overlayView(false);
                    ((TabHostFragments) context).progressBarGone();
                    e.printStackTrace();
                }
            }
        });
    }
}
