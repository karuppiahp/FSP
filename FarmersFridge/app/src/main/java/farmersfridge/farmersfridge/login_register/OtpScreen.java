package farmersfridge.farmersfridge.login_register;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.asynctasks.ActivateAccountTask;
import farmersfridge.farmersfridge.asynctasks.StartSessionTask;
import farmersfridge.farmersfridge.utils.SessionStores;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 8/1/2016.
 */
public class OtpScreen extends Fragment {

    @BindView(R.id.otpHeader)
    TextView otpText;
    @BindView(R.id.editTxtForOtp)
    EditText editTxtForOtp;
    @BindView(R.id.otpBtn)
    ImageView btnForSend;
    private String from;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        //View initialized
        View v = inflater.inflate(R.layout.otp, container, false);
        ButterKnife.bind(this, v);

        Bundle bundle = getArguments();
        from = bundle.getString("from");

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GarageFonts - FreightMicro Pro Bold Italic.otf");
        otpText.setTypeface(font);

        //Send button click listener
        btnForSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(getActivity());
                if (editTxtForOtp.getText().toString().trim().length() > 0) {
                    SessionStores.USER_PROFILE.setCode(editTxtForOtp.getText().toString().trim());
                    FragmentManager fragmentManager = getFragmentManager();
                    if (from.equals("register")) { //From register screen token activiate api task calls
                        new ActivateAccountTask(getActivity(), SessionStores.USER_PROFILE.activateToken(), fragmentManager);
                    } else { //else session start api task calls
                        new StartSessionTask(getActivity(), SessionStores.USER_PROFILE.startSession(), fragmentManager);
                    }
                } else {
                    Utils.ShowAlert(getActivity(), "Code field must not be empty");
                }
            }
        });
        return v;
    }
}
