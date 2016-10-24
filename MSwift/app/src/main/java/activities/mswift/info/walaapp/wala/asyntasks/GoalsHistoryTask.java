package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import activities.mswift.info.walaapp.wala.adapter.GoalsHistoryAdapter;
import activities.mswift.info.walaapp.wala.home.GoalsActivity;
import activities.mswift.info.walaapp.wala.model.GoalsHistoryModel;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 4/28/2016.
 */
public class GoalsHistoryTask {
    private Context context;
    private Map<String, String> params;
    private JSONObject jObject = null;
    private String authorizationKey, result = "", status = "", message = "", arraySize;
    public ListAdapter adapter;
    private ListView listView;


    public GoalsHistoryTask(Context context, ListView listView, Map<String, String> params, String arraySize) {
        this.context = context;
        this.listView = listView;
        this.params = params;
        this.arraySize = arraySize;
        GoalsActivity.goalsList.clear();
        ResponseTask();

    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getBackendApiUrl(Constants.GOALSHISTORY)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                ((TabHostFragments) context).progressBarGone();
                Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                GoalsActivity.swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(String response) {
                result = response.toString();
                try {
                    ((TabHostFragments) context).progressBarGone();
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        if (jObject.has("status")) {
                            status = jObject.getString("status");
                            if (status.equals("success")) {
                                JSONArray goalsArray = jObject.getJSONArray("goals");
                                GoalsActivity.goalsList.clear();
                                for (int i = 0; i < goalsArray.length(); i++) {

                                    JSONObject jsobj = goalsArray.getJSONObject(i);

                                    String goalsName = jsobj.getString("name");
                                    String goalsPoint = jsobj.getString("points");
                                    String createdDate = jsobj.getString("created");
                                    String goalsType = jsobj.getString("type");
                                    String goalsTarget = jsobj.getString("target");

                                    GoalsHistoryModel goalsModel = new GoalsHistoryModel();
                                    goalsModel.setGoalsPoints(goalsPoint);
                                    goalsModel.setGoalsName(goalsName);
                                    goalsModel.setGoalsDate(createdDate);
                                    goalsModel.setGoalsType(goalsType);
                                    goalsModel.setGoalsTarget(goalsTarget);

                                    GoalsActivity.goalsList.add(goalsModel);
                                }

                                if (arraySize.equals("0")) {
                                    Utils.ShowAlert(context, "Gaols record not found");
                                } else {
                                    adapter = new GoalsHistoryAdapter(context, GoalsActivity.goalsList);
                                    listView.setAdapter(adapter);
                                    Utils.setListViewHeightBasedOnChildren(listView);
                                }
                            } else {
                                message = jObject.getString("message");
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    GoalsActivity.swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    ((TabHostFragments) context).progressBarGone();
                    e.printStackTrace();
                    GoalsActivity.swipeRefreshLayout.setRefreshing(false);
                }

            }


        });

    }
}
