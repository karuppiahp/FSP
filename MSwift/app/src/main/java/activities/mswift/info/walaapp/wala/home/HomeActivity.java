package activities.mswift.info.walaapp.wala.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.adapter.TodoListAdapter;
import activities.mswift.info.walaapp.wala.model.RecentTransactionsModel;
import activities.mswift.info.walaapp.wala.model.TodoListModel;
import activities.mswift.info.walaapp.wala.prizes.DailyQuizActivity;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.transactions.TransactionActivity;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 11/23/2015.
 */
public class HomeActivity extends Fragment implements View.OnClickListener {

    private View v;
    private ListView todoListView;
    private List<TodoListModel> todoListArray = new ArrayList<>();
    private List<RecentTransactionsModel> transactionArray = new ArrayList<>();
    private LinearLayout layForTransactionTable;
    private boolean firstTime = true;
    private ImageView imgForPrevious, imgForNext;
    private TextView txtForTransactionDate;
    private Calendar c;
    private String formattedDate;
    private SimpleDateFormat df;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        ((TabHostFragments) getActivity()).tabColorWhite();

        v = inflater.inflate(R.layout.home, container, false);
        todoListView = (ListView) v.findViewById(R.id.listViewForTodoList);
        layForTransactionTable = (LinearLayout) v.findViewById(R.id.layForRecentTransactionTableItems);
        todoListArray = Utils.getTodoNameValues(getActivity());
        todoListView.setAdapter(new TodoListAdapter(getActivity(), todoListArray));
        imgForPrevious = (ImageView) v.findViewById(R.id.imgForPrevious);
        imgForNext = (ImageView) v.findViewById(R.id.imgForNext);
        txtForTransactionDate = (TextView) v.findViewById(R.id.txtForTransactionDate);

        Utils.setListViewHeightBasedOnChildren(todoListView);

        c = Calendar.getInstance();
        df = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = df.format(c.getTime());
        txtForTransactionDate.setText(formattedDate);

        imgForPrevious.setOnClickListener(this);
        imgForNext.setOnClickListener(this);

        todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (i == 0) {
                    ((TabHostFragments) getActivity()).setcurrent(1);
                    // Move to step 2 fragment
                    DailyQuizActivity fragment = new DailyQuizActivity();
                    fragmentTransaction.replace(R.id.realtabcontent, fragment);
                }

                if (i == 1) {
                    ((TabHostFragments) getActivity()).setcurrent(2);
                    // Move to step 2 fragment
                    TransactionActivity fragment = new TransactionActivity();
                    fragmentTransaction.replace(R.id.realtabcontent, fragment);
                }

                if (i == 2) {
                    ((TabHostFragments) getActivity()).setcurrent(2);
                    // Move to step 2 fragment
                    TransactionActivity fragment = new TransactionActivity();
                    fragmentTransaction.replace(R.id.realtabcontent, fragment);
                }

                fragmentTransaction.commit();
            }
        });

        return v;
    }

    public void updateDate(int i) {
        c.add(Calendar.DATE, i);
        formattedDate = df.format(c.getTime());
        txtForTransactionDate.setText(formattedDate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgForPrevious:
                layForTransactionTable.removeAllViews();
                updateDate(-1);
                break;
            case R.id.imgForNext:
                layForTransactionTable.removeAllViews();
                updateDate(1);
                break;
        }
    }
}
