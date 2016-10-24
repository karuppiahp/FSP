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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.asyntasks.AddInitialValue;
import activities.mswift.info.walaapp.wala.asyntasks.CreateDebtWalletTask;
import activities.mswift.info.walaapp.wala.asyntasks.FinancialInfo4Task;
import activities.mswift.info.walaapp.wala.asyntasks.GetDebtTask;
import activities.mswift.info.walaapp.wala.asyntasks.GetWalletInfo;
import activities.mswift.info.walaapp.wala.asyntasks.UseInitialValue;
import activities.mswift.info.walaapp.wala.model.DebtOweTypeModel;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.transactions.Transaction;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 12/10/2015.
 */
public class FinancialInfoStep3Activity extends Fragment {

    private View v;
    private Button btnForSave, btnForSkip;
    public static EditText editTxtForCurrentlyOwe, editTextForDebtOwed;
    private ArrayList<DebtOweTypeModel> debtOweTypeArray = new ArrayList<>();
    public static String debtOweType = "", savingsTypeBackend;
    public static CheckBox chkBoxForLoan, chkBoxForCreditCard, chkBoxForSchool, chkBoxForOther;
    public static long number;
    private Calendar cal;
    private int day, month, year;
    private String curDate;
    String debt;
    private String tag = "debt";
    public static String oldAmount = "0.0", resultBackend = "0";
    private MixpanelAPI mixpanelAPI;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        ((TabHostFragments) getActivity()).tabColorWhite();

        v = inflater.inflate(R.layout.financial_debt, container, false);
        editTxtForCurrentlyOwe = (EditText) v.findViewById(R.id.editTxtForDebtCurrent);
        btnForSave = (Button) v.findViewById(R.id.btnForSave);
        btnForSkip = (Button) v.findViewById(R.id.btnForSkip);
        chkBoxForLoan = (CheckBox) v.findViewById(R.id.chkBoxForLoan);
        chkBoxForCreditCard = (CheckBox) v.findViewById(R.id.chkBoxForCreditCard);
        chkBoxForSchool = (CheckBox) v.findViewById(R.id.chkBoxForSchool);
        chkBoxForOther = (CheckBox) v.findViewById(R.id.chkBoxForOther);

        // mixpanel screen tracking
        mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
        mixpanelAPI.track(getString(R.string.mixpanel_financial_info_screen3));

        //getting calendar instance
        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        //formatting current date
        curDate = (Utils.calculateDate(day) + "/" + Utils.calculateDate(month + 1) + "/" + Utils.calculateDate(year));

        //get debt wallet code or create wallet for saving and debt
        debt = SessionStores.getDebtWalletNumber(getActivity());
        if (debt != null && debt.length() != 0) {
            new GetWalletInfo(getActivity(), debt, tag);
        } else {
            new CreateDebtWalletTask(getActivity());
        }

        String accNumber = SessionStores.getAccNumberStep3(getActivity());
        if (accNumber != null && accNumber.length() != 0) {
            new GetDebtTask(getActivity(), accNumber);
        }

        chkBoxForLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TabHostFragments) getActivity()).hideKeyboard();
                debtOweType = "PersonalLoan";
                savingsTypeBackend = "loan";
                setCheckBoxSelected(debtOweType);
            }
        });

        chkBoxForCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TabHostFragments) getActivity()).hideKeyboard();
                debtOweType = "CreditCardAccount";
                savingsTypeBackend = "credit_card";
                setCheckBoxSelected(debtOweType);
            }
        });

        chkBoxForSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TabHostFragments) getActivity()).hideKeyboard();
                debtOweType = "StudentLoan";
                savingsTypeBackend = "school";
                setCheckBoxSelected(debtOweType);
            }
        });

        chkBoxForOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TabHostFragments) getActivity()).hideKeyboard();
                debtOweType = "DebtOtherAccounts";
                savingsTypeBackend = "other";
                setCheckBoxSelected(debtOweType);
            }
        });

        btnForSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TabHostFragments) getActivity()).hideKeyboard();
                number = (long) Math.floor(Math.random() * 9000000000L) + 1000000000L;
                try {
                    if (editTxtForCurrentlyOwe.getText().toString().trim().length() > 0) {
                        if (editTxtForCurrentlyOwe.getText().toString().trim().length() <= 10) {
                            final double tmpNewAmount = Double.parseDouble(editTxtForCurrentlyOwe.getText().toString());
                            final double tmpOldAmount = Double.parseDouble(oldAmount);

                            if (debtOweType.length() > 0 || tmpNewAmount == 0) {

                                //mixpanel save tracking
                                mixpanelAPI.track(getString(R.string.mixpanel_financial_info_save3));

                                //check if save wallet number exist first
                                final String debt = SessionStores.getDebtWalletNumber(getActivity());
                                Transaction transaction = new Transaction(getActivity(), 12);

                                //entering amount for first time
                                if (oldAmount.equals("0.0")) {
                                    Map<String, String> params1 = new HashMap<>();
                                    params1.put("WalletCode", "" + debt);
                                    params1.put("Date", "" + curDate);
                                    params1.put("Amount", "" + editTxtForCurrentlyOwe.getText().toString());
                                    params1.put("Reason", "" + "Opening balance");
                                    params1.put("SubCategoryCode", "" + transaction.getTransactionMapping());

                                    Constants.FINANCIAL_TYPE = "debt";
                                    FragmentManager fragmentManager = getFragmentManager();
                                    if (SessionStores.getFinancialStep1(getActivity()) == null || SessionStores.getFinancialStep2(getActivity()) == null || SessionStores.getFinancialStep4(getActivity()) == null) {
                                        resultBackend = "0";
                                    } else {
                                        resultBackend = "1";
                                    }
                                    new AddInitialValue(getActivity(), params1, fragmentManager, savingsTypeBackend, editTxtForCurrentlyOwe.getText().toString());

                                    //update account first time
                                    updateAccount();

                                } else {
                                    //if amount stays the same
                                    if (tmpNewAmount == tmpOldAmount) {
                                        updateAccount();
                                    } else {
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("Balance adjustment")
                                                .setMessage("Set debt balance to: " + editTxtForCurrentlyOwe.getText().toString() + " ?")
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        if (tmpNewAmount > tmpOldAmount) {
                                                            double tmpResult = tmpNewAmount - tmpOldAmount;
                                                            //add value (make as loan)
                                                            Transaction transaction = new Transaction(getActivity(), 12);
                                                            Map<String, String> params1 = new HashMap<>();
                                                            params1.put("WalletCode", "" + debt);
                                                            params1.put("Date", "" + curDate);
                                                            params1.put("Amount", "" + tmpResult);
                                                            params1.put("Reason", "" + "Balance adjustment");
                                                            params1.put("SubCategoryCode", "" + transaction.getTransactionMapping());

                                                            Constants.FINANCIAL_TYPE = "debt";
                                                            FragmentManager fragmentManager = getFragmentManager();
                                                            if (SessionStores.getFinancialStep1(getActivity()) == null || SessionStores.getFinancialStep2(getActivity()) == null || SessionStores.getFinancialStep4(getActivity()) == null) {
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
                                                            params1.put("WalletCode", "" + debt);
                                                            params1.put("Date", "" + curDate);
                                                            params1.put("Amount", "" + tmpResult);
                                                            params1.put("Reason", "" + "Balance adjustment");
                                                            params1.put("SubCategoryCode", "" + transaction.getTransactionMapping());

                                                            Constants.FINANCIAL_TYPE = "debt";
                                                            FragmentManager fragmentManager = getFragmentManager();
                                                            if (SessionStores.getFinancialStep1(getActivity()) == null || SessionStores.getFinancialStep2(getActivity()) == null || SessionStores.getFinancialStep4(getActivity()) == null) {
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
                                Utils.ShowAlert(getActivity(), "Please choose the type of debt you owe.");
                            }
                        } else {
                            Utils.ShowAlert(getActivity(), "Please enter the amount of money you owe to others as of today (less than 11 digits).");
                        }
                    } else {
                        Utils.ShowAlert(getActivity(), "Please enter the amount of money you owe to others as of today.");
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
                mixpanelAPI.track(getString(R.string.mixpanel_financial_info_skip3));
                moveToNextStep();
            }
        });

        return v;
    }

    public void moveToNextStep() {
        /** Move to step 2 fragment */
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FinancialInfoStep4Activity fragment = new FinancialInfoStep4Activity();
        fragmentTransaction.replace(R.id.realtabcontent, fragment);
        fragmentTransaction.commit();
    }

    private void updateAccount() {
        Map<String, String> params = new HashMap<>();
        params.put("AccountNumber", "" + number);
        params.put("AccountName", "" + SessionStores.getUserName(getActivity()));
        params.put("AccountType", "" + debtOweType);
        params.put("ProductVariant", "");
        params.put("Tip", "");
        params.put("Portfolio", "");
        params.put("Adviser", "");
        params.put("ProdctProvider", "");
        params.put("FinancialItemType", "Liability");
        params.put("Owner", "Self");
        params.put("OpeningBalance", "0.0");
        params.put("CurrentBalance", "" + editTxtForCurrentlyOwe.getText().toString());
        params.put("InterestRate", "0.0");
        params.put("Currency", "");

        FragmentManager fragmentManager = getFragmentManager();
        new FinancialInfo4Task(getActivity(), params, fragmentManager, debtOweType, "");
    }

    public void setCheckBoxSelected(String s) {

        if (s.equals("PersonalLoan")) {

            if (chkBoxForLoan.isChecked()) {
                chkBoxForLoan.setChecked(true);
                chkBoxForCreditCard.setChecked(false);
                chkBoxForSchool.setChecked(false);
                chkBoxForOther.setChecked(false);
            } else {
                chkBoxForLoan.setChecked(false);
                debtOweType = "";
            }
        }

        if (s.equals("CreditCardAccount")) {

            if (chkBoxForCreditCard.isChecked()) {
                chkBoxForCreditCard.setChecked(true);
                chkBoxForLoan.setChecked(false);
                chkBoxForSchool.setChecked(false);
                chkBoxForOther.setChecked(false);
            } else {
                chkBoxForCreditCard.setChecked(false);
                debtOweType = "";
            }
        }

        if (s.equals("StudentLoan")) {

            if (chkBoxForSchool.isChecked()) {
                chkBoxForSchool.setChecked(true);
                chkBoxForLoan.setChecked(false);
                chkBoxForCreditCard.setChecked(false);
                chkBoxForOther.setChecked(false);

            } else {
                chkBoxForSchool.setChecked(false);
                debtOweType = "";
            }
        }

        if (s.equals("DebtOtherAccounts")) {

            if (chkBoxForOther.isChecked()) {
                chkBoxForOther.setChecked(true);
                chkBoxForLoan.setChecked(false);
                chkBoxForCreditCard.setChecked(false);
                chkBoxForSchool.setChecked(false);

            } else {
                chkBoxForOther.setChecked(false);
                debtOweType = "";
            }
        }
    }
}
