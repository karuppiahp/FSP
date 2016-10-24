package activities.mswift.info.walaapp.wala.more;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.adapter.EmpStatusAdapter;
import activities.mswift.info.walaapp.wala.adapter.IncomeFreqAdapter;
import activities.mswift.info.walaapp.wala.adapter.IncomeTypeAdapter;
import activities.mswift.info.walaapp.wala.asyntasks.FinancialInfo2Task;
import activities.mswift.info.walaapp.wala.asyntasks.GetMemberBudgetStep1Task;
import activities.mswift.info.walaapp.wala.model.EmploymentStatusModel;
import activities.mswift.info.walaapp.wala.model.IncomeFrequencyModel;
import activities.mswift.info.walaapp.wala.model.IncomeTypeModel;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.FinancialInfoUtils;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 12/7/2015.
 */
public class FinancialInfoStep1Activity extends Fragment {


    private View v;
    Button btnForSave, btnForSkip;
    public static EditText editTxtAvgMonthlyIncm;
    private RelativeLayout layForEmpStatus, layForIncomeType, layForIncomeFreq;
    public static TextView txtForEmpStatus, txtForIncomeType, txtForIncomeFreq;
    private ListView listForSpinner;
    private ArrayList<EmploymentStatusModel> empStatusArray = new ArrayList<>();
    private ArrayList<IncomeTypeModel> incomeTypeArray = new ArrayList<>();
    private ArrayList<IncomeFrequencyModel> incomeFreqArray = new ArrayList<>();
    public static String spinnerClicks, empStatus = "", incomeType = "", incomeFreq = "", empStatusBackend, incomeTypeBackend, incomeFreqBackend;
    public static boolean empStatusClicks = false, incomeTypeClicks = false, incomeFreqClicks = false;
    private MixpanelAPI mixpanelAPI;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        ((TabHostFragments) getActivity()).tabColorWhite();
        Constants.firstTime = true;
        v = inflater.inflate(R.layout.financial_income, container, false);
        editTxtAvgMonthlyIncm = (EditText) v.findViewById(R.id.editTxtForAvgMonthlyIncome);
        layForEmpStatus = (RelativeLayout) v.findViewById(R.id.layForEmpStatusSpinner);
        layForIncomeType = (RelativeLayout) v.findViewById(R.id.layForIncomeTypeSpinner);
        layForIncomeFreq = (RelativeLayout) v.findViewById(R.id.layForIncomeFreqSpinner);
        txtForEmpStatus = (TextView) v.findViewById(R.id.txtForEmpStatusSelected);
        txtForIncomeType = (TextView) v.findViewById(R.id.txtForIncomeTypeSelected);
        txtForIncomeFreq = (TextView) v.findViewById(R.id.txtForIncomeFreqSelected);
        listForSpinner = (ListView) v.findViewById(R.id.listViewForSpinner);
        btnForSave = (Button) v.findViewById(R.id.btnForSave);
        btnForSkip = (Button) v.findViewById(R.id.btnForSkip);

        empStatusArray = Utils.getEmpStatus(getActivity());
        incomeTypeArray = Utils.getIncomeType(getActivity());
        incomeFreqArray = Utils.getIncomeFreq(getActivity());

        // mixpanel screen tracking
        mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
        mixpanelAPI.track(getString(R.string.mixpanel_financial_info_screen1));

        new GetMemberBudgetStep1Task(getActivity());

        layForEmpStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TabHostFragments) getActivity()).hideKeyboard();
                spinnerClicks = "empStatus";
                listForSpinner.setVisibility(View.VISIBLE);
                listForSpinner.setAdapter(new EmpStatusAdapter(getActivity(), empStatusArray));
            }
        });

        layForIncomeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TabHostFragments) getActivity()).hideKeyboard();
                spinnerClicks = "incomeType";
                listForSpinner.setVisibility(View.VISIBLE);
                listForSpinner.setAdapter(new IncomeTypeAdapter(getActivity(), incomeTypeArray));
            }
        });

        layForIncomeFreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TabHostFragments) getActivity()).hideKeyboard();
                spinnerClicks = "incomeFreq";
                listForSpinner.setVisibility(View.VISIBLE);
                listForSpinner.setAdapter(new IncomeFreqAdapter(getActivity(), incomeFreqArray));
            }
        });

        listForSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listForSpinner.setVisibility(View.GONE);
                if (spinnerClicks.equals("empStatus")) {
                    empStatusClicks = true;
                    empStatus = Utils.empStatus(empStatusArray.get(i).getStatus());
                    empStatusBackend = FinancialInfoUtils.empStatusBackend(empStatusArray.get(i).getStatus());
                    txtForEmpStatus.setText(empStatusArray.get(i).getStatus());
                } else if (spinnerClicks.equals("incomeType")) {
                    incomeTypeClicks = true;
                    incomeType = Utils.incomeType(incomeTypeArray.get(i).getType());
                    incomeTypeBackend = FinancialInfoUtils.incomeTypeBackend(incomeTypeArray.get(i).getType());
                    txtForIncomeType.setText(incomeTypeArray.get(i).getType());
                } else if (spinnerClicks.equals("incomeFreq")) {
                    incomeFreqClicks = true;
                    incomeFreq = Utils.frequency(incomeFreqArray.get(i).getFreq());
                    incomeFreqBackend = FinancialInfoUtils.frequencyBackend(incomeFreqArray.get(i).getFreq());
                    txtForIncomeFreq.setText(incomeFreqArray.get(i).getFreq());
                }
            }
        });

        btnForSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TabHostFragments) getActivity()).hideKeyboard();
                if (txtForEmpStatus.getText().toString().equals("Choose...")) {
                    empStatusClicks = false;
                } else if (txtForEmpStatus.getText().toString().length() > 0) {
                    empStatus = Utils.empStatus(txtForEmpStatus.getText().toString());
                    empStatusClicks = true;
                    empStatusBackend = FinancialInfoUtils.empStatusBackend(txtForEmpStatus.getText().toString());
                }


                if (txtForIncomeType.getText().toString().equals("Choose...")) {
                    incomeTypeClicks = false;
                } else if (txtForIncomeType.getText().toString().length() > 0) {
                    incomeType = Utils.incomeType(txtForIncomeType.getText().toString());
                    incomeTypeClicks = true;
                    incomeTypeBackend = FinancialInfoUtils.incomeTypeBackend(txtForIncomeType.getText().toString());
                }


                if (txtForIncomeFreq.getText().toString().equals("Choose...")) {
                    incomeFreqClicks = false;
                } else if (txtForIncomeFreq.getText().toString().length() > 0) {
                    incomeFreq = Utils.frequency(txtForIncomeFreq.getText().toString());
                    incomeFreqClicks = true;
                    incomeFreqBackend = FinancialInfoUtils.frequencyBackend(txtForIncomeFreq.getText().toString());
                }
                try {
                    if (editTxtAvgMonthlyIncm.getText().toString().trim().length() > 0) {
                        String tempavgMonthlyincome = editTxtAvgMonthlyIncm.getText().toString().split("\\.")[0];
                        if (tempavgMonthlyincome.length() <= 10) {
                            if (empStatusClicks == true) {
                                if (incomeTypeClicks == true) {
                                    if (incomeFreqClicks == true) {
                                        //mixpanel save tracking
                                        mixpanelAPI.track(getString(R.string.mixpanel_financial_info_save1));

                                        Map<String, String> params = new HashMap<>();
                                        params.put("Amount", "" + editTxtAvgMonthlyIncm.getText().toString());
                                        params.put("Category", "");
                                        params.put("SubCategory", "" + incomeType);
                                        params.put("Frequency", "" + incomeFreq);
                                        Map<String, String> paramsBckend = new HashMap<>();
                                        paramsBckend.put("user_email", "" + SessionStores.getUserEmail(getActivity()));
                                        paramsBckend.put("average_monthly_income", "" + editTxtAvgMonthlyIncm.getText().toString());
                                        paramsBckend.put("employment_status", "" + empStatusBackend);
                                        paramsBckend.put("income_type", "" + incomeTypeBackend);
                                        paramsBckend.put("salary_paid_type", "" + incomeFreqBackend);
                                        if (SessionStores.getFinancialStep2(getActivity()) == null || SessionStores.getFinancialStep3(getActivity()) == null || SessionStores.getFinancialStep4(getActivity()) == null) {
                                            paramsBckend.put("result", "0");
                                        } else {
                                            paramsBckend.put("result", "1");
                                        }
                                        FragmentManager fragmentManager = getFragmentManager();
                                        new FinancialInfo2Task(getActivity(), params, fragmentManager, empStatus, paramsBckend);
                                    } else {
                                        Utils.ShowAlert(getActivity(), "Please select the income frequency");
                                    }
                                } else {
                                    Utils.ShowAlert(getActivity(), "Please select the income type");
                                }
                            } else {
                                Utils.ShowAlert(getActivity(), "Please select the employment status");
                            }
                        } else {
                            Utils.ShowAlert(getActivity(), "Please enter your average monthly income (less than 11 digits)");
                        }
                    } else {
                        Utils.ShowAlert(getActivity(), "Please enter your average monthly income");
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Please enter a valid input", Toast.LENGTH_LONG).show();
                }
            }
        });


        btnForSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mixpanel skip tracking
                mixpanelAPI.track(getString(R.string.mixpanel_financial_info_skip1));
                /** Move to step 2 fragment */
                ((TabHostFragments) getActivity()).hideKeyboard();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FinancialInfoStep2Activity fragment = new FinancialInfoStep2Activity();
                fragmentTransaction.replace(R.id.realtabcontent, fragment);
                fragmentTransaction.commit();
            }
        });

        return v;
    }

}
