package activities.mswift.info.walaapp.wala.progress;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.adapter.ViewPagerAdapterProgress;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;

/**
 * Created by karuppiah on 11/23/2015.
 */
public class ProgressActivity extends Fragment {

    private View v;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        ((TabHostFragments) getActivity()).tabColorWhite();

        v = inflater.inflate(R.layout.progress_tabs, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapterProgress adapter = new ViewPagerAdapterProgress(getFragmentManager());
        adapter.addFragment(new activities.mswift.info.walaapp.wala.progress.BalanceFragment(), "BALANCES");
        adapter.addFragment(new activities.mswift.info.walaapp.wala.progress.HealthFragment(), "HEALTH");
        adapter.addFragment(new activities.mswift.info.walaapp.wala.progress.GoalsFragment(), "GOALS");
        viewPager.setAdapter(adapter);
    }
}
