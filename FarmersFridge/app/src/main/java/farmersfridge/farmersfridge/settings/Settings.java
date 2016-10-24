package farmersfridge.farmersfridge.settings;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.asynctasks.FeedbackTask;
import farmersfridge.farmersfridge.models.SettingsMainModel;
import farmersfridge.farmersfridge.utils.SessionStores;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 7/15/2016.
 */
public class Settings extends Fragment {

    @BindView(R.id.yourfeedbackheader)
    TextView fbheader;
    @BindView(R.id.thankyouDesc)
    TextView thankyouDesc;
    @BindView(R.id.FaqHeader)
    TextView faqHeader;
    @BindView(R.id.TermsHeader)
    TextView termsHeader;
    @BindView(R.id.TermsDesc)
    TextView termsDesc;
    @BindView(R.id.FaqDesc)
    TextView faqDesc;
    @BindView(R.id.termsBtn)
    ImageView termsBtn;
    @BindView(R.id.faqBtn)
    ImageView faqBtn;
    @BindView(R.id.btnForSubmit)
    Button btnForSave;
    @BindView(R.id.yourmessageText)
    EditText editTxtForMsg;
    @BindView(R.id.emaileditText)
    EditText editTxtForEmail;
    @BindView(R.id.editxttitle)
    EditText editTxtForTitle;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        //View initialized
        View v = inflater.inflate(R.layout.settings, container, false);
        ButterKnife.bind(this, v);

        //Typefaces
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-SemiBold.otf");
        Typeface descfont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-Light.otf");
        Typeface edtTxtFnt = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-Medium.otf");

        fbheader.setTypeface(font);
        thankyouDesc.setTypeface(descfont);
        faqHeader.setTypeface(font);
        termsHeader.setTypeface(font);
        faqDesc.setTypeface(descfont);
        termsDesc.setTypeface(descfont);
        editTxtForTitle.setTypeface(edtTxtFnt);
        editTxtForEmail.setTypeface(edtTxtFnt);
        editTxtForMsg.setTypeface(edtTxtFnt);

        return v;
    }

    @OnClick(R.id.faqBtn)
    public void FAQ() {
        //Fragment replaces with FAQ
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FAQ fragment = new FAQ();
        fragmentTransaction.replace(R.id.realtabcontent, fragment);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.termsBtn)
    public void TermsAndConditions() {
        //Fragment replaces with T&C
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TandC fragment = new TandC();
        fragmentTransaction.replace(R.id.realtabcontent, fragment);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.btnForSubmit)
    public void onSubmitClicked() {
        if (new SessionStores(getActivity()).getLoginStatus() != null && new SessionStores(getActivity()).getAccessToken() != null) {
            if (editTxtForTitle.getText().toString().length() > 0) {
                if (editTxtForEmail.getText().toString().length() > 0) {
                    if (Utils.isEmailValid(editTxtForEmail.getText().toString().trim())) {
                        if (editTxtForMsg.getText().toString().length() > 0) {
                            //Feedback api task calls
                            FragmentManager fragmentManager = getFragmentManager();
                            SessionStores.SETTINGS_MAIN_MODEL = new SettingsMainModel(editTxtForEmail.getText().toString().trim(), editTxtForEmail.getText().toString().trim(),
                                    editTxtForMsg.getText().toString().trim());
                            new FeedbackTask(getActivity(), SessionStores.SETTINGS_MAIN_MODEL.feedback(), fragmentManager);

                            editTxtForMsg.setText("");
                            editTxtForEmail.setText("");
                            editTxtForTitle.setText("");
                        } else {
                            Utils.ShowAlert(getActivity(), "Message field must not be empty");
                        }
                    } else {
                        Utils.ShowAlert(getActivity(), "Email is invalid");
                    }
                } else {
                    Utils.ShowAlert(getActivity(), "Email field must not be empty");
                }
            } else {
                Utils.ShowAlert(getActivity(), "Name field must not be empty");
            }
        } else {
            Utils.ShowAlert(getActivity(), "You need to be logged into sending the feedback");
        }
    }
}
