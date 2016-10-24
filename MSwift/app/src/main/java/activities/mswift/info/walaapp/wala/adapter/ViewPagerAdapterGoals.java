package activities.mswift.info.walaapp.wala.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.home.GoalsActivity;
import activities.mswift.info.walaapp.wala.model.CurrentGoalModel;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 2/15/2016.
 */
public class ViewPagerAdapterGoals extends PagerAdapter {
    // Declare Variables
    Context context;
    LayoutInflater inflater;
    private RelativeLayout layForBg;
    private TextView txtForGoalsName, txtForTypeMode, txtForDaysLeft, txtForSubTitleName, txtForInsideProgress;
    private ImageView imgForIcon;
    private RelativeLayout layForProgressMain;
    private View viewForProgress;
    private int size;
    private MixpanelAPI mixpanelAPI;
    private int currentValueInt = 0, goalValueInt = 0;

    public ViewPagerAdapterGoals(Context context, int size) {
        this.context = context;
        this.size = size;
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.gaols_pager_item, container,
                false);
        // Locate the ImageView in viewpager_item.xml
        layForBg = (RelativeLayout) itemView.findViewById(R.id.layForPagerBg);
        txtForGoalsName = (TextView) itemView.findViewById(R.id.threeTimesPlay);
        imgForIcon = (ImageView) itemView.findViewById(R.id.games_Icon);
        layForProgressMain = (RelativeLayout) itemView.findViewById(R.id.layForProgress);
        txtForTypeMode = (TextView) itemView.findViewById(R.id.weeklytxt);
        txtForDaysLeft = (TextView) itemView.findViewById(R.id.daysleft);
        txtForSubTitleName = (TextView) itemView.findViewById(R.id.playnow_Game);
        txtForInsideProgress = (TextView) itemView.findViewById(R.id.txtForProgressStatus);
        ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);

        CurrentGoalModel goalsModel = GoalsActivity.goalsArray.get(position);
        if (goalsModel.getMode().equalsIgnoreCase("weekly")) {
            txtForTypeMode.setText("WEEKLY GOAL");
            if (goalsModel.getType().equalsIgnoreCase("quizzes") || goalsModel.getType().equalsIgnoreCase("correct%")) {
                imgForIcon.setBackgroundResource(R.drawable.playgameicon);
            }

            if (goalsModel.getType().equalsIgnoreCase("all wants") || goalsModel.getType().equalsIgnoreCase("Specific Want") || goalsModel.getType().equalsIgnoreCase("Savings")) {
                imgForIcon.setBackgroundResource(R.drawable.goals_balances_icon);
            }

            if (goalsModel.getType().equalsIgnoreCase("Data") || goalsModel.getType().equalsIgnoreCase("Entered") || goalsModel.getType().equalsIgnoreCase("Savings")) {
                imgForIcon.setBackgroundResource(R.drawable.plus_img_icon);
            }

            // mixpanel screen tracking
            mixpanelAPI = MixpanelAPI.getInstance(context, Constants.MIXPANEL_TOKEN);
            mixpanelAPI.track(context.getString(R.string.mixpanel_goals_weekly_quiz_screen));
        } else {
            txtForTypeMode.setText("MONTHLY GOAL");
            imgForIcon.setBackgroundResource(R.drawable.plus_img_icon);

            // mixpanel screen tracking
            mixpanelAPI = MixpanelAPI.getInstance(context, Constants.MIXPANEL_TOKEN);
            mixpanelAPI.track(context.getString(R.string.mixpanel_goals_monthly_quiz_screen));
        }

        if (goalsModel.getCurrentValue().length() > 0 && !(goalsModel.getCurrentValue().equalsIgnoreCase("null"))) {
            if (isDouble(goalsModel.getCurrentValue())) {
                double valueD = Double.parseDouble(goalsModel.getCurrentValue());
                currentValueInt = (int) valueD;
            } else {
                currentValueInt = Integer.parseInt(goalsModel.getCurrentValue());
            }
        }

        if (goalsModel.getGoalValue().length() > 0 && !(goalsModel.getGoalValue().equalsIgnoreCase("null"))) {
            if (isDouble(goalsModel.getGoalValue())) {
                double valueD = Double.parseDouble(goalsModel.getGoalValue());
                goalValueInt = (int) valueD;
            } else {
                goalValueInt = Integer.parseInt(goalsModel.getGoalValue());
            }
        }

        if (goalsModel.getCurrentValue().length() > 0 && !(goalsModel.getCurrentValue().equalsIgnoreCase("null")) && currentValueInt > 0 && goalsModel.getGoalValue().length() > 0 && !(goalsModel.getGoalValue().equalsIgnoreCase("null")) && goalValueInt > 0) {
            Double goalsValueDouble = Double.parseDouble(goalsModel.getGoalValue());
            int goalValue = goalsValueDouble.intValue();
            int progressStatus = (Integer.parseInt(goalsModel.getCurrentValue()) * 100) / goalValue;
            progressBar.setProgress(progressStatus);
        } else {
            progressBar.setProgress(0);
        }
        txtForDaysLeft.setText(goalsModel.getDaysLeft() + " DAYS LEFT");
        txtForGoalsName.setText(Utils.goalNames(goalsModel.getType(), goalsModel.getGoalValue(), "monthly"));
        txtForSubTitleName.setText(Utils.goalReachedMain(goalsModel.getType()));
        txtForInsideProgress.setText(Utils.goalReached(goalsModel.getType()));

        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((LinearLayout) object);

    }

    boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
