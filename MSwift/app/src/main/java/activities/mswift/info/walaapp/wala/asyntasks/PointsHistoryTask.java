package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import activities.mswift.info.walaapp.wala.adapter.PointsHistoryAdapter;
import activities.mswift.info.walaapp.wala.model.PointsHistoryModel;
import activities.mswift.info.walaapp.wala.prizes.GamesHistoryFragment;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 4/15/2016.
 */
public class PointsHistoryTask {
    private Context context;
    private Map<String, String> params;
    private JSONObject jObject = null;
    private String authorizationKey, result = "", status = "", message = "";
    public ListAdapter adapter;
    private PullToRefreshListView pullToRefreshView;

    public PointsHistoryTask(Context context, PullToRefreshListView pullToRefreshView, Map<String, String> params) {
        this.context = context;
        this.pullToRefreshView = pullToRefreshView;
        this.params = params;
        GamesHistoryFragment.pointsList.clear();
        ((TabHostFragments) context).progressBarVisible();
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getBackendApiUrl(Constants.POINTS_HISTORY)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                ((TabHostFragments) context).progressBarGone();
                Toast.makeText(context, "Please check your network availability", Toast.LENGTH_LONG).show();
                pullToRefreshView.onRefreshComplete();
            }

            @Override
            public void onResponse(String response) {
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        if (jObject.has("status")) {
                            ((TabHostFragments) context).progressBarGone();
                            String totalPoints = jObject.getString("total_points");
                            GamesHistoryFragment.pointsTotal.setText(totalPoints);
                            JSONArray historyArray = jObject.getJSONArray("history");
                            GamesHistoryFragment.pointsList.clear();
                            for (int i = 0; i < historyArray.length(); i++) {

                                JSONObject jsobj = historyArray.getJSONObject(i);
                                String usage = jsobj.getString("usage");
                                String points = jsobj.getString("points");
                                String date = jsobj.getString("date");
                                PointsHistoryModel point = new PointsHistoryModel();
                                point.setUsageDetail(jsobj.getString("usage"));
                                point.setPointsDetail(jsobj.getString("points"));
                                point.setDatePoint(jsobj.getString("date"));

                                GamesHistoryFragment.pointsList.add(point);

                                adapter = new PointsHistoryAdapter(context, GamesHistoryFragment.pointsList);
                                pullToRefreshView.setAdapter(adapter);
                            }
                            pullToRefreshView.onRefreshComplete();

                        } else {
                            ((TabHostFragments) context).progressBarGone();
                        }
                    }
                } catch (JSONException e) {
                    ((TabHostFragments) context).progressBarGone();
                    e.printStackTrace();
                    pullToRefreshView.onRefreshComplete();
                }
            }


        });
    }
}
