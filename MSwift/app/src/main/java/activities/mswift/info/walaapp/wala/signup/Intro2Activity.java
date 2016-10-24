package activities.mswift.info.walaapp.wala.signup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.utils.Constants;

/**
 * Created by karuppiah on 2/2/2016.
 */
public class Intro2Activity extends Fragment {

    private LinearLayout layForTouch;
    private TextView txtForCntnt;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.intro_2_of_4, container, false);

        MixpanelAPI mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
        mixpanelAPI.track(getString(R.string.mixpanel_intro_screen_2));

        txtForCntnt = (TextView) v.findViewById(R.id.txtForIntro3Cntnt);
        layForTouch = (LinearLayout) v.findViewById(R.id.layForTouch);

        String text1 = "<font><b>Wala</b> will create savings goals for you each week on your personal income, needs, and spending habits. You will track your progress against these goals by easily logging your daily transactions in the <b>Wala</b> app.</font>";
        txtForCntnt.setText(Html.fromHtml(text1));

        return v;
    }
}
