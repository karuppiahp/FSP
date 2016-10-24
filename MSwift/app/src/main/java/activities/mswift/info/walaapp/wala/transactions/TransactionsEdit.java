package activities.mswift.info.walaapp.wala.transactions;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.asyntasks.UpdateTransactionTask;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.Analitics;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by kruno on 11.02.16..
 */
public class TransactionsEdit extends Fragment implements View.OnClickListener {

    TextView txtForAddTransactionValue, txtForDotSelected, tw_transaction_tip;
    private EditText editTxtForDescription, editTxtForDebt;
    private ImageView im_transaction;
    String iD, description, tranAmount, dateposted, subCathegoryMapping, subCathegory;
    ImageButton transaction_edit_back_button;
    Button btnForSaveTransactionEdit;
    private static SimpleDateFormat sdf;
    private static Date ddObj;
    Calendar cal;
    int index;
    int TAG = 0;
    private MixpanelAPI mixpanelAPI;

    public TransactionsEdit() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.transactions_edit, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        txtForAddTransactionValue = (TextView) getActivity().findViewById(R.id.txtForAddTransactionValue);
        txtForDotSelected = (TextView) getActivity().findViewById(R.id.txtForDotSelected);

        editTxtForDebt = (EditText) getActivity().findViewById(R.id.editTxtForTransactionAmount);
        editTxtForDescription = (EditText) getActivity().findViewById(R.id.editTxtForTransactionDescription);

        im_transaction = (ImageView) getActivity().findViewById(R.id.im_transaction);
        tw_transaction_tip = (TextView) getActivity().findViewById(R.id.tw_transaction_tip);

        transaction_edit_back_button = (ImageButton) getActivity().findViewById(R.id.transaction_edit_back_button);
        btnForSaveTransactionEdit = (Button) getActivity().findViewById(R.id.btnForSaveTransactionEdit);

        //get bundle
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            subCathegoryMapping = bundle.getString("SubCategoryMapping");
            subCathegory = bundle.getString("SubCathegory");
            iD = bundle.getString("ID");
            description = bundle.getString("Description");
            tranAmount = bundle.getString("Tranamount");
            dateposted = bundle.getString("DatePosted");
            index = bundle.getInt("index");
        }

        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        // mixpanel screen tracking
        mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
        mixpanelAPI.track(getString(R.string.mixpanel_edit_transaction_screen));

        //get reference to Transaction class
        Transaction transaction = new Transaction(getActivity(), index);
        im_transaction.setImageResource(transaction.getTransactionIcon().getResourceId(index, -1));
        tw_transaction_tip.setText(transaction.getTransactionTip());
        txtForAddTransactionValue.setText(subCathegoryMapping + " Transaction");

        txtForDotSelected.setText(ddMMMMYYYYFormat(dateposted));
        editTxtForDebt.setText(tranAmount);
        //editTxtForDebt.setText(transaction.formatNegativeValue(tranAmount));
        editTxtForDescription.setText(description);

        transaction_edit_back_button.setOnClickListener(this);
        btnForSaveTransactionEdit.setOnClickListener(this);
        txtForDotSelected.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        FragmentManager fragmentManager = getFragmentManager();
        switch (v.getId()) {
            case R.id.transaction_edit_back_button:
                ((TabHostFragments) getActivity()).hideKeyboard();
                //switch to transactionActivity and pass value to viewpager to start at second fragment
                ((TabHostFragments) getActivity()).removeChild();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                TransactionActivity fragment = new TransactionActivity();
                Bundle bundle = new Bundle();
                bundle.putInt("viewPagerPosition", 1);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.realtabcontent, fragment);
                fragmentTransaction.commit();
                break;

            case R.id.btnForSaveTransactionEdit:
                ((TabHostFragments) getActivity()).hideKeyboard();
                try {
                    if (editTxtForDebt.getText().toString().length() > 0) {
                        if (!editTxtForDebt.getText().toString().trim().equals("0") && Double.parseDouble(editTxtForDebt.getText().toString().trim()) > 0 || Double.parseDouble(editTxtForDebt.getText().toString().trim()) < 0) {
                            if (editTxtForDebt.getText().toString().trim().length() <= 10) {
                                if (editTxtForDescription.getText().toString().trim().length() <= 100) {
                                    if (subCathegory.equals("IncomeProtection")) {
                                        //mixpanel tracking
                                        mixpanelAPI.track(getString(R.string.mixpanel_edit_transaction_screen_complete), Analitics.editTransaction("Manage transaction", txtForAddTransactionValue.getText().toString()));

                                        String tmpDescription;
                                        if (editTxtForDescription.getText().toString().trim().length() < 1) {
                                            tmpDescription = "-";
                                        } else {
                                            tmpDescription = editTxtForDescription.getText().toString().trim();
                                        }

                                        String amnt = "";
                                        if (tranAmount.startsWith("-")) {
                                            if (editTxtForDebt.getText().toString().startsWith("-")) {
                                                amnt = editTxtForDebt.getText().toString();
                                            } else {
                                                amnt = "-" + editTxtForDebt.getText().toString();
                                            }
                                        } else {
                                            amnt = editTxtForDebt.getText().toString();
                                        }

                                        //generate json object for transaction update
                                        Transaction transaction = new Transaction(getActivity());
                                        JSONObject jsonObject = transaction.transactionUpdateJson(iD, txtForDotSelected.getText().toString(), amnt,
                                                tmpDescription, subCathegory);

                                        Map<String, String> params = new HashMap<>();
                                        params.put("user_email", "" + SessionStores.getUserEmail(getActivity()));
                                        params.put("tipsgo_transaction_id", "" + iD);
                                        params.put("amount", "" + amnt);
                                        params.put("description", "" + tmpDescription);
                                        params.put("transaction_date", "" + Utils.dateConvert(txtForDotSelected.getText().toString()));

                                        //update transaction detail's
                                        new UpdateTransactionTask(getActivity(), jsonObject, fragmentManager, TAG, params);
                                    } else {
                                        Log.e("debt baleance??", "" + Double.parseDouble(editTxtForDebt.getText().toString()));
                                        Log.e("constants baleance??", "" + Constants.WALLET_BALANCE);
                                        if (Double.parseDouble(editTxtForDebt.getText().toString()) <= Constants.WALLET_BALANCE) {
                                            //mixpanel tracking
                                            mixpanelAPI.track(getString(R.string.mixpanel_edit_transaction_screen_complete), Analitics.editTransaction("Manage transaction", txtForAddTransactionValue.getText().toString()));

                                            String tmpDescription;
                                            if (editTxtForDescription.getText().toString().trim().length() < 1) {
                                                tmpDescription = "-";
                                            } else {
                                                tmpDescription = editTxtForDescription.getText().toString().trim();
                                            }

                                            String amnt = "";
                                            if (tranAmount.startsWith("-")) {
                                                if (editTxtForDebt.getText().toString().startsWith("-")) {
                                                    amnt = editTxtForDebt.getText().toString();
                                                } else {
                                                    amnt = "-" + editTxtForDebt.getText().toString();
                                                }
                                            } else {
                                                amnt = editTxtForDebt.getText().toString();
                                            }

                                            //generate json object for transaction update
                                            Transaction transaction = new Transaction(getActivity());
                                            JSONObject jsonObject = transaction.transactionUpdateJson(iD, txtForDotSelected.getText().toString(), amnt,
                                                    tmpDescription, subCathegory);

                                            Map<String, String> params = new HashMap<>();
                                            params.put("user_email", "" + SessionStores.getUserEmail(getActivity()));
                                            params.put("tipsgo_transaction_id", "" + iD);
                                            params.put("amount", "" + amnt);
                                            params.put("description", "" + tmpDescription);
                                            params.put("transaction_date", "" + Utils.dateConvert(txtForDotSelected.getText().toString()));

                                            //update transaction detail's
                                            new UpdateTransactionTask(getActivity(), jsonObject, fragmentManager, TAG, params);
                                        } else {
                                            Utils.ShowAlert(getActivity(), "Your entered amount must be less than your current balance");
                                        }
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
                        Toast.makeText(getActivity(), "Please enter debt amount", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Please enter a valid number", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.txtForDotSelected:
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
    }

    public static String ddMMMMYYYYFormat(String ipDate) {
        String s = "";
        try {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            ddObj = sdf.parse(ipDate);
            System.out.println(ddObj);
            s = new SimpleDateFormat("dd/MM/yyyy").format(ddObj);
            System.out.println(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return s;
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
