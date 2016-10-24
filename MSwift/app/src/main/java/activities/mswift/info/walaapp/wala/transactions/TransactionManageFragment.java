package activities.mswift.info.walaapp.wala.transactions;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.TimeZone;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.asyntasks.GetTransactionList;
import activities.mswift.info.walaapp.wala.asyntasks.TransactionDeleteTask;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by kruno on 11.02.16..
 */
public class TransactionManageFragment extends Fragment {

    private View v;
    SwipeMenuListView mListView;
    public static JSONArray jsonArray;
    private Calendar cal;
    private int day, month, year;
    private String transactionDateStart, transactionDateEnd;
    boolean isOpen = false;

    private static final int HORIZONTAL_MIN_DISTANCE = 20;
    private static final int VERTICAL_MIN_DISTANCE = 80;
    private float downX, downY, upX, upY;
    private MixpanelAPI mixpanelAPI;

    public TransactionManageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_transaction_manage, container, false);

        mListView = (SwipeMenuListView) v.findViewById(R.id.transactionManageListView);

        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        transactionDateEnd = (Utils.calculateDate(day) + "/" + Utils.calculateDate(month + 1) + "/" + Utils.calculateDate(year));

        //set range for transaction -3 day's from today
        transactionDateStart = calculateDate(-3);

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xba, 0xd7, 0xe2)));
                // set item width
                deleteItem.setWidth(dp2px(70));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        mListView.setMenuCreator(creator);

        // handle click on menu item
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // delete
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(position);
                            String id = jsonObject.getString("ID");
                            int itemPosition = position;

                            Transaction transaction = new Transaction(getActivity());
                            new TransactionDeleteTask(getActivity(), transaction.transactionDeleteJson(id), itemPosition, jsonArray, mListView, id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //mixpanel track delete transaction
                        mixpanelAPI.track(getString(R.string.mixpanel_manage_transaction_screen_delete_button));
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
                if (!isOpen) {
                    mListView.requestDisallowInterceptTouchEvent(true);
                }
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        mListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
                isOpen = true;
            }

            @Override
            public void onMenuClose(int position) {

                isOpen = false;
                mListView.setOnTouchListener(mSuppressInterceptListener);
            }
        });

        // open edit transaction fragment
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                String subCathegory = null;
                String iD = null;
                String description = null;
                String tranAmount = null;
                String dateposted = null;
                String subCathegoryMapping = null;
                int index;

                try {
                    JSONObject json = jsonArray.getJSONObject(position);
                    subCathegory = json.getString("SubCategory");
                    iD = json.getString("ID");
                    description = json.getString("Description");
                    tranAmount = json.getString("TranAmount");
                    dateposted = json.getString("DatePosted");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (subCathegory.equalsIgnoreCase("IncomeProtection")) {
                    Constants.WALLET_BALANCE = Constants.WALLET_BALANCE_DEBT;
                } else {
                    Constants.WALLET_BALANCE = Constants.WALLET_BALANCE_SAVINGS;
                }

                //convert cathegory to array index
                Transaction transaction = new Transaction(getActivity(), subCathegory);
                index = transaction.getArrayNumber();
                subCathegoryMapping = transaction.getTransactionMapping();

                //create bundle for transaction edit
                Bundle bundle = transaction.getTransactionBundle(subCathegoryMapping, iD, description, tranAmount, dateposted, subCathegory, index);

                //switch to editTransaction fragment
                ((TabHostFragments) getActivity()).removeChild();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                TransactionsEdit fragment = new TransactionsEdit();
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.realtabcontent, fragment);
                fragmentTransaction.commit();
                return false;
            }
        });

        mListView.setOnTouchListener(mSuppressInterceptListener);

        new GetTransactionList(getActivity(), transactionDateStart, transactionDateEnd, mListView);

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // mixpanel screen tracking
            mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
            mixpanelAPI.track(getString(R.string.mixpanel_manage_transaction_screen));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    //detect movement left/right
    private View.OnTouchListener mSuppressInterceptListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = event.getX();
                    downY = event.getY();
                    return false; // allow other events like Click to be processed
                }
                case MotionEvent.ACTION_MOVE: {
                    upX = event.getX();
                    upY = event.getY();

                    float deltaX = downX - upX;
                    float deltaY = downY - upY;

                    // horizontal swipe detection
                    if (Math.abs(deltaX) > HORIZONTAL_MIN_DISTANCE) {
                        // left or right
                        if (deltaX < 0) {
                            return true;
                        }
                        if (deltaX > 0) {
                            mListView.setOnTouchListener(mSuppressInterceptListener1);
                            return true;
                        }
                    } else
                        // vertical swipe detection
                        if (Math.abs(deltaY) > VERTICAL_MIN_DISTANCE) {
                            // top or down
                            if (deltaY < 0) {
                                return false;
                            }
                            if (deltaY > 0) {
                                return false;
                            }
                        }
                    return true;
                }
            }
            return false;
        }
    };

    private String calculateDate(int i) {

        int tmpDay, tmpMonth, tmpYear;
        String tmpDate;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, i);
        tmpDay = calendar.get(Calendar.DAY_OF_MONTH);
        tmpMonth = calendar.get(Calendar.MONTH);
        tmpYear = calendar.get(Calendar.YEAR);
        tmpDate = (Utils.calculateDate(tmpDay) + "/" + Utils.calculateDate(tmpMonth + 1) + "/" + Utils.calculateDate(tmpYear));

        return tmpDate;
    }

    //disable view pager scrolling
    private View.OnTouchListener mSuppressInterceptListener1 = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN &&
                    v instanceof ViewGroup) {
                ((ViewGroup) v).requestDisallowInterceptTouchEvent(true);
            }
            return false;
        }
    };
}
