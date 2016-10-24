package com.eliqxir.slidermenu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import com.eliqxir.CartActivity;
import com.eliqxir.LocationWindow;
import com.eliqxir.MyAccountActivity;
import com.eliqxir.R;
import com.eliqxir.SearchActivity;
import com.eliqxir.driver.LoginDriverActivity;
import com.eliqxir.tabhostfragments.TabsFragmentActivity;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.Utils;
import com.eliqxir.vendor.LoginVendorActivity;

public class ListMenuFragment extends Fragment implements OnClickListener {

	View rootView;
	ImageButton img_btn_home, img_btn_settings;
	RelativeLayout layoutForBrowse, layoutForMyAccount, layoutForLocation, layoutForCart, layoutForVendorLogin,layoutForSearch,layDriverLogin;
	SharedPreferences sharedpreferences;
	Editor editor;
	boolean rememberUser;
	String image_name="",pwd="";
	LocationManager lm;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}

		return false;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Utils.trackError(getActivity());

		rootView = inflater.inflate(R.layout.menu_list, container, false);
		// btn_buss = (Button) rootView.findViewById(R.id.businesss_btn);
		layoutForSearch=(RelativeLayout)rootView.findViewById(R.id.laySearch);
		layoutForBrowse = (RelativeLayout)rootView.findViewById(R.id.layBrowse);
		layoutForMyAccount = (RelativeLayout)rootView.findViewById(R.id.layAccount);
		layoutForLocation = (RelativeLayout)rootView.findViewById(R.id.layLocation);
		layoutForCart = (RelativeLayout)rootView.findViewById(R.id.layCart);
		layoutForVendorLogin = (RelativeLayout)rootView.findViewById(R.id.layVendorLogin);
		layDriverLogin=(RelativeLayout)rootView.findViewById(R.id.layDriverLogin);
		layDriverLogin.setOnClickListener(this);
		layoutForSearch.setOnClickListener(this);
		layoutForBrowse.setOnClickListener(this);
		layoutForMyAccount.setOnClickListener(this);
		layoutForLocation.setOnClickListener(this);
		layoutForCart.setOnClickListener(this);
		layoutForVendorLogin.setOnClickListener(this);
		sharedpreferences = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();
		 rememberUser = sharedpreferences.getBoolean("remember-user", false);
		  image_name = sharedpreferences.getString("img_name",
					"");
			Log.e("image_name",image_name);
			if(!image_name.equals(""))
			{
				layoutForMyAccount.setVisibility(View.VISIBLE);
			}
			else
			{
				layoutForMyAccount.setVisibility(View.GONE);
			}
//			 pwd=sharedpreferences.getString("pwd",
//					"");
		return rootView;

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.laySearch)
		{
			lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
			if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
			      !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				alertdialog();
			}else{	
			Intent intentToSearch = new Intent(getActivity(), SearchActivity.class);
			startActivity(intentToSearch);
			getActivity().finish();
			}
		}
		if(v.getId()==R.id.layBrowse)
		{
			lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
			if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
			      !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				alertdialog();
			}else{			
			Intent intentToBrowse = new Intent(getActivity(), TabsFragmentActivity.class);
			startActivity(intentToBrowse);
			getActivity().finish();
			}
		}
		
		if(v.getId()==R.id.layAccount)
		{
//			 if(rememberUser)
//			 {
			lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
			if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
			      !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				alertdialog();
			}else{	
				 Intent intentToMyAcc = new Intent(getActivity(), MyAccountActivity.class);
					startActivity(intentToMyAcc);
					getActivity().finish();
			}
//			 }
//			 else
//			 {
//				 Utils.ShowAlert(getActivity(), "User not logged In");
//			 }
			
		}
		
		if(v.getId()==R.id.layLocation)
		{
			lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
			if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
			      !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				alertdialog();
			}else{	
			Intent intentToLocation = new Intent(getActivity(), LocationWindow.class);
			startActivity(intentToLocation);
			}
		}
		
		if(v.getId()==R.id.layCart)
		{
			lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
			if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
			      !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				alertdialog();
			}else{	
			if(Constant.cartArray.size()>0)
			{
				Intent intentToLocation = new Intent(getActivity(), CartActivity.class);
				startActivity(intentToLocation);
			}
			else
			{
				Utils.ShowAlert(getActivity(), "You have no items in your shopping cart.");
			}
			}
		}
		
		if(v.getId()==R.id.layVendorLogin)
		{
			Intent intentToLocation = new Intent(getActivity(), LoginVendorActivity.class);
			startActivity(intentToLocation);
		//	getActivity().finish();
		}
		if(v.getId()==R.id.layDriverLogin)
		{
			Intent intentToLocation = new Intent(getActivity(), LoginDriverActivity.class);
			startActivity(intentToLocation);
			//getActivity().finish();
		}
		
	}
	
	public void alertdialog(){
		 // Build the alert dialog
		  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		  builder.setTitle("Location Services Not Active");
		  builder.setMessage("Hey! Turn on your GPS and Location Services");
		  builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialogInterface, int i) {
		    // Show location settings when the user acknowledges the alert dialog
		    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		    startActivity(intent);
		    }
		  });
		  Dialog alertDialog = builder.create();
		  alertDialog.setCanceledOnTouchOutside(false);
		  alertDialog.show();
	}

}
