package farmersfridge.farmersfridge.login_register;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.asynctasks.RegisterTask;
import farmersfridge.farmersfridge.fragment.FragmentMain;
import farmersfridge.farmersfridge.models.UserProfile;
import farmersfridge.farmersfridge.utils.SessionStores;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 7/14/2016.
 */
public class RegisterActivity extends Fragment {

    @BindView(R.id.edittxtForfName)
    EditText edtTxtFirstName;
    @BindView(R.id.edittxtForLName)
    EditText edtTxtLastName;
    @BindView(R.id.edittxtForPhone)
    EditText edtTxtPhNo;
    @BindView(R.id.edittxtForEmail)
    EditText edtTxtMail;
    @BindView(R.id.signuptext)
    TextView signuptext;
    @BindView(R.id.registerBtn)
    TextView registerBtn;
    @BindView(R.id.joinNowBtn)
    TextView joinNowBtn;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        ((FragmentMain) getActivity()).setHeaderText("");

        //View initialized
        View v = inflater.inflate(R.layout.register_new, container, false);
        ButterKnife.bind(this, v);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GarageFonts - FreightMicro Pro Bold Italic.otf");
        Typeface signupfont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-SemiBold.otf");

        registerBtn.setTypeface(font);
        signuptext.setTypeface(signupfont);
        joinNowBtn.setTypeface(font);

        return v;
    }

    @OnClick(R.id.joinNowBtn)
    public void WelcomePage() {
        Utils.hideKeyboard(getActivity());
        if (edtTxtFirstName.getText().toString().trim().length() > 0) {
            if (edtTxtLastName.getText().toString().trim().length() > 0) {
                if (edtTxtPhNo.getText().toString().trim().length() > 0) {
                    if (Utils.isValidMobile(edtTxtPhNo.getText().toString().trim())) {
                        if (edtTxtMail.getText().toString().trim().length() > 0) {
                            if (Utils.isEmailValid(edtTxtMail.getText().toString().trim())) {
                                //Register api task calls
                                FragmentManager fragmentManager = getFragmentManager();
                                SessionStores.USER_PROFILE = new UserProfile(edtTxtFirstName.getText().toString().trim(), edtTxtLastName.getText().toString().trim(),
                                        edtTxtPhNo.getText().toString().trim(), edtTxtMail.getText().toString().trim());
                                new RegisterTask(getActivity(), SessionStores.USER_PROFILE.register(), fragmentManager);
                            } else {
                                Utils.ShowAlert(getActivity(), "Please enter a valid Email address.");
                            }
                        } else {
                            Utils.ShowAlert(getActivity(), "Email field must not be empty");
                        }
                    } else {
                        Utils.ShowAlert(getActivity(), "Please enter a valid phone number.");
                    }
                } else {
                    Utils.ShowAlert(getActivity(), "Phone number field must not be empty");
                }
            } else {
                Utils.ShowAlert(getActivity(), "Last Name field must not be empty");
            }
        } else {
            Utils.ShowAlert(getActivity(), "First Name field must not be empty");
        }

    }
}
