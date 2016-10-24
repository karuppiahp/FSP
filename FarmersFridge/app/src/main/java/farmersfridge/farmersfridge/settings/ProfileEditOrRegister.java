package farmersfridge.farmersfridge.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.asynctasks.GetProfileDataTask;
import farmersfridge.farmersfridge.asynctasks.RegisterTask;
import farmersfridge.farmersfridge.asynctasks.UpdateProfileTasks;
import farmersfridge.farmersfridge.models.UserProfile;
import farmersfridge.farmersfridge.utils.SessionStores;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 6/14/2016.
 */
public class ProfileEditOrRegister extends Fragment {

    @BindView(R.id.edittxtForfName)
    EditText edtTxtFirstName;
    @BindView(R.id.edittxtForLName)
    EditText edtTxtLastName;
    @BindView(R.id.edittxtForPhone)
    EditText edtTxtPhNo;
    @BindView(R.id.edittxtForEmail)
    EditText edtTxtMail;
    @BindView(R.id.btnForSave)
    Button btnForSave;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        //View initialized
        View v = inflater.inflate(R.layout.profile_edit_register, container, false);
        ButterKnife.bind(this, v);

        if(new SessionStores(getActivity()).getAccessToken() != null || new SessionStores(getActivity()).getAccessToken().length() > 0) {
            FragmentManager fragmentManager = getFragmentManager();
            SessionStores.USER_PROFILE = new UserProfile();
            //Get profile api task
            new GetProfileDataTask(getActivity(), ProfileEditOrRegister.this, SessionStores.USER_PROFILE.getProfileDate(), fragmentManager);
        }
        return v;
    }

    public void setProfileDate(String firstName, String lastName, String phNo, String email) {
        edtTxtFirstName.setText(firstName);
        edtTxtLastName.setText(lastName);
        edtTxtPhNo.setText(phNo.replace("+",""));
        edtTxtMail.setText(email);
        edtTxtPhNo.setClickable(false);
        edtTxtPhNo.setEnabled(false);
        edtTxtMail.setClickable(false);
        edtTxtMail.setEnabled(false);
    }

    @OnClick(R.id.btnForSave)
    public void onSave() {
        if(edtTxtFirstName.getText().toString().trim().length() > 0) {
            if(edtTxtLastName.getText().toString().trim().length() > 0) {
                if(edtTxtPhNo.getText().toString().trim().length() > 0) {
                    if(Utils.isValidMobile(edtTxtPhNo.getText().toString().trim())) {
                        if(edtTxtMail.getText().toString().trim().length() > 0) {
                            if(Utils.isEmailValid(edtTxtMail.getText().toString().trim())) {
                                FragmentManager fragmentManager = getFragmentManager();
                                SessionStores.USER_PROFILE = new UserProfile(edtTxtFirstName.getText().toString().trim(), edtTxtLastName.getText().toString().trim(),
                                        edtTxtPhNo.getText().toString().trim(), edtTxtMail.getText().toString().trim());

                                if(new SessionStores(getActivity()).getAccessToken() != null || new SessionStores(getActivity()).getAccessToken().length() > 0) { //if access token present then update profile api calls
                                    new UpdateProfileTasks(getActivity(), SessionStores.USER_PROFILE.updateProfileDate(), fragmentManager);
                                } else { //else register task
                                    new RegisterTask(getActivity(), SessionStores.USER_PROFILE.register(), fragmentManager);
                                }
                            } else {
                                Utils.ShowAlert(getActivity(), "Email is invalid");
                            }
                        } else {
                            Utils.ShowAlert(getActivity(), "Email field must not be empty");
                        }
                    } else {
                        Utils.ShowAlert(getActivity(), "Phone number is invalid");
                    }
                } else {
                    Utils.ShowAlert(getActivity(), "Phone number field must not be empty");
                }
            } else {
                Utils.ShowAlert(getActivity(), "Lastname field must not be empty");
            }
        } else {
            Utils.ShowAlert(getActivity(), "Firstname field must not be empty");
        }
    }

    public void backButton(){
        //Fragment replaces
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Settings fragment = new Settings();
        fragmentTransaction.replace(R.id.realtabcontent, fragment);
        fragmentTransaction.commit();
    }
}
