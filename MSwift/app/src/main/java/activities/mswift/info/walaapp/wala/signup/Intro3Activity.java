package activities.mswift.info.walaapp.wala.signup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.utils.Constants;

/**
 * Created by karuppiah on 1/28/2016.
 */
public class Intro3Activity extends Fragment {

    private LinearLayout layForTouch;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.intro_3_of_4, container, false);

        MixpanelAPI mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
        mixpanelAPI.track(getString(R.string.mixpanel_intro_screen_3));

        layForTouch = (LinearLayout) v.findViewById(R.id.layForTouch);
        return v;
    }
}
