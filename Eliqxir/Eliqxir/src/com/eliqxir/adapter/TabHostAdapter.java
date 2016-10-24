package com.eliqxir.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.eliqxir.utils.Constant;

public class TabHostAdapter extends FragmentPagerAdapter{
	

	int totalCount;
	CharSequence headerChar;
	FragmentManager fm;
	ArrayList<HashMap<String, String>> arrayListHeader = new ArrayList<HashMap<String,String>>();
	ArrayList<HashMap<String, String>> arrayListPagerValues = new ArrayList<HashMap<String,String>>();
	TabHostFragmentAdapter myFragment;

	public TabHostAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }
	
	public TabHostAdapter(FragmentManager fm, ArrayList<HashMap<String, String>> subCategoriesHeader, ArrayList<HashMap<String, String>> subCategoriesArray) {
        super(fm);
        this.fm = fm;
        arrayListHeader = subCategoriesHeader;
        arrayListPagerValues = subCategoriesArray;
    }

	@SuppressWarnings("deprecation")
	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		
        if(Integer.decode(Build.VERSION.SDK) >17)
        {
        	myFragment = new TabHostFragmentAdapter(fm);
            Bundle data = new Bundle();  
        	data.putString("titleNames", Constant.subCategoriesHeader.get(arg0).get("header").toUpperCase());
        	myFragment.setArguments(data);
        }
        else{
        	myFragment = new TabHostFragmentAdapter(fm, arrayListHeader, arrayListPagerValues);  
            Bundle data = new Bundle();   
        	data.putString("titleNames", arrayListHeader.get(arg0).get("header").toUpperCase());
        	myFragment.setArguments(data);
        }
        return myFragment;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(Integer.decode(Build.VERSION.SDK) >17)
        {
			totalCount = Constant.subCategoriesHeader.size();
        }
		else{
			totalCount = arrayListHeader.size();
		}
		return totalCount;
	}
	
	@SuppressWarnings("deprecation")
	@Override
    public CharSequence getPageTitle(int position) {
		if(Integer.decode(Build.VERSION.SDK) >17)
        {
			headerChar = Constant.subCategoriesHeader.get(position).get("header").toUpperCase();
        }
		else{
			headerChar = arrayListHeader.get(position).get("header").toUpperCase();
		}
    	return headerChar;
    }
}