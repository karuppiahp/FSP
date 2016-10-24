package activities.mswift.info.walaapp.wala.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.adapter.ViewPagerAdapterGoals;
import activities.mswift.info.walaapp.wala.asyntasks.GetCurrentGoalsTask;
import activities.mswift.info.walaapp.wala.asyntasks.GoalsHistoryTask;
import activities.mswift.info.walaapp.wala.model.CurrentGoalModel;
import activities.mswift.info.walaapp.wala.model.GoalsHistoryModel;
import activities.mswift.info.walaapp.wala.more.FinancialInfoStep1Activity;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 2/15/2016.
 */
public class GoalsActivity extends Fragment {

    private static String userEmail;
    private View v;
    private ViewPager viewPager;
    private ViewPagerAdapterGoals pagerAdapter;
    private Button btnForGotIt;
    private RelativeLayout layForOverlay, layForRedirect, btnForRedirect;
    private ImageView imgForInfoIcon;
    public static ArrayList<CurrentGoalModel> goalsArray = new ArrayList<>();
    public static PullToRefreshListView pullToRefreshView;
    public static ListView listViewForHistory;
    public static SwipyRefreshLayout swipeRefreshLayout;
    public static int pageNum = 1;
    public static List<GoalsHistoryModel> goalsList = new ArrayList<GoalsHistoryModel>();
    private MixpanelAPI mixpanelAPI;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        ((TabHostFragments) getActivity()).tabColorWhite();

        v = inflater.inflate(R.layout.goals, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        btnForGotIt = (Button) v.findViewById(R.id.btnForGotIt);
        layForOverlay = (RelativeLayout) v.findViewById(R.id.layForOverlay);
        layForRedirect = (RelativeLayout) v.findViewById(R.id.layForRedirect);
        btnForRedirect = (RelativeLayout) v.findViewById(R.id.layForRedirectBtn);
        imgForInfoIcon = (ImageView) v.findViewById(R.id.imgForInfoIcon);
        swipeRefreshLayout = (SwipyRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        listViewForHistory = (ListView) v.findViewById(R.id.listViewForGoals);

        swipeRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTTOM);

        userEmail = SessionStores.getUserEmail(getActivity());
        goalsList.clear();

        Map<String, String> param = new HashMap<String, String>();
        param.put("user_email", SessionStores.getUserEmail(getActivity()));
        pageNum = 1;
        new GetCurrentGoalsTask(getActivity(), GoalsActivity.this, param, goalsArray, pagerAdapter, viewPager);

        // mixpanel screen tracking
        mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
        mixpanelAPI.track(getString(R.string.mixpanel_goals_screen));

        btnForGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layForOverlay.setVisibility(View.GONE);
            }
        });

        imgForInfoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layForOverlay.setVisibility(View.VISIBLE);
            }
        });

        btnForRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TabHostFragments) getActivity()).setcurrent(4);
                ((TabHostFragments) getActivity()).setTabSelected("more");
                /** Move to Financial Profile */
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FinancialInfoStep1Activity fragment = new FinancialInfoStep1Activity();
                fragmentTransaction.replace(R.id.realtabcontent, fragment);
                fragmentTransaction.commit();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
                ++pageNum;
                Map<String, String> paramsHistory = goalsHistory();
                new GoalsHistoryTask(getActivity(), GoalsActivity.listViewForHistory, paramsHistory, "1");
            }
        });
        return v;
    }

    public static Map<String, String> goalsHistory() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_email", userEmail);
        params.put("page_number", "" + pageNum);
        return params;
    }

    public void overlayView(boolean success) {
        if (success == false) {
            layForRedirect.setVisibility(View.VISIBLE);
        } else {
            layForRedirect.setVisibility(View.GONE);
            if (SessionStores.getGoalsOverlay(getActivity()) == null || !(SessionStores.getGoalsOverlay(getActivity()).equals("entered"))) {
                SessionStores.saveGoalsOverlay("entered", getActivity());
                layForOverlay.setVisibility(View.VISIBLE);
            } else {
                layForOverlay.setVisibility(View.GONE);
            }
        }
    }
}
