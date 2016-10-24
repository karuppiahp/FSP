package activities.mswift.info.walaapp.wala.more;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.adapter.MyAccountAdapter;
import activities.mswift.info.walaapp.wala.adapter.OtherListAdapter;
import activities.mswift.info.walaapp.wala.asyntasks.LogoutTask;
import activities.mswift.info.walaapp.wala.model.MyAccountModel;
import activities.mswift.info.walaapp.wala.model.OtherModel;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;
import io.intercom.android.sdk.Intercom;

/**
 * Created by karuppiah on 11/23/2015.
 */
public class MoreActivity extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener {

    private View v;
    ImageButton btnForSave;
    TabHost mTabHost;
    RelativeLayout layForStep1, layForStep2;
    View step2View;
    ArrayList<MyAccountModel> myAccArray = new ArrayList<>();
    ArrayList<OtherModel> otherArray = new ArrayList<>();
    ListView listForMyAcc, listForOther;
    private static final String TAG = ReferFriendActivity.class.getSimpleName();
    private static final int REQUEST_INVITE = 0;
    private RelativeLayout layForLogout;
    private TextView txtForOther;

    private GoogleApiClient mGoogleApiClient;
    private MixpanelAPI mixpanelAPI;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        mTabHost = new TabHost(getActivity());
        ((TabHostFragments) getActivity()).tabColorWhite();
        v = inflater.inflate(R.layout.more, container, false);
        listForMyAcc = (ListView) v.findViewById(R.id.listViewForMyAcc);
        listForOther = (ListView) v.findViewById(R.id.listViewForOther);
        layForLogout = (RelativeLayout) v.findViewById(R.id.layForLogout);
        txtForOther = (TextView) v.findViewById(R.id.txtForOther);


        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listForMyAcc.getLayoutParams();
        LinearLayout.LayoutParams paramsOther = (LinearLayout.LayoutParams) txtForOther.getLayoutParams();
        LinearLayout.LayoutParams paramsListOther = (LinearLayout.LayoutParams) listForOther.getLayoutParams();
        LinearLayout.LayoutParams paramsLog = (LinearLayout.LayoutParams) layForLogout.getLayoutParams();

        int density = getResources().getDisplayMetrics().densityDpi;

        // mixpanel screen tracking
        mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
        mixpanelAPI.track(getString(R.string.mixpanel_more_screen));

        myAccArray = Utils.getMyAccValues(getActivity()); //My acc array values fetched and saved
        otherArray = Utils.getOtherValues(getActivity()); //Other array values fetched and saved
        listForMyAcc.setAdapter(new MyAccountAdapter(getActivity(), myAccArray));
        listForOther.setAdapter(new OtherListAdapter(getActivity(), otherArray));

        Utils.setListViewHeightBasedOnChildren(listForMyAcc);
        Utils.setListViewHeightBasedOnChildren(listForOther);

        if (density == DisplayMetrics.DENSITY_HIGH) {
            params.setMargins(12, 5, 0, -20);
            paramsOther.setMargins(12, -40, 0, 0);
            paramsListOther.setMargins(12, 5, 0, -20);
            paramsLog.setMargins(3, -20, 0, 0);
            listForMyAcc.setLayoutParams(params);
            txtForOther.setLayoutParams(paramsOther);
            listForOther.setLayoutParams(paramsListOther);
            layForLogout.setLayoutParams(paramsLog);
        }

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Constants.userRegister) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FinancialInfoStep1Activity fragment = new FinancialInfoStep1Activity();
            fragmentTransaction.replace(R.id.realtabcontent, fragment);
            fragmentTransaction.commit();
            Constants.userRegister = false;
        }

        // listview for My Accounts
        listForMyAcc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (i == 0) {
                    EditPersonalInfo fragment = new EditPersonalInfo();
                    fragmentTransaction.replace(R.id.realtabcontent, fragment);
                }
                if (i == 1) {
                    FinancialInfoStep1Activity fragment = new FinancialInfoStep1Activity();
                    fragmentTransaction.replace(R.id.realtabcontent, fragment);
                }

                if (i == 2) {
                    ChangePasswordActivity fragment = new ChangePasswordActivity();
                    fragmentTransaction.replace(R.id.realtabcontent, fragment);
                }
                if (i == 3) {
                    onCustomInviteClicked();
                }
                fragmentTransaction.commit();
            }
        });

        //listview for Other
        listForOther.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (i == 0) {
                    Intercom.client().displayMessageComposer();
                    //mixpanel tracking
                    mixpanelAPI.track(getString(R.string.mixpanel_more_screen_contact_us_screen));
                }
                if (i == 1) {
                    FaqActivity fragment = new FaqActivity();
                    fragmentTransaction.replace(R.id.realtabcontent, fragment);
                }
                if (i == 2) {
                    AboutUsActivity fragment = new AboutUsActivity();
                    fragmentTransaction.replace(R.id.realtabcontent, fragment);
                }
                fragmentTransaction.commit();
            }
        });

        layForLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Are you sure you want to logout from the app?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("token", "" + SessionStores.getAccessToken(getActivity()));
                                new LogoutTask(getActivity(), params);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
        return v;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        showMessage(getString(R.string.google_play_services_error));
    }

    private void onInviteClicked() {
        //mixpanel tracking
        mixpanelAPI.track(getString(R.string.mixpanel_more_screen_refer_friend_screen));
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link) + "&email=" + SessionStores.getUserEmail(getActivity())))
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
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link) + "&email=" + SessionStores.getUserEmail(getActivity())))
                .setEmailHtmlContent("<html><body>" +
                        "<body>Hi,</body><br /><br />" +
                        "<body>Your friend " + SessionStores.getUserName(getActivity()) + " has invited you to join Wala, a financial management application! To accept " + SessionStores.getUserName(getActivity()) + "'s invitation, click the link below, download the app, and get started.</body>" +
                        "<br /><br /><a href=\"%%APPINVITE_LINK_PLACEHOLDER%%\">Install Now!</a>" +
                        "<br /><br />Welcome to Wala!" +
                        "<br />Team Wala" +
                        "</body></html>")
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
