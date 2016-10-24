package activities.mswift.info.walaapp.wala.prizes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.HashMap;
import java.util.Map;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.asyntasks.QuizQuestionTask;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 2/11/2016.
 */
public class GamesPlayFragment extends Fragment {

    private View gamesPlay;
    private Button btnForPlay, btnForWeekly, btnForMonthly;
    private MixpanelAPI mixpanelAPI;

    public static GamesPlayFragment newInstance() {
        GamesPlayFragment fragment = new GamesPlayFragment();

        return fragment;
    }

    public GamesPlayFragment() {
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
        gamesPlay = inflater.inflate(R.layout.play, container, false);
        btnForPlay = (Button) gamesPlay.findViewById(R.id.dailyQuizbtn);
        btnForWeekly = (Button) gamesPlay.findViewById(R.id.weeklyQuizbtn);
        btnForMonthly = (Button) gamesPlay.findViewById(R.id.monthlyQuizbtn);

        btnForPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SessionStores.getQuizEntry(getActivity()) != null) {
                    if (Utils.calculateDaysBtwn(SessionStores.getQuesDate(getActivity())) == true) {
                        SessionStores.clearQuizQues(getActivity());
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("user_email", SessionStores.getUserEmail(getActivity()));
                        new QuizQuestionTask(getActivity(), params);
                    } else {
                        if (SessionStores.getAnswer(getActivity()).equals("1")) {
                            Constants.GAMES_PAGE = "Correct";
                            GamesActivity.refreshPage();
                        } else if (SessionStores.getAnswer(getActivity()).equals("0")) {
                            Constants.GAMES_PAGE = "Wrong";
                            GamesActivity.refreshPage();
                        }
                    }
                } else {
                    if (SessionStores.getQuesDate(getActivity()) == null) {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("user_email", SessionStores.getUserEmail(getActivity()));
                        new QuizQuestionTask(getActivity(), params);
                    } else if (Utils.calculateDaysBtwn(SessionStores.getQuesDate(getActivity())) == true) {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("user_email", SessionStores.getUserEmail(getActivity()));
                        new QuizQuestionTask(getActivity(), params);
                    } else {
                        if (SessionStores.getAnswer(getActivity()).equals("1")) {
                            Constants.GAMES_PAGE = "Correct";
                            GamesActivity.refreshPage();
                        } else if (SessionStores.getAnswer(getActivity()).equals("0")) {
                            Constants.GAMES_PAGE = "Wrong";
                            GamesActivity.refreshPage();
                        } else {
                            Utils.ShowAlert(getActivity(), "Sorry, your next try is by tomorrow");
                        }
                    }
                }
            }
        });

        btnForWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.ShowAlert(getActivity(), "Wala lotteries will begin soon. Start tracking your spending, reaching goals, and earning points.");
            }
        });

        btnForMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.ShowAlert(getActivity(), "Wala lotteries will begin soon. Start tracking your spending, reaching goals, and earning points.");
            }
        });

        return gamesPlay;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // mixpanel screen tracking
            mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
            mixpanelAPI.track(getString(R.string.mixpanel_games_screen));
        }
    }

}
