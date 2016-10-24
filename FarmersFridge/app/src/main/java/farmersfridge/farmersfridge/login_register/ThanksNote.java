package farmersfridge.farmersfridge.login_register;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import farmersfridge.farmersfridge.R;

/**
 * Created by karuppiah on 7/14/2016.
 */
public class ThanksNote extends Fragment {

    @BindView(R.id.ThankyouText)
    TextView thankyouText;
    @BindView(R.id.WelcomeText)
    TextView welcomeText;
    @BindView(R.id.ConfirmationText)
    TextView confirmationText;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        //View initialized
        View v = inflater.inflate(R.layout.thank_you, container, false);
        ButterKnife.bind(this, v);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GarageFonts - FreightMicro Pro Bold Italic.otf");
        Typeface welcomefont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GarageFonts - FreightMicro Pro Black.otf");
        Typeface confirmFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GarageFonts - FreightMicro Pro Medium.otf");

        thankyouText.setTypeface(font);
        welcomeText.setTypeface(confirmFont);
        confirmationText.setTypeface(welcomefont);

        return v;
    }
}
