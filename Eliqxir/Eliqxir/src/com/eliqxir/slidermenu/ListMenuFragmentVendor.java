package com.eliqxir.slidermenu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.eliqxir.R;
import com.eliqxir.driver.LoginDriverActivity;
import com.eliqxir.tabhostfragments.TabsFragmentActivity;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.SessionStore;
import com.eliqxir.utils.Utils;
import com.eliqxir.vendor.DriversActivity;
import com.eliqxir.vendor.LoginVendorActivity;
import com.eliqxir.vendor.OrderActivity;
import com.eliqxir.vendor.OverviewActivity;
import com.eliqxir.vendor.StoreInfoActivity;

public class ListMenuFragmentVendor extends Fragment implements OnClickListener {

	View rootView;
	ImageButton img_btn_home, img_btn_settings;
	RelativeLayout layoutForOverView, layoutForOrders, layoutForMenu, layoutForStoreInfo,
							   layoutForDrivers, layoutForLogout,layoutForDriverLogin;
	SharedPreferences vendorSharedpreferences;
	Editor vendorPreferenceEditor;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}

		return false;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Utils.trackError(getActivity());

		rootView = inflater.inflate(R.layout.menu_list_vendor, container, false);
		vendorSharedpreferences = rootView.getContext().getSharedPreferences("vendorPrefs", Context.MODE_PRIVATE);
		// btn_buss = (Button) rootView.findViewById(R.id.businesss_btn);
		layoutForOverView = (RelativeLayout)rootView.findViewById(R.id.layOverview);
		layoutForOrders = (RelativeLayout)rootView.findViewById(R.id.layOrders);
		layoutForMenu = (RelativeLayout)rootView.findViewById(R.id.layMenu);
		layoutForStoreInfo = (RelativeLayout)rootView.findViewById(R.id.layStoreInfo);
		layoutForDrivers = (RelativeLayout)rootView.findViewById(R.id.layDrivers);
		layoutForLogout = (RelativeLayout)rootView.findViewById(R.id.layVendorLogout);
		layoutForDriverLogin= (RelativeLayout)rootView.findViewById(R.id.laydriverLogin);
		layoutForDriverLogin.setOnClickListener(this);
		layoutForOverView.setOnClickListener(this);
		layoutForOrders.setOnClickListener(this);
		layoutForMenu.setOnClickListener(this);
		layoutForStoreInfo.setOnClickListener(this);
		layoutForDrivers.setOnClickListener(this);
		layoutForLogout.setOnClickListener(this);
		return rootView;

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.layOverview)
		{
			Intent intentToBrowse = new Intent(getActivity(), OverviewActivity.class);
			startActivity(intentToBrowse);
			getActivity().finish();
		}
		
		if(v.getId()==R.id.layOrders)
		{
			Intent intentToMyAcc = new Intent(getActivity(), OrderActivity.class);
			startActivity(intentToMyAcc);
			getActivity().finish();
		}
		
		if(v.getId()==R.id.layMenu)
		{
			Intent intentToLocation = new Intent(getActivity(), TabsFragmentActivity.class);
			startActivity(intentToLocation);
		}
		
		if(v.getId()==R.id.layStoreInfo)
		{
			Intent intentToLocation = new Intent(getActivity(), StoreInfoActivity.class);
			startActivity(intentToLocation);
		}
		
		if(v.getId()==R.id.layDrivers)
		{
			Intent intentToLocation = new Intent(getActivity(), DriversActivity.class);
			startActivity(intentToLocation);
		}
		
		if(v.getId()==R.id.layVendorLogout)
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());			 
			alertDialogBuilder.setTitle("Message");
	 		alertDialogBuilder	.setMessage("Are you sure want to Logout?")
					.setCancelable(false)
					.setPositiveButton("OK",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {								
							dialog.cancel();
							Constant.isVendorAvailable="notAvailable";
							SessionStore.saveSlidinMenu(null, getActivity());
							vendorPreferenceEditor=vendorSharedpreferences.edit();
							vendorPreferenceEditor.clear();
							vendorPreferenceEditor.commit();
							Intent intentToLocation = new Intent(getActivity(), TabsFragmentActivity.class);
							startActivity(intentToLocation);
							getActivity().finish();
						}
					  })
					 .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {						
						dialog.cancel();
					}
				});
					AlertDialog alertDialog = alertDialogBuilder.create();
	 				alertDialog.show();		
		}
		if(v.getId()==R.id.laydriverLogin)
		{
			Intent intentToLocation = new Intent(getActivity(), LoginDriverActivity.class);
			startActivity(intentToLocation);
		}
		
	}

}