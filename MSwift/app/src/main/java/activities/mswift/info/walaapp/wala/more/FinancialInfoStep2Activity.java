package activities.mswift.info.walaapp.wala.more;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.adapter.SavingsTypeSpinnerAdapter;
import activities.mswift.info.walaapp.wala.asyntasks.AddInitialValue;
import activities.mswift.info.walaapp.wala.asyntasks.CreateWalletTask;
import activities.mswift.info.walaapp.wala.asyntasks.FinancialInfo3Task;
import activities.mswift.info.walaapp.wala.asyntasks.GetSavingsTask;
import activities.mswift.info.walaapp.wala.asyntasks.GetWalletInfo;
import activities.mswift.info.walaapp.wala.asyntasks.UseInitialValue;
import activities.mswift.info.walaapp.wala.model.SavingsTypeModel;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.transactions.Transaction;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 12/7/2015.
 */
public class FinancialInfoStep2Activity extends Fragment {

    private View v;
    private Button btnForSave, btnForSkip;
    public static EditText edittextForAmnt;
    private RelativeLayout layForTypesSpinner;
    private ListView listForSpinner;
    public static TextView txtForType;
    private ArrayList<SavingsTypeModel> savingsTypeArray = new ArrayList<>();
    private boolean listClicks = false;
    public static String savingsType = "", curDate, savings, savingsTypeBackend;
    public static long number;
    private Calendar cal;
    private int day, month, year;
    private String tag = "savings";
    public static String oldAmount = "0.0", resultBackend = "0";
    private MixpanelAPI mixpanelAPI;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        ((TabHostFragments) getActivity()).tabColorWhite();

        v = inflater.inflate(R.layout.financial_savings, container, false);
        btnForSave = (Button) v.findViewById(R.id.btnForSave);
        btnForSkip = (Button) v.findViewById(R.id.btnForSkip);
        edittextForAmnt = (EditText) v.findViewById(R.id.editTxtForSavings);
        layForTypesSpinner = (RelativeLayout) v.findViewById(R.id.layForChooseSpinner);
        listForSpinner = (ListView) v.findViewById(R.id.listViewForSpinner);
        txtForType = (TextView) v.findViewById(R.id.txtForSavingsSelected);

        savingsTypeArray = Utils.getSavingsTypes(getActivity());
        listForSpinner.setAdapter(new SavingsTypeSpinnerAdapter(getActivity(), savingsTypeArray));

        String accNumber = SessionStores.getAccNumberStep2(getActivity());
        if (accNumber != null && accNumber.length() != 0) {
            new GetSavingsTask(getActivity(), accNumber);
        }

        // mixpanel screen tracking
        mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
        mixpanelAPI.track(getString(R.string.mixpanel_financial_info_screen2));

        //getting calendar instance
        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        //formatting current date
        curDate = (Utils.calculateDate(day) + "/" + Utils.calculateDate(month + 1) + "/" + Utils.calculateDate(year));

        //create wallet for saving and debt
        savings = SessionStores.getSavingsWalletNumber(getActivity());
        if (savings != null && savings.length() != 0) {
            new GetWalletInfo(getActivity(), savings, tag);
        } else {
            new CreateWalletTask(getActivity());
        }

        layForTypesSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TabHostFragments) getActivity()).hideKeyboard();
                listForSpinner.setVisibility(View.VISIBLE);
            }
        });

        listForSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listClicks = true;
                listForSpinner.setVisibility(View.GONE);
                savingsType = savingsTypeArray.get(i).getType();
                txtForType.setText(savingsType);
                if (savingsType.equals("Bank")) {
                    savingsType = "SavingsAccount";
                    savingsTypeBackend = "bank";
                } else if (savingsType.equals("Home")) {
                    savingsType = "CashSavingsHome";
                    savingsTypeBackend = "home";
                } else {
                    savingsType = "CashSavingsOther";
                    savingsTypeBackend = "other";
                }
            }
        });

        btnForSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TabHostFragments) getActivity()).hideKeyboard();
                if (txtForType.getText().toString().equals("Choose...")) {
                    listClicks = false;
                } else if (txtForType.getText().toString().length() > 0) {
                    listClicks = true;
                    savingsType = txtForType.getText().toString();
                    if (savingsType.equals("Bank")) {
                        savingsType = "SavingsAccount";
                        savingsTypeBackend = "bank";
                    } else if (savingsType.equals("Home")) {
                        savingsType = "CashSavingsHome";
                        savingsTypeBackend = "home";
                    } else {
                        savingsType = "CashSavingsOther";
                        savingsTypeBackend = "other";
                    }
                }

                number = (long) Math.floor(Math.random() * 9000000000L) + 1000000000L;
                try {
                    if (edittextForAmnt.getText().toString().trim().length() > 0) {
                        if (edittextForAmnt.getText().toString().trim().length() <= 10) {
                            if (listClicks == true) {

                                //mixpanel save tracking
                                mixpanelAPI.track(getString(R.string.mixpanel_financial_info_save2));

                                final double tmpNewAmount = Double.parseDouble(edittextForAmnt.getText().toString());
                                final double tmpOldAmount = Double.parseDouble(oldAmount);

                                //check if save wallet number exist first and add value to savings wallet
                                savings = SessionStores.getSavingsWalletNumber(getActivity());
                                Transaction transaction = new Transaction(getActivity(), 11);

                                //entering amount for first time
                                if (oldAmount.equals("0.0")) {

                                    Map<String, String> params1 = new HashMap<>();
                                    params1.put("WalletCode", "" + savings);
                                    params1.put("Date", "" + curDate);
                                    params1.put("Amount", "" + edittextForAmnt.getText().toString());
                                    params1.put("Reason", "" + "Opening balance");
                                    params1.put("SubCategoryCode", "" + transaction.getTransactionMapping());

                                    Constants.FINANCIAL_TYPE = "savings";
                                    FragmentManager fragmentManager = getFragmentManager();
                                    if (SessionStores.getFinancialStep1(getActivity()) == null || SessionStores.getFinancialStep3(getActivity()) == null || SessionStores.getFinancialStep4(getActivity()) == null) {
                                        resultBackend = "0";
                                    } else {
                                        resultBackend = "1";
                                    }
                                    new AddInitialValue(getActivity(), params1, fragmentManager, savingsTypeBackend, edittextForAmnt.getText().toString());

                                    //update account first time
                                    updateAccount();
                                } else {

                                    //if amount stays the same
                                    if (tmpNewAmount == tmpOldAmount) {

                                        moveToNextStep();
                                        updateAccount();
                                    } else {
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("Balance adjustment")
                                                .setMessage("Set savings balance to: " + edittextForAmnt.getText().toString() + " ?")
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        if (tmpNewAmount > tmpOldAmount) {
                                                            double tmpResult = tmpNewAmount - tmpOldAmount;

                                                            //add value (make as income)

                                                            Transaction transaction = new Transaction(getActivity(), 11);
                                                            Map<String, String> params1 = new HashMap<>();
                                                            params1.put("WalletCode", "" + savings);
                                                            params1.put("Date", "" + curDate);
                                                            params1.put("Amount", "" + tmpResult);
                                                            params1.put("Reason", "" + "Balance adjustment");
                                                            params1.put("SubCategoryCode", "" + transaction.getTransactionMapping());

                                                            Constants.FINANCIAL_TYPE = "savings";
                                                            FragmentManager fragmentManager = getFragmentManager();
                                                            if (SessionStores.getFinancialStep1(getActivity()) == null || SessionStores.getFinancialStep3(getActivity()) == null || SessionStores.getFinancialStep4(getActivity()) == null) {
                                                                resultBackend = "0";
                                                            } else {
                                                                resultBackend = "1";
                                                            }
                                                            new AddInitialValue(getActivity(), params1, fragmentManager, savingsTypeBackend, "" + tmpNewAmount);
                                                        } else {
                                                            double tmpResult = tmpOldAmount - tmpNewAmount;

                                                            //use value (make as expence)
                                                            Transaction transaction = new Transaction(getActivity(), 10);
                                                            Map<String, String> params1 = new HashMap<>();
                                                            params1.put("WalletCode", "" + savings);
                                                            params1.put("Date", "" + curDate);
                                                            params1.put("Amount", "" + tmpResult);
                                                            params1.put("Reason", "" + "Balance adjustment");
                                                            params1.put("SubCategoryCode", "" + transaction.getTransactionMapping());

                                                            Constants.FINANCIAL_TYPE = "savings";
                                                            FragmentManager fragmentManager = getFragmentManager();
                                                            if (SessionStores.getFinancialStep1(getActivity()) == null || SessionStores.getFinancialStep3(getActivity()) == null || SessionStores.getFinancialStep4(getActivity()) == null) {
                                                                resultBackend = "0";
                                                            } else {
                                                                resultBackend = "1";
                                                            }
                                                            new UseInitialValue(getActivity(), params1, fragmentManager, savingsTypeBackend, "" + tmpNewAmount);
                                                        }

                                                        //update account
                                                        updateAccount();

                                                    }
                                                })
                                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        moveToNextStep();
                                                    }
                                                })
                                                .setIcon(R.drawable.dollar_image)
                                                .show();
                                    }
                                }

                            } else {
                                Utils.ShowAlert(getActivity(), "Please choose the savings option");
                            }
                        } else {
                            Utils.ShowAlert(getActivity(), "Please enter the amount of money you have saved as of today (less than 11 digits).");
                        }
                    } else {
                        Utils.ShowAlert(getActivity(), "Please enter the amount of money you have saved as of today.");
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Please enter a valid input", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnForSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TabHostFragments) getActivity()).hideKeyboard();
                //mixpanel skip tracking
                mixpanelAPI.track(getString(R.string.mixpanel_financial_info_skip2));
                moveToNextStep();
            }
        });

        return v;
    }

    public void moveToNextStep() {
        /** Move to step 2 fragment */
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FinancialInfoStep3Activity fragment = new FinancialInfoStep3Activity();
        fragmentTransaction.replace(R.id.realtabcontent, fragment);
        fragmentTransaction.commit();
    }

    public void updateAccount() {
        Map<String, String> params = new HashMap<>();
        params.put("AccountNumber", "" + number);
        params.put("AccountName", "" + SessionStores.getUserName(getActivity()));
        params.put("AccountType", "" + savingsType);
        params.put("ProductVariant", "");
        params.put("Tip", "");
        params.put("Portfolio", "");
        params.put("Adviser", "");
        params.put("ProdctProvider", "");
        params.put("FinancialItemType", "Asset");
        params.put("Owner", "Self");
        params.put("OpeningBalance", "0.0");
        params.put("CurrentBalance", "" + edittextForAmnt.getText().toString());
        params.put("InterestRate", "0.0");
        params.put("Currency", "");

        FragmentManager fragmentManager = getFragmentManager();
        new FinancialInfo3Task(getActivity(), params, fragmentManager, savingsTypeBackend);
    }
}
