package activities.mswift.info.walaapp.wala.transactions;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.Analitics;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by kruno on 22.01.16..
 */
public class TransactionsAdd extends Fragment implements View.OnClickListener {

    private View v;
    public static TextView txtForAddTransactionValue, txtForDotSelected, tw_transaction_tip;
    public static EditText editTxtForDescription, editTxtForDebt;
    private String walletNumber;
    private String walletNumberToLower;
    private Button btnForSaveTransactionAdd;
    private ImageView im_transaction;
    private ImageButton transaction_back_button;
    private Calendar cal;
    private int day, month, year, transactionId;
    private MixpanelAPI mixpanelAPI;
    private long mLastClickTime = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if (container == null) {
            return null;
        }

        ((TabHostFragments) getActivity()).tabColorWhite();

        v = inflater.inflate(R.layout.transactions_add, container, false);

        txtForAddTransactionValue = (TextView) v.findViewById(R.id.txtForAddTransactionValue);
        txtForDotSelected = (TextView) v.findViewById(R.id.txtForDotSelected);
        editTxtForDebt = (EditText) v.findViewById(R.id.editTxtForDebt);
        editTxtForDescription = (EditText) v.findViewById(R.id.editTxtForDescription);
        btnForSaveTransactionAdd = (Button) v.findViewById(R.id.btnForSaveTransactionAdd);
        im_transaction = (ImageView) v.findViewById(R.id.im_transaction);
        tw_transaction_tip = (TextView) v.findViewById(R.id.tw_transaction_tip);
        transaction_back_button = (ImageButton) v.findViewById(R.id.transaction_edit_back_button);

        //get bundle
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            transactionId = bundle.getInt("transaction");
        }

        Transaction transaction = new Transaction(getActivity(), transactionId);

        txtForAddTransactionValue.setText(transaction.getTransactionName() + " Transaction");
        im_transaction.setImageResource(transaction.getTransactionIcon().getResourceId(transactionId, -1));
        tw_transaction_tip.setText(transaction.getTransactionTip());

        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        txtForDotSelected.setText(Utils.calculateDate(day) + "/" + Utils.calculateDate(month + 1) + "/" + Utils.calculateDate(year));

        editTxtForDebt.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        // mixpanel screen tracking
        mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
        mixpanelAPI.track(getString(R.string.mixpanel_add_transaction_screen_add_individual_transaction_screen), Analitics.addTransaction(transaction.getTransactionName() + " Transaction"));

        btnForSaveTransactionAdd.setOnClickListener(this);
        transaction_back_button.setOnClickListener(this);
        txtForDotSelected.setOnClickListener(this);

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((TabHostFragments) getActivity()).removeChild();
    }

    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if (v.getId() == R.id.btnForSaveTransactionAdd) {
            ((TabHostFragments) getActivity()).hideKeyboard();
            try {
                if (editTxtForDebt.getText().toString().trim().length() > 0) {
                    if (!editTxtForDebt.getText().toString().trim().equals("0") && Double.parseDouble(editTxtForDebt.getText().toString().trim()) > 0) {
                        if (editTxtForDebt.getText().toString().trim().length() <= 10) {
                            if (editTxtForDescription.getText().toString().length() <= 100) {
                                if (SessionStores.getSavingsWalletNumber(getActivity()) != null) {
                                    //checking transaction id to see are we gonna call savings or debt wallet;
                                    if (transactionId < 12) {
                                        walletNumber = SessionStores.getSavingsWalletNumber(getActivity());
                                    } else {
                                        walletNumber = SessionStores.getDebtWalletNumber(getActivity());
                                        walletNumberToLower = SessionStores.getSavingsWalletNumber(getActivity());
                                    }

                                    String tmpDescription;
                                    if (editTxtForDescription.getText().toString().trim().length() < 1) {
                                        tmpDescription = "-";
                                    } else {
                                        tmpDescription = editTxtForDescription.getText().toString().trim();
                                    }

                                    Transaction transaction = new Transaction(getActivity(), transactionId);

                                    //current wallet number
                                    Map<String, String> params = new HashMap<>();
                                    params.put("WalletCode", "" + walletNumber);
                                    params.put("Date", "" + txtForDotSelected.getText().toString());
                                    params.put("Amount", "" + editTxtForDebt.getText().toString());
                                    params.put("Reason", "" + tmpDescription);
                                    params.put("SubCategoryCode", "" + transaction.getTransactionMapping());

                                    //wallet to lower
                                    Map<String, String> params1 = new HashMap<>();
                                    params1.put("WalletCode", "" + walletNumberToLower);
                                    params1.put("Date", "" + txtForDotSelected.getText().toString());
                                    params1.put("Amount", "" + editTxtForDebt.getText().toString());
                                    params1.put("Reason", "" + tmpDescription);
                                    params1.put("SubCategoryCode", "" + transaction.getTransactionMapping());

                                    //mixpanel track save transaction
                                    mixpanelAPI.track(getString(R.string.mixpanel_add_transaction_screen_save_transaction),
                                            Analitics.saveTransaction(txtForAddTransactionValue.getText().toString(),
                                                    editTxtForDebt.getText().toString(), txtForDotSelected.getText().toString(), transactionId));

                                    FragmentManager fragmentManager = getFragmentManager();
                                    transaction.addTransactionToVallet(params, fragmentManager, params1);
                                } else {
                                    Toast.makeText(getActivity(), "Please finish Financial profile", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Transaction Describtion character must be less than count 100 character", Toast.LENGTH_LONG).show();

                            }
                        } else {
                            Toast.makeText(getActivity(), "Transaction amount must be less than count 11", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Transaction amount must be greater than 0", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please enter transaction amount", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Please enter a valid number", Toast.LENGTH_LONG).show();
            }
        }

        if (v.getId() == R.id.txtForDotSelected) {
            ((TabHostFragments) getActivity()).hideKeyboard();
            DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
                    0, datePickerListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH));
            datePicker.setCancelable(false);
            datePicker.setTitle("Select the date");
            datePicker.show();
        }

        if (v.getId() == R.id.transaction_edit_back_button) {
            ((TabHostFragments) getActivity()).hideKeyboard();
            ((TabHostFragments) getActivity()).removeChild();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            TransactionActivity fragment = new TransactionActivity();
            fragmentTransaction.replace(R.id.realtabcontent, fragment);
            fragmentTransaction.commit();
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            int year1 = selectedYear;
            int month1 = selectedMonth + 1;
            int day1 = selectedDay;
            txtForDotSelected.setText(Utils.calculateDate(day1) + "/" + Utils.calculateDate(month1) + "/" + Utils.calculateDate(year1));
        }
    };
}
