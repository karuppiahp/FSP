package activities.mswift.info.walaapp.wala.transactions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.adapter.ViewPagerAdapterTransactions;
import activities.mswift.info.walaapp.wala.support.SlidingTabLayoutTransactions;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 12/30/2015.
 */
public class TransactionActivity extends Fragment {

    private View v;
    private String savingsWalletNumber;
    private String debtWalletNumber;
    private String TAG = "screen size ";
    TabHost mTabHost;
    ViewPager pager;
    ViewPagerAdapterTransactions adapter;
    SlidingTabLayoutTransactions tabs;
    CharSequence Titles[] = {"Add", "Manage"};
    int Numboftabs = 2, viewPagerPosition;
    private Button btnForGotIt;
    private RelativeLayout layForOverlay;
    private ImageView imgForInfoIcon;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        mTabHost = new TabHost(getActivity());
        ((TabHostFragments) getActivity()).tabColorWhite();

        v = inflater.inflate(R.layout.transactions, container, false);

        btnForGotIt = (Button) v.findViewById(R.id.btnForGotIt);
        layForOverlay = (RelativeLayout) v.findViewById(R.id.layForTransOverlay);
        imgForInfoIcon = (ImageView) v.findViewById(R.id.imgForInfoIcon);

        if (SessionStores.getTransOverlay(getActivity()) == null || !(SessionStores.getTransOverlay(getActivity()).equals("entered"))) {
            SessionStores.saveTransOverlay("entered", getActivity());
            layForOverlay.setVisibility(View.VISIBLE);
        } else {
            layForOverlay.setVisibility(View.GONE);
        }

        btnForGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layForOverlay.setVisibility(View.GONE);
            }
        });

        imgForInfoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layForOverlay.setVisibility(View.VISIBLE);
            }
        });

        //check if wallet number is stored
        savingsWalletNumber = SessionStores.getSavingsWalletNumber(getActivity());
        debtWalletNumber = SessionStores.getDebtWalletNumber(getActivity());

        //get wallet code
        if (savingsWalletNumber != null && savingsWalletNumber.length() > 0 && debtWalletNumber != null && debtWalletNumber.length() > 0) {
            // Toast.makeText(getActivity(), "wallet number's stored", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Please finish Financial profile", Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapterTransactions(getActivity().getSupportFragmentManager(), Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) getActivity().findViewById(R.id.pager);
        pager.setAdapter(adapter);

        //get bundle
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            viewPagerPosition = bundle.getInt("viewPagerPosition");
            pager.setCurrentItem(viewPagerPosition);
        }

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayoutTransactions) getActivity().findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayoutTransactions.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabindicator);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
    }
}