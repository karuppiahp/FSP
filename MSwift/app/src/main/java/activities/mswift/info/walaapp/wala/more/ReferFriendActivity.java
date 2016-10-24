package activities.mswift.info.walaapp.wala.more;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 12/16/2015.
 */
public class ReferFriendActivity extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener {

    private View v;
    Button btnForSendInvite;

    private static final String TAG = ReferFriendActivity.class.getSimpleName();
    private static final int REQUEST_INVITE = 0;

    private GoogleApiClient mGoogleApiClient;
    private MixpanelAPI mixpanelAPI;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        ((TabHostFragments) getActivity()).tabColorWhite();

        v = inflater.inflate(R.layout.refer_a_friend, container, false);
        btnForSendInvite = (Button) v.findViewById(R.id.btnForSendInvite);

        // mixpanel screen tracking
        mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
        mixpanelAPI.track(getString(R.string.mixpanel_more_screen_refer_friend_screen));

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(AppInvite.API)
                .enableAutoManage(getActivity(), this)
                .build();

        boolean autoLaunchDeepLink = true;

        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, getActivity(), autoLaunchDeepLink)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(AppInviteInvitationResult result) {
                                Log.d(TAG, "getInvitation:onResult:" + result.getStatus());
                                // Because autoLaunchDeepLink = true we don't have to do anything
                                // here, but we could set that to false and manually choose
                                // an Activity to launch to handle the deep link here.
                            }
                        });

        btnForSendInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInviteClicked();
            }
        });
        // Create an auto-managed GoogleApiClient with acccess to App Invites.


        return v;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        showMessage(getString(R.string.google_play_services_error));
    }

    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage("This app is for money maintanance and you received this referral from your friend " + SessionStores.getUserEmail(getActivity()))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link) + " " + SessionStores.getUserEmail(getActivity()))) //Play store published link
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }


    private void onCustomInviteClicked() {
        // When using the setEmailHtmlContent method, you must also set a subject using the
        // setEmailSubject message and you may not use either setCustomImage or setCallToActionText
        // in conjunction with the setEmailHtmlContent method.
        //
        // The "%%APPINVITE_LINK_PLACEHOLDER%%" token is replaced by the invitation server
        // with the custom invitation deep link based on the other parameters you provide.
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setEmailHtmlContent("<html><body>" +
                        "<h1>App Invites</h1>" +
                        "<a href=\"%%APPINVITE_LINK_PLACEHOLDER%%\">Install Now!</a>" +
                        "<body></html>")
                .setEmailSubject(getString(R.string.invitation_subject))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == Activity.RESULT_OK) {
                // Check how many invitations were sent and log a message
                // The ids array contains the unique invitation ids for each invitation sent
                // (one for each contact select by the user). You can use these for analytics
                // as the ID will be consistent on the sending and receiving devices.
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
            } else {
                // Sending failed or it was canceled, show failure message to the user
                showMessage(getString(R.string.send_failed));
            }
        }
    }

    private void showMessage(String msg) {
        ViewGroup container = (ViewGroup) v.findViewById(R.id.snackbar_layout);
        Snackbar.make(container, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }
}
