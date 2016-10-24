package activities.mswift.info.walaapp.wala.prizes;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.HashMap;
import java.util.Map;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.adapter.ViewPagerAdapterProgress;
import activities.mswift.info.walaapp.wala.asyntasks.BuyTicketsTask;
import activities.mswift.info.walaapp.wala.asyntasks.DropTicketTask;
import activities.mswift.info.walaapp.wala.model.GetLotteryModel;
import activities.mswift.info.walaapp.wala.support.SlidingTabLayout;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 2/11/2016.
 */
public class GamesActivity extends Fragment {

    private View v;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private PagerTabStrip pagerTabStrip;
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPagerAdapterProgress adapter;
    private TabLayout tabLayout;
    private static FragmentManager fragmentManager;
    private static RelativeLayout layForPopUp;
    public static Button btnForBuyTicket, btnForDropTicket;
    private static TextView txtForPts, txtForPtsLeft, txtForTktCount, txtForDone;
    private TextView withoutArrowTxt, withArrowTxt;
    private ImageButton backButton;
    private MixpanelAPI mixpanelAPI;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        ((TabHostFragments) getActivity()).tabColorWhite();
        ((TabHostFragments) getActivity()).removeChild();

        v = inflater.inflate(R.layout.games, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        layForPopUp = (RelativeLayout) v.findViewById(R.id.layForPopUp);
        btnForBuyTicket = (Button) v.findViewById(R.id.btnForBuyTkt);
        btnForDropTicket = (Button) v.findViewById(R.id.btnForDropTkt);
        txtForPts = (TextView) v.findViewById(R.id.txtForPoints);
        txtForPtsLeft = (TextView) v.findViewById(R.id.txtForPtsLeft);
        txtForTktCount = (TextView) v.findViewById(R.id.txtForTktsBought);
        txtForDone = (TextView) v.findViewById(R.id.txtForDone);
        withoutArrowTxt = (TextView) v.findViewById(R.id.witoutArrowTxt);
        withArrowTxt = (TextView) v.findViewById(R.id.withArrow);
        backButton = (ImageButton) v.findViewById(R.id.backButton);
        fragmentManager = getFragmentManager();

        if (Constants.GAMES_PAGE.length() == 0) {
            setupViewPager(viewPager);
        } else if (Constants.GAMES_PAGE.equals("Daily")) {
            backButton.setVisibility(View.VISIBLE);
            withArrowTxt.setVisibility(View.VISIBLE);
            withoutArrowTxt.setVisibility(View.GONE);
            Constants.GAMES_PAGE = "";
            setupViewPagerQuiz(viewPager);
        } else if (Constants.GAMES_PAGE.equals("Correct")) {
            backButton.setVisibility(View.VISIBLE);
            withArrowTxt.setVisibility(View.VISIBLE);
            withoutArrowTxt.setVisibility(View.GONE);
            Constants.GAMES_PAGE = "";
            setupViewPagerCorrect(viewPager);
        } else if (Constants.GAMES_PAGE.equals("Wrong")) {
            backButton.setVisibility(View.VISIBLE);
            withArrowTxt.setVisibility(View.VISIBLE);
            withoutArrowTxt.setVisibility(View.GONE);
            Constants.GAMES_PAGE = "";
            setupViewPagerWrong(viewPager);
        } else if (Constants.GAMES_PAGE.equals("Weekly")) {
            backButton.setVisibility(View.VISIBLE);
            withArrowTxt.setVisibility(View.VISIBLE);
            withoutArrowTxt.setVisibility(View.GONE);
            Constants.GAMES_PAGE = "";
            setupViewPagerWeekly(viewPager);
        } else if (Constants.GAMES_PAGE.equals("Monthly")) {
            backButton.setVisibility(View.VISIBLE);
            withArrowTxt.setVisibility(View.VISIBLE);
            withoutArrowTxt.setVisibility(View.GONE);
            Constants.GAMES_PAGE = "";
            setupViewPagerMonthly(viewPager);
        }

        tabLayout.setupWithViewPager(viewPager);
        setUpTabIcons();

        btnForBuyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnForBuyTicket.setClickable(false);
                btnForBuyTicket.setEnabled(false);
                GetLotteryModel lotteryModel = Constants.LOTTERY_MODEL_ARRAY.get(0);
                if (Integer.parseInt(lotteryModel.getPointsLeft()) >= Integer.parseInt(lotteryModel.getPoints())) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_email", SessionStores.getUserEmail(getActivity()));
                    params.put("lottery_id", lotteryModel.getId());
                    if (Constants.WEEKLY_MONTHLY == 1) {
                        params.put("mode", "1");
                    } else {
                        params.put("mode", "0");
                    }
                    new BuyTicketsTask(getActivity(), params);
                } else {
                    btnForBuyTicket.setClickable(true);
                    btnForBuyTicket.setEnabled(true);
                    Utils.ShowAlert(getActivity(), "You need more points to buy a ticket");
                }
            }
        });

        btnForDropTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnForDropTicket.setClickable(false);
                btnForDropTicket.setEnabled(false);
                GetLotteryModel lotteryModel = Constants.LOTTERY_MODEL_ARRAY.get(0);
                if (Integer.parseInt(lotteryModel.getTcktsCount()) > 0) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_email", SessionStores.getUserEmail(getActivity()));
                    params.put("lottery_id", lotteryModel.getId());
                    if (Constants.WEEKLY_MONTHLY == 1) {
                        params.put("mode", "1");
                    } else {
                        params.put("mode", "0");
                    }
                    new DropTicketTask(getActivity(), params);
                } else {
                    btnForDropTicket.setClickable(true);
                    btnForDropTicket.setEnabled(true);
                    Utils.ShowAlert(getActivity(), "You have no tickets to drop");
                }
            }
        });

        txtForDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpHide();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Fragment newFragment = new GamesActivity();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.realtabcontent, newFragment);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();


            }
        });

        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapterProgress(getChildFragmentManager());
        adapter.clear();
        adapter.addFragment(new GamesPlayFragment(), "PLAY");
        adapter.addFragment(new GamesHistoryFragment(), "HISTORY");
        viewPager.setAdapter(adapter);
    }

    private void setupViewPagerQuiz(ViewPager viewPager) {
        adapter = new ViewPagerAdapterProgress(getChildFragmentManager());
        adapter.clear();
        adapter.addFragment(new DailyQuizActivity(), "PLAY");
        adapter.addFragment(new GamesHistoryFragment(), "HISTORY");
        viewPager.setAdapter(adapter);
    }

    private void setupViewPagerCorrect(ViewPager viewPager) {
        adapter = new ViewPagerAdapterProgress(getChildFragmentManager());
        adapter.clear();
        adapter.addFragment(new CorrectAnswerActivity(), "PLAY");
        adapter.addFragment(new GamesHistoryFragment(), "HISTORY");
        viewPager.setAdapter(adapter);
    }

    private void setupViewPagerWrong(ViewPager viewPager) {
        adapter = new ViewPagerAdapterProgress(getChildFragmentManager());
        adapter.clear();
        adapter.addFragment(new WrongAnswerActivity(), "PLAY");
        adapter.addFragment(new GamesHistoryFragment(), "HISTORY");
        viewPager.setAdapter(adapter);
    }

    private void setupViewPagerWeekly(ViewPager viewPager) {
        adapter = new ViewPagerAdapterProgress(getChildFragmentManager());
        adapter.clear();
        adapter.addFragment(new WeeklyLotteryActivity(), "PLAY");
        adapter.addFragment(new GamesHistoryFragment(), "HISTORY");
        viewPager.setAdapter(adapter);
    }

    private void setupViewPagerMonthly(ViewPager viewPager) {
        adapter = new ViewPagerAdapterProgress(getChildFragmentManager());
        adapter.clear();
        adapter.addFragment(new MonthlyLotteryActivity(), "PLAY");
        adapter.addFragment(new GamesHistoryFragment(), "HISTORY");
        viewPager.setAdapter(adapter);
    }

    public static void refreshPage() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        GamesActivity fragment = new GamesActivity();
        fragmentTransaction.replace(R.id.realtabcontent, fragment);
        fragmentTransaction.commit();
    }

    private void setUpTabIcons() {
        View viewPlay = LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_text, null);
        TextView tabPlay = (TextView) viewPlay.findViewById(R.id.textViewTitle);
        tabPlay.setText("PLAY");
        tabLayout.getTabAt(0).setCustomView(tabPlay);

        View viewHistory = LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_text, null);
        TextView tabHistory = (TextView) viewHistory.findViewById(R.id.textViewTitle);
        tabHistory.setText("HISTORY");
        tabLayout.getTabAt(1).setCustomView(tabHistory);
    }

    public static void popUpVisible() {
        if (Constants.LOTTERY_MODEL_ARRAY.size() > 0) {
            layForPopUp.setVisibility(View.VISIBLE);
            GetLotteryModel lotteryModel = Constants.LOTTERY_MODEL_ARRAY.get(0);
            String pntsLeft = "<font color=#5cb5c2>" + lotteryModel.getPointsLeft() + "</font>";
            String tcktsCount = "<font color=#5cb5c2>" + lotteryModel.getTcktsCount() + "</font>";
            txtForPts.setText("Tickets cost " + lotteryModel.getPoints() + " points per ticket");
            txtForPtsLeft.setText(Html.fromHtml(pntsLeft + " Points Left"));
            txtForTktCount.setText(Html.fromHtml(tcktsCount + " Tickets Bought"));
        }
    }

    public static void popUpHide() {
        layForPopUp.setVisibility(View.GONE);
    }

}
