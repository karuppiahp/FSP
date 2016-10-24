package activities.mswift.info.walaapp.wala.progress;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.Calendar;
import java.util.TimeZone;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.asyntasks.GetWalletTransactionListTask;
import activities.mswift.info.walaapp.wala.transactions.Transaction;
import activities.mswift.info.walaapp.wala.utils.Analitics;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by fssd23 on 12/30/2015.
 */
public class BalanceSavingsFragment extends Fragment implements View.OnClickListener {
    private View balsavings;
    private TextView txtForDotStartSelected;
    private TextView txtForDotEndSelected;
    private ProgressActivity progessActiv;
    private SwipeMenuListView swipeMenuListView;
    private Calendar cal;
    private int day, month, year, transactionId;
    private String walletCode;
    private String TAG = "savings";
    private static TextView accSavingsBal;
    private MixpanelAPI mixpanelAPI;

    public static BalanceSavingsFragment newInstance() {
        BalanceSavingsFragment fragment = new BalanceSavingsFragment();

        return fragment;
    }

    public BalanceSavingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        balsavings = inflater.inflate(R.layout.bal_savings_fragment, container, false);

        //checking walletCode for savings
        if (SessionStores.getSavingsWalletNumber(getActivity()) != null && SessionStores.getSavingsWalletNumber(getActivity()).length() > 0) {
            walletCode = SessionStores.getSavingsWalletNumber(getActivity());
        } else {
            Toast.makeText(getActivity(), "Please finish Financial profile", Toast.LENGTH_SHORT).show();
        }

        //binding view's
        swipeMenuListView = (SwipeMenuListView) balsavings.findViewById(R.id.transactionSavingsListView);
        txtForDotStartSelected = (TextView) balsavings.findViewById(R.id.txtForDotStartSelected);
        txtForDotEndSelected = (TextView) balsavings.findViewById(R.id.txtForDotEndSelected);
        accSavingsBal = (TextView) balsavings.findViewById(R.id.accBal);

        // mixpanel screen tracking
        mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
        mixpanelAPI.track(getString(R.string.mixpanel_balance_savings_screen));

        //getting calendar instance
        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        //setting initial values for date textview's
        txtForDotStartSelected.setText(Utils.calculateDate(day) + "/" + Utils.calculateDate(month + 1) + "/" + Utils.calculateDate(year));
        txtForDotEndSelected.setText(Utils.calculateDate(day) + "/" + Utils.calculateDate(month + 1) + "/" + Utils.calculateDate(year));

        //setting Onclicklisteners for textview's
        txtForDotStartSelected.setOnClickListener(this);
        txtForDotEndSelected.setOnClickListener(this);

        swipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView transactionDescription = (TextView) view.findViewById(R.id.transaction_manage_item_description);
                TextView transactionAmount = (TextView) view.findViewById(R.id.transaction_manage_item_amount);
                TextView transactionDate = (TextView) view.findViewById(R.id.transaction_manage_item_datePosted);
                TextView transactionId = (TextView) view.findViewById(R.id.transaction_manage_item_id);
                TextView transactionSubCathegory = (TextView) view.findViewById(R.id.transaction_manage_item_subCathegory);

                //mixpanel edit transaction tracking
                mixpanelAPI.track(getString(R.string.mixpanel_balance_savings_screen_edit_transaction));

                //convert cathegory to array index
                Transaction transaction = new Transaction(getActivity(), transactionSubCathegory.getText().toString());
                int index = transaction.getArrayNumber();
                String subCathegoryMapping = transaction.getTransactionMapping();

                Constants.WALLET_BALANCE = Constants.WALLET_BALANCE_SAVINGS;

                //create bundle for transaction edit
                Bundle bundle = transaction.getTransactionBundle(subCathegoryMapping, transactionId.getText().toString()
                        , transactionDescription.getText().toString(), transactionAmount.getText().toString(),
                        transactionDate.getText().toString(), transactionSubCathegory.getText().toString(), index);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                BalanceTransactionEdit fragment = new BalanceTransactionEdit();
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.realtabcontent, fragment);
                fragmentTransaction.commit();
            }
        });

        getWalletTransactionListtask();

        return balsavings;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txtForDotStartSelected) {
            DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
                    0, datePickerListenerForStartDate,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH));
            datePicker.setCancelable(false);
            datePicker.setTitle("Select the date");
            datePicker.show();
        }

        if (v.getId() == R.id.txtForDotEndSelected) {
            DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
                    0, datePickerListenerForEndDate,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH));
            datePicker.setCancelable(false);
            datePicker.setTitle("Select the date");
            datePicker.show();
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListenerForStartDate = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            int year1 = selectedYear;
            int month1 = selectedMonth + 1;
            int day1 = selectedDay;
            txtForDotStartSelected.setText(Utils.calculateDate(day1) + "/" + Utils.calculateDate(month1) + "/" + Utils.calculateDate(year1));
            getWalletTransactionListtask();
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListenerForEndDate = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            int year1 = selectedYear;
            int month1 = selectedMonth + 1;
            int day1 = selectedDay;
            txtForDotEndSelected.setText(Utils.calculateDate(day1) + "/" + Utils.calculateDate(month1) + "/" + Utils.calculateDate(year1));
            getWalletTransactionListtask();
        }
    };

    public void getWalletTransactionListtask() {

        //mixpanel time span tracking
        mixpanelAPI.track(getString(R.string.mixpanel_balance_savings_screen_time_span), Analitics.balanceTimeSpan(txtForDotStartSelected.getText().toString(), txtForDotEndSelected.getText().toString()));
        new GetWalletTransactionListTask(getActivity(), txtForDotStartSelected.getText().toString(), txtForDotEndSelected.getText().toString(), walletCode, swipeMenuListView, TAG);
    }

    public static void updateBalanceTextview(String s) {
        accSavingsBal.setText("Account Balance: " + s);
    }
}