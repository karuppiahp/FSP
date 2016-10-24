package farmersfridge.farmersfridge.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.asynctasks.ActivateAccountTask;
import farmersfridge.farmersfridge.utils.SessionStores;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 6/15/2016.
 */
public class CodeEntry extends Fragment {

    @BindView(R.id.editTxtForCode)
    EditText edtTxtForCode;
    @BindView(R.id.btnForSave)
    Button btnForSave;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        //View initialized
        View v = inflater.inflate(R.layout.code_entry, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @OnClick(R.id.btnForSave)
    public void onSave() {
        if (edtTxtForCode.getText().toString().trim().length() > 0) {
            SessionStores.USER_PROFILE.setCode(edtTxtForCode.getText().toString().trim());
            FragmentManager fragmentManager = getFragmentManager();
            //Activate account api task calls
            new ActivateAccountTask(getActivity(), SessionStores.USER_PROFILE.activateToken(), fragmentManager);
        } else {
            Utils.ShowAlert(getActivity(), "Code field must not be empty");
        }
    }
}
