package activities.mswift.info.walaapp.wala.more;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.HashMap;
import java.util.Map;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.asyntasks.ChangePasswordTask;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 12/15/2015.
 */
public class ChangePasswordActivity extends Fragment {

    private View v;
    private Button btnForSave;
    private TextView txtForPopUp;
    private EditText editTxtForOldPwd, editTxtForNewPwd;
    private MixpanelAPI mixpanelAPI;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        ((TabHostFragments) getActivity()).tabColorWhite();

        v = inflater.inflate(R.layout.change_password, container, false);
        editTxtForOldPwd = (EditText) v.findViewById(R.id.editTxtForOldPwd);
        editTxtForNewPwd = (EditText) v.findViewById(R.id.editTxtForNewPwd);
        btnForSave = (Button) v.findViewById(R.id.btnForSave);
        txtForPopUp = (TextView) v.findViewById(R.id.txtForChngPwdPopUp);

        String popUpTxt = "<font color='#7d898c'>Password should contain the following: <br/><br/>" +
                "- At least 6 characters or more <br/>" +
                "- At least 1 capital and 1 lowercase letter <br/>" +
                "- At least 1 number <br/>" +
                "- At least one of the following special characters:<br/>" +
                "<b> ?@!-+</b></font>";

        txtForPopUp.setText(Html.fromHtml(popUpTxt));


        editTxtForOldPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_NEXT:
                        if (editTxtForOldPwd.getText().toString().length() > 0) {
                            if (Utils.PasswordValidator(editTxtForOldPwd.getText().toString())) {
                                editTxtForNewPwd.requestFocus();
                            } else {
                                editTxtForOldPwd.setError("Old password must be at least 6 characters long, using uppercase, lowercase, numbers and special characters (!, @, ?, -, +).");
                                return true;
                            }
                        } else {
                            Utils.ShowAlert(getActivity(), "Please enter the old password");
                            return true;
                        }
                        break;
                }
                return false;
            }
        });

        editTxtForNewPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        if (editTxtForNewPwd.getText().toString().length() > 0) {
                            if (Utils.PasswordValidator(editTxtForNewPwd.getText().toString())) {
                            } else {
                                editTxtForNewPwd.setError("New password must be at least 6 characters long, using uppercase, lowercase, numbers and special characters (!, @, ?, -, +).");
                                return true;
                            }
                        } else {
                            Utils.ShowAlert(getActivity(), "Please enter the new password");
                            return true;
                        }
                        break;
                }
                return false;
            }
        });

        // mixpanel screen tracking
        mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
        mixpanelAPI.track(getString(R.string.mixpanel_more_screen_change_password_screen));

        btnForSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TabHostFragments) getActivity()).hideKeyboard();
                if (editTxtForOldPwd.getText().toString().length() > 0) {
                    if (Utils.PasswordValidator(editTxtForOldPwd.getText().toString())) {
                        if (editTxtForOldPwd.getText().toString().equals(SessionStores.getUserPwd(getActivity()))) {
                            if (editTxtForNewPwd.getText().toString().length() > 0) {
                                if (Utils.PasswordValidator(editTxtForNewPwd.getText().toString())) {
                                    if (editTxtForOldPwd.getText().toString().equals(editTxtForNewPwd.getText().toString())) {
                                        Utils.ShowAlert(getActivity(), "Old and New Password should not be same");
                                    } else {
                                        /** Call another fragment */
                                        FragmentManager fragmentManager = getFragmentManager();
                                        Map<String, String> params = new HashMap<>();
                                        params.put("OldPassword", editTxtForOldPwd.getText().toString());
                                        params.put("NewPassword", editTxtForNewPwd.getText().toString());
                                        new ChangePasswordTask(getActivity(), params, fragmentManager);
                                    }
                                } else {
                                    editTxtForNewPwd.setError("New password must be at least 6 characters long, using uppercase, lowercase, numbers and special characters (!, @, ?, -, +).");
                                }
                            } else {
                                Utils.ShowAlert(getActivity(), "Please enter the new password");
                            }
                        } else {
                            Utils.ShowAlert(getActivity(), "Old password is invalid, please enter the correct one.");
                        }
                    } else {
                        editTxtForOldPwd.setError("Old password must be at least 6 characters long, using uppercase, lowercase, numbers and special characters (!, @, ?, -, +).");
                    }
                } else {
                    Utils.ShowAlert(getActivity(), "Please enter the old password");
                }
            }
        });

        return v;
    }
}