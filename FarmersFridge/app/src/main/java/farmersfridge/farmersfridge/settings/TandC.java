package farmersfridge.farmersfridge.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.asynctasks.TermsAndConditionsTask;
import farmersfridge.farmersfridge.models.SettingsMainModel;
import farmersfridge.farmersfridge.utils.SessionStores;

/**
 * Created by karuppiah on 6/21/2016.
 */
public class TandC extends Fragment {

    @BindView(R.id.txtForTandC)
    TextView txtForTandC;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        //View initialized
        View v = inflater.inflate(R.layout.terms_and_cndtns, container, false);
        ButterKnife.bind(this, v);
        FragmentManager fragmentManager = getFragmentManager();
        SessionStores.SETTINGS_MAIN_MODEL = new SettingsMainModel();
        new TermsAndConditionsTask(getActivity(), TandC.this, SessionStores.SETTINGS_MAIN_MODEL.termsConditions(), fragmentManager);
        return v;
    }

    public void setText(String text) {
        txtForTandC.setText(Html.fromHtml(text));
    }

    @OnClick(R.id.backbtn)
    public void backButton(){
        //Fragment replaces with settings screen
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Settings fragment = new Settings();
        fragmentTransaction.replace(R.id.realtabcontent, fragment);
        fragmentTransaction.commit();
    }
}
