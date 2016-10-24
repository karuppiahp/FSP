package activities.mswift.info.walaapp.wala.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 1/28/2016.
 */
public class Intro4Activity extends Fragment implements View.OnClickListener {

    private LinearLayout layForTouch;
    private ImageView btnForEmail, btnForFb, btnForLogin;
    private ProgressBar progressBar;
    private MixpanelAPI mixpanelAPI;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.intro_4_test, container, false);

        Constants.FB_BUTTON_CLICKS = false;
        Constants.EMAIL_ADDRESS = "";
        Constants.PHONE_NUMBER = "";
        Constants.USER_NAME = "";
        Constants.PASSWORD = "";
        Constants.FIRST_NAME = "";
        Constants.MIDDLE_NAME = "";
        Constants.LAST_NAME = "";
        Constants.COUNTRY = "";
        Constants.CITY = "";
        Constants.DOB = "";

        mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
        mixpanelAPI.track(getString(R.string.mixpanel_intro_screen_4));

        btnForEmail = (ImageView) v.findViewById(R.id.btnForEmail);
        btnForFb = (ImageView) v.findViewById(R.id.btnForFb);
        btnForLogin = (ImageView) v.findViewById(R.id.btnForLogin);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        btnForEmail.setOnClickListener(this);
        btnForLogin.setOnClickListener(this);

        SessionStores.saveLoginDate(null, getActivity());

        btnForFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //apiCall();
                /** Intent to signup activity */
                mixpanelAPI.track(getString(R.string.mixpanel_intro_screen_4_fb_signup));
                Constants.FB_BUTTON_CLICKS = true;
                Intent intentToSignUp = new Intent(getActivity(), SignUpActivity.class);
                startActivity(intentToSignUp);
                getActivity().finish();
            }
        });

        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnForEmail) {
            /** Intent to signup activity */
            mixpanelAPI.track(getString(R.string.mixpanel_intro_screen_4_email_signup));
            Intent intentToSignUp = new Intent(getActivity(), SignUpActivity.class);
            startActivity(intentToSignUp);
            getActivity().finish();
        }

        if (v.getId() == R.id.btnForLogin) {
            /** Intent to signup activity */
            Intent intentToLogin = new Intent(getActivity(), LoginActivity.class);
            startActivity(intentToLogin);
            getActivity().finish();
        }
    }
}