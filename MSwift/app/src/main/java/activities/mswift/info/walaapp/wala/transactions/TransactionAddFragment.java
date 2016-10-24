package activities.mswift.info.walaapp.wala.transactions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.Constants;

/**
 * Created by kruno on 11.02.16..
 */
public class TransactionAddFragment extends Fragment implements View.OnClickListener {

    private final int HOME = 0;
    private final int GROCERIES = 1;
    private final int TRANSPORTATION = 2;
    private final int HEALTH = 3;
    private final int EDUCATION = 4;
    private final int UTILITIES = 5;
    private final int SHOPPING = 6;
    private final int DININGOUT = 7;
    private final int ENTERTAINMENT = 8;
    private final int TRAVEL = 9;
    private final int OTHER = 10;
    //values <11 are for expences
    private final int INCOME = 11;

    //values >12 use debt wallet
    private final int NEW_LOAN = 12;
    private final int PAY_OFF_LOAN = 13;

    private MixpanelAPI mixpanelAPI;
    private View v;

    ImageView trans_im_home, trans_im_groceries, trans_im_transportation, trans_im_health, trans_im_education, trans_im_utilities,
            trans_im_shopping, trans_im_dining_out, trans_im_entertainment, trans_im_travel, trans_im_other, trans_im_income, trans_im_new_loan, trans_im_pay_off;

    public TransactionAddFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }

        v = inflater.inflate(R.layout.fragment_transaction_add, container, false);

        trans_im_home = (ImageView) v.findViewById(R.id.trans_im_home);
        trans_im_groceries = (ImageView) v.findViewById(R.id.trans_im_groceries);
        trans_im_transportation = (ImageView) v.findViewById(R.id.trans_im_transportation);
        trans_im_health = (ImageView) v.findViewById(R.id.trans_im_health);
        trans_im_education = (ImageView) v.findViewById(R.id.trans_im_education);
        trans_im_utilities = (ImageView) v.findViewById(R.id.trans_im_utilities);
        trans_im_shopping = (ImageView) v.findViewById(R.id.trans_im_shopping);
        trans_im_dining_out = (ImageView) v.findViewById(R.id.trans_im_dining_out);
        trans_im_entertainment = (ImageView) v.findViewById(R.id.trans_im_enterteinment);
        trans_im_travel = (ImageView) v.findViewById(R.id.trans_im_travel);
        trans_im_other = (ImageView) v.findViewById(R.id.trans_im_other);
        trans_im_income = (ImageView) v.findViewById(R.id.trans_im_income);
        trans_im_new_loan = (ImageView) v.findViewById(R.id.trans_im_new_loan);
        trans_im_pay_off = (ImageView) v.findViewById(R.id.trans_im_pay_off_loan);

        trans_im_home.setOnClickListener(this);
        trans_im_groceries.setOnClickListener(this);
        trans_im_transportation.setOnClickListener(this);
        trans_im_health.setOnClickListener(this);
        trans_im_education.setOnClickListener(this);
        trans_im_utilities.setOnClickListener(this);
        trans_im_shopping.setOnClickListener(this);
        trans_im_dining_out.setOnClickListener(this);
        trans_im_entertainment.setOnClickListener(this);
        trans_im_travel.setOnClickListener(this);
        trans_im_other.setOnClickListener(this);
        trans_im_income.setOnClickListener(this);
        trans_im_new_loan.setOnClickListener(this);
        trans_im_pay_off.setOnClickListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // mixpanel screen tracking
            mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
            mixpanelAPI.track(getString(R.string.mixpanel_add_transaction_screen));
        }
    }

    @Override
    public void onClick(View v) {
        ((TabHostFragments) getActivity()).removeChild();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TransactionsAdd fragment = new TransactionsAdd();
        Bundle bundle = new Bundle();

        switch (v.getId()) {
            case R.id.trans_im_home:
                bundle.putInt("transaction", HOME);
                break;
            case R.id.trans_im_groceries:
                bundle.putInt("transaction", GROCERIES);
                break;
            case R.id.trans_im_transportation:
                bundle.putInt("transaction", TRANSPORTATION);
                break;
            case R.id.trans_im_health:
                bundle.putInt("transaction", HEALTH);
                break;
            case R.id.trans_im_education:
                bundle.putInt("transaction", EDUCATION);
                break;
            case R.id.trans_im_utilities:
                bundle.putInt("transaction", UTILITIES);
                break;
            case R.id.trans_im_shopping:
                bundle.putInt("transaction", SHOPPING);
                break;
            case R.id.trans_im_dining_out:
                bundle.putInt("transaction", DININGOUT);
                break;
            case R.id.trans_im_enterteinment:
                bundle.putInt("transaction", ENTERTAINMENT);
                break;
            case R.id.trans_im_travel:
                bundle.putInt("transaction", TRAVEL);
                break;
            case R.id.trans_im_other:
                bundle.putInt("transaction", OTHER);
                break;
            case R.id.trans_im_income:
                bundle.putInt("transaction", INCOME);
                break;
            case R.id.trans_im_new_loan:
                bundle.putInt("transaction", NEW_LOAN);
                break;
            case R.id.trans_im_pay_off_loan:
                bundle.putInt("transaction", PAY_OFF_LOAN);
            default:
                break;
        }
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.realtabcontent, fragment);
        fragmentTransaction.commit();
    }
}
