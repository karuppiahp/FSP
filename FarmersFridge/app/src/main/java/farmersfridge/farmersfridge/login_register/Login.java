package farmersfridge.farmersfridge.login_register;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.asynctasks.LoginTask;
import farmersfridge.farmersfridge.fragment.FragmentMain;
import farmersfridge.farmersfridge.models.UserProfile;
import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.SessionStores;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 7/14/2016.
 */
public class Login extends Fragment {

    @BindView(R.id.loginText)
    TextView loginText;
    @BindView(R.id.signuptext)
    TextView signuptext;
    @BindView(R.id.registerBtn)
    TextView registerBtn;
    @BindView(R.id.firstNameLogin)
    EditText edtTxtForName;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        ((FragmentMain) getActivity()).setHeaderText("");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);

        //View initialized
        View v = inflater.inflate(R.layout.login, container, false);
        ButterKnife.bind(this, v);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GarageFonts - FreightMicro Pro Bold Italic.otf");
        Typeface signupfont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-SemiBold.otf");

        registerBtn.setTypeface(font);
        loginText.setTypeface(font);
        signuptext.setTypeface(signupfont);

        return v;
    }

    @OnClick(R.id.loginText)
    public void Login() {
        Utils.hideKeyboard(getActivity());
        if (edtTxtForName.getText().toString().trim().length() > 0) {
            Constants.REGISTER = "";
            //Login api task calls
            SessionStores.USER_PROFILE = new UserProfile(edtTxtForName.getText().toString());
            FragmentManager fragmentManager = getFragmentManager();
            new LoginTask(getActivity(), SessionStores.USER_PROFILE.login(), fragmentManager);
        } else {
            Utils.ShowAlert(getActivity(), "Phone Number field must not be empty");
        }
    }

    @OnClick(R.id.registerBtn)
    public void Register() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RegisterActivity fragment = new RegisterActivity();
        fragmentTransaction.replace(R.id.realtabcontent, fragment);
        fragmentTransaction.commit();
    }
}
