package activities.mswift.info.walaapp.wala.prizes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.asyntasks.PointsHistoryTask;
import activities.mswift.info.walaapp.wala.model.PointsHistoryModel;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 2/11/2016.
 */
public class GamesHistoryFragment extends Fragment {

    private View balsavings;
    public ListAdapter adapter;
    public static TextView pointsTotal;
    public int pageNum = 1;
    private PullToRefreshListView pullToRefreshView;
    public static List<PointsHistoryModel> pointsList = new ArrayList<PointsHistoryModel>();
    private MixpanelAPI mixpanelAPI;

    public static GamesHistoryFragment newInstance() {
        GamesHistoryFragment fragment = new GamesHistoryFragment();

        return fragment;
    }

    public GamesHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        balsavings = inflater.inflate(R.layout.history, container, false);
        pointsTotal = (TextView) balsavings.findViewById(R.id.total);
        pullToRefreshView = (PullToRefreshListView) balsavings.findViewById(R.id.pointsHistoryList);

        pageNum = 1;
        pointsList.clear();
        pointsHistorydetail();

        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                ++pageNum;
                pointsHistorydetail();
            }
        });

        return balsavings;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // mixpanel screen tracking
            mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
            mixpanelAPI.track(getString(R.string.mixpanel_games_history_screen));
        }
    }

    private void pointsHistorydetail() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_email", SessionStores.getUserEmail(getActivity()));
        params.put("page_number", "" + pageNum);
        new PointsHistoryTask(getActivity(), pullToRefreshView, params);
    }
}
