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
 * Created by karuppiah on 2/1/2016.
 */
public class Intro1Activity extends Fragment {

    private LinearLayout layForTouch;
    private TextView txtForCntnt1, txtForCntnt2;
    String installationId;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(
                R.layout.intro_1_of_4, container, false);

        MixpanelAPI mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
        mixpanelAPI.track(getString(R.string.mixpanel_intro_screen_1));

        txtForCntnt1 = (TextView) v.findViewById(R.id.txtForIntro1Cntnt);
        txtForCntnt2 = (TextView) v.findViewById(R.id.txtForIntro1Cntnt2);
        layForTouch = (LinearLayout) v.findViewById(R.id.layForTouch);

        String text1 = "<font><b>Wala</b> is your personalized savings tool.</font>";
        String text2 = "<font>Swipe to learn how <b>Wala</b> can help you increase your life savings!</font>";

        txtForCntnt1.setText(Html.fromHtml(text1));
        txtForCntnt2.setText(Html.fromHtml(text2));

        return v;
    }
}
