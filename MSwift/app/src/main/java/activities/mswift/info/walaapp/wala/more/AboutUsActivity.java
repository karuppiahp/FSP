package activities.mswift.info.walaapp.wala.more;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.ArrayList;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.adapter.ViewPagerAdapter;
import activities.mswift.info.walaapp.wala.model.AboutUsModel;
import activities.mswift.info.walaapp.wala.support.SlidingTabLayout;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 12/18/2015.
 */
public class AboutUsActivity extends Fragment {

    private View v;
    private ViewPager pager;
    private PagerTabStrip pagerTabStrip;
    private ArrayList<AboutUsModel> headerArray = new ArrayList<>();
    SlidingTabLayout mSlidingTabLayout;
    private MixpanelAPI mixpanelAPI;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        ((TabHostFragments) getActivity()).tabColorWhite();
        ((TabHostFragments) getActivity()).removeChild(); // remove child view
        v = inflater.inflate(R.layout.about_us, container, false);

        // mixpanel screen tracking
        mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
        mixpanelAPI.track(getString(R.string.mixpanel_more_screen_abaut_wala_screen));

        pager = (ViewPager) v.findViewById(R.id.viewPager);

        headerArray = Utils.getAboutUsHeaders(getActivity());
        /** Getting fragment manager */
        FragmentManager fm = getChildFragmentManager();
        /** Instantiating FragmentPagerAdapter */
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(fm, headerArray);

        /** Setting the pagerAdapter to the pager object */
        pager.setAdapter(pagerAdapter);

        /** Sliding tab layout initialized */
        mSlidingTabLayout = (SlidingTabLayout) v.findViewById(R.id.tabs);
        mSlidingTabLayout.setDistributeEvenly(true); // for equal width
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabindicator);
            }
        });
        mSlidingTabLayout.setViewPager(pager);


        return v;
    }
}
