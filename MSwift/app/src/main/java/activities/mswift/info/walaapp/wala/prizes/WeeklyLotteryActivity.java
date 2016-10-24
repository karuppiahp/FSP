package activities.mswift.info.walaapp.wala.prizes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.HashMap;
import java.util.Map;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.asyntasks.GetCurrentLotteryTask;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 3/7/2016.
 */
public class WeeklyLotteryActivity extends Fragment {

    private View v;
    public static TextView txtForAmnt, txtForParticipants, txtForDaysLeft, txtForPointsWinnerCount;
    public static RelativeLayout btnForBuyTickets;
    private MixpanelAPI mixpanelAPI;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        ((TabHostFragments) getActivity()).tabColorMerun();

        v = inflater.inflate(R.layout.weekly_lottery, container, false);
        txtForAmnt = (TextView) v.findViewById(R.id.priceamount);
        txtForParticipants = (TextView) v.findViewById(R.id.txtForParticipants);
        txtForDaysLeft = (TextView) v.findViewById(R.id.txtForDaysLeft);
        btnForBuyTickets = (RelativeLayout) v.findViewById(R.id.btnForBuyTickets);
        txtForPointsWinnerCount = (TextView) v.findViewById(R.id.txtForPointsWinnerCnt);
        Map<String, String> params = new HashMap<String, String>();
        params.put("mode", "1");
        params.put("user_email", SessionStores.getUserEmail(getActivity()));
        new GetCurrentLotteryTask(getActivity(), params, "weekly");
        btnForBuyTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.WEEKLY_MONTHLY = 1;
                GamesActivity.popUpVisible();
            }
        });

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // mixpanel screen tracking
            mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
            mixpanelAPI.track(getString(R.string.mixpanel_games_weekly_quiz));
        }
    }
}
