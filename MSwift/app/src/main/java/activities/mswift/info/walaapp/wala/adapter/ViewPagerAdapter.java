package activities.mswift.info.walaapp.wala.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import activities.mswift.info.walaapp.wala.model.AboutUsModel;

/**
 * Created by karuppiah on 12/18/2015.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager fm;
    private ArrayList<AboutUsModel> headerArray = new ArrayList<>();
    private ViewPagerFragmentAdapter fragmentAdapter;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<AboutUsModel> headerArray) {
        super(fm);
        this.fm = fm;
        this.headerArray = headerArray;
    }

    @Override
    public Fragment getItem(int position) {
        fragmentAdapter = new ViewPagerFragmentAdapter(fm, headerArray); // call fragment adapter to set the view
        Bundle data = new Bundle();
        data.putString("titleName", headerArray.get(position).getName()); //Using title name the layout has been set
        fragmentAdapter.setArguments(data);
        return fragmentAdapter;
    }

    @Override
    public int getCount() {
        return headerArray.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence headerChar = headerArray.get(position).getName(); // header of tab set
        return headerChar;
    }
}
