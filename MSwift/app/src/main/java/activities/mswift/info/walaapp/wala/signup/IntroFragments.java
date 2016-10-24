package activities.mswift.info.walaapp.wala.signup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.adapter.ViewPagerAdapterIntroScreens;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 2/4/2016.
 */
public class IntroFragments extends FragmentActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    String installationId;
    View view;
    ViewPagerAdapterIntroScreens adapter;
    private Context context = IntroFragments.this;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = IntroFragments.class.getSimpleName();
    private static final int REQUEST_INVITE = 0;

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapterIntroScreens(getSupportFragmentManager());
        adapter.addFragment(new Intro1Activity(), "");
        adapter.addFragment(new Intro2Activity(), "");
        adapter.addFragment(new Intro3Activity(), "");
        adapter.addFragment(new Intro4Activity(), "");
        viewPager.setAdapter(adapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_screen_fragments);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "activities.mswift.info.walaapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (SessionStores.getRegisterToken(this) != null && SessionStores.getRegisterToken(this).length() > 0 && SessionStores.getInstallationId(this) == null) {
            Intent intentToOTP = new Intent(IntroFragments.this, ConfirmOTPActivity.class);
            startActivity(intentToOTP);
            finish();
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(AppInvite.API)
                .enableAutoManage(IntroFragments.this, this)
                .build();

        boolean autoLaunchDeepLink = true;

        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autoLaunchDeepLink)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(AppInviteInvitationResult result) {
                                Log.d("", "getInvitation:onResult:" + result.getStatus());
                                // Because autoLaunchDeepLink = true we don't have to do anything
                                // here, but we could set that to false and manually choose
                                // an Activity to launch to handle the deep link here.
                            }
                        });

        installationId = SessionStores.getInstallationId(this);
        if (installationId != null && installationId.length() > 0) {
            Intent intentToLogin = new Intent(IntroFragments.this, LoginActivity.class);
            startActivity(intentToLogin);
            finish();
        } else {
        }

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        showMessage(getString(R.string.google_play_services_error));
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
                Log.d(TAG, getString(R.string.sent_invitations_fmt, ids.length));
            } else {
                // Sending failed or it was canceled, show failure message to the user
                showMessage(getString(R.string.send_failed));
            }
        }
    }

    private void showMessage(String msg) {
        ViewGroup container = (ViewGroup) findViewById(R.id.snackbar_layout);
        Snackbar.make(container, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
    }
}
