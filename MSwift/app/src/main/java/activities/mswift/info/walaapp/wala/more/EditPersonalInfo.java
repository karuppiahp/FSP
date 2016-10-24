package activities.mswift.info.walaapp.wala.more;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.adapter.SpinnerListAdapter;
import activities.mswift.info.walaapp.wala.asyntasks.GetPersonalInfoTask;
import activities.mswift.info.walaapp.wala.asyntasks.UpdatePersonalInfoTask;
import activities.mswift.info.walaapp.wala.support.DatePickerFragment;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 12/12/2015.
 */
public class EditPersonalInfo extends Fragment {

    private View v;
    Button btnForSave;
    public static EditText editTxtForFirstName, editTxtForMiddleName, editTxtForLastName, editTxtForEmail, editTxtForPhoneNumber, editTxtForCity;
    public static TextView txtForCountry, txtForDob;
    private LinearLayout layForDob;

    ListView listView;
    RelativeLayout layForCountry;
    String spinnerClicks;
    private MixpanelAPI mixpanelAPI;
    private String countryCode = "UK";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        ((TabHostFragments) getActivity()).tabColorWhite();

        v = inflater.inflate(R.layout.personal_info_edit, container, false);
        btnForSave = (Button) v.findViewById(R.id.btnForSavePersonalInfo);
        editTxtForFirstName = (EditText) v.findViewById(R.id.editTxtForFirstName);
        editTxtForMiddleName = (EditText) v.findViewById(R.id.editTxtForMiddleName);
        editTxtForLastName = (EditText) v.findViewById(R.id.editTxtForLastName);
        editTxtForEmail = (EditText) v.findViewById(R.id.editTxtForEmailAddress);
        editTxtForPhoneNumber = (EditText) v.findViewById(R.id.editTxtForPhoneNo);
        editTxtForCity = (EditText) v.findViewById(R.id.editTxtForCity);
        txtForDob = (TextView) v.findViewById(R.id.txtForDobSelected);
        txtForCountry = (TextView) v.findViewById(R.id.txtForCountrySpinner);
        layForDob = (LinearLayout) v.findViewById(R.id.layForDob);


        layForCountry = (RelativeLayout) v.findViewById(R.id.layForCountrySpinner);
        listView = (ListView) v.findViewById(R.id.listViewForSpinner);

        if (Utils.isOnline()) {
            if (!(Constants.COUNTRY_ARRAY.size() > 0))
                Utils.getCountry();
        } else {
            Utils.ShowAlert(getActivity(), "Network connectivity is not available");
        }

        new GetPersonalInfoTask(getActivity());

        // mixpanel screen tracking
        mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
        mixpanelAPI.track(getString(R.string.mixpanel_more_screen_personal_info_screen));

        //getting country code
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(getActivity().getApplicationContext().TELEPHONY_SERVICE);
        countryCode = tm.getNetworkCountryIso();

        if (countryCode != null && countryCode.length() != 2) {
            countryCode = this.getResources().getConfiguration().locale.getDefault().getCountry().toLowerCase();
        }

        btnForSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TabHostFragments) getActivity()).hideKeyboard();
                if (editTxtForFirstName.getText().toString().length() > 0) {
                    if (editTxtForLastName.getText().toString().length() > 0) {
                        if (editTxtForPhoneNumber.getText().toString().length() > 0) {
                            if (Utils.isValidMobile(editTxtForPhoneNumber.getText().toString(), countryCode)) {
                                ((TabHostFragments) getActivity()).removeChild(); // remove child view
                                Map<String, String> param = new HashMap<String, String>();
                                param.put("FirstName", editTxtForFirstName.getText().toString());
                                param.put("MiddleName", editTxtForMiddleName.getText().toString());
                                param.put("LastName", editTxtForLastName.getText().toString());
                                param.put("Gender", "Male");
                                param.put("DateOfBirth", txtForDob.getText().toString());
                                param.put("PhoneNumber", editTxtForPhoneNumber.getText().toString());
                                param.put("OccupationStatus", "");
                                param.put("AddressLine1", "");
                                param.put("AddressLine2", "");
                                param.put("Suburb", "");
                                param.put("State", "");
                                param.put("PostCode", "");
                                param.put("NumChildren", "1");
                                param.put("CountryCode", Constants.COUNTRY_CODE);
                                param.put("City", editTxtForCity.getText().toString());
                                FragmentManager fragmentManager = getFragmentManager();
                                new UpdatePersonalInfoTask(getActivity(), param, fragmentManager);
                            } else {
                                Utils.ShowAlert(getActivity(), "Please enter a valid Phone number, the phone number must contain valid country code without (+) after that your phone number will be follows.");
                            }
                        } else {
                            Utils.ShowAlert(getActivity(), "Phone number field must not be empty");
                        }
                    } else {
                        Utils.ShowAlert(getActivity(), "Please enter your LastName");
                    }
                } else {
                    Utils.ShowAlert(getActivity(), "Please enter your FirstName");
                }
            }
        });

        layForDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TabHostFragments) getActivity()).hideKeyboard();
                listView.setVisibility(View.GONE);
                DatePickerFragment newFragment = new DatePickerFragment(txtForDob);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        if (!(Constants.GENDER_ARRAY.size() > 0))
            Constants.GENDER_ARRAY = Utils.getGenderValues(getActivity());

        //Listview for country list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listView.setVisibility(View.GONE);
                if (spinnerClicks.equals("country")) {
                    txtForCountry.setText(Constants.COUNTRY_ARRAY.get(i).getName());
                    Constants.COUNTRY = Constants.COUNTRY_ARRAY.get(i).getName();
                    Constants.COUNTRY_ID = Constants.COUNTRY_ARRAY.get(i).getId();

                    Map<String, String> countries = new HashMap<>();
                    for (String iso : Locale.getISOCountries()) {
                        Locale li = new Locale("", iso);
                        countries.put(li.getDisplayCountry(), iso);
                    }

                    SessionStores.saveCurrencyCode(countries.get(Constants.COUNTRY), getActivity());
                    Constants.COUNTRY_CODE = Constants.COUNTRY_ARRAY.get(i).getCountryCode();
                }
            }
        });

        layForCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TabHostFragments) getActivity()).hideKeyboard();
                listView.setVisibility(View.VISIBLE);
                spinnerClicks = "country";
                listView.setAdapter(new SpinnerListAdapter(getActivity(), spinnerClicks));
            }
        });

        return v;
    }
}
