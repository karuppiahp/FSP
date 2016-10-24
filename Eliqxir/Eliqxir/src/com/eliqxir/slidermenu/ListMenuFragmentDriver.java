package com.eliqxir.slidermenu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.eliqxir.driver.OrdersActivity;
import com.eliqxir.tabhostfragments.TabsFragmentActivity;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.SessionStore;
import com.eliqxir.utils.Utils;
import com.eliqxir.vendor.LoginVendorActivity;

public class ListMenuFragmentDriver extends Fragment implements OnClickListener {

	View rootView;
	ImageButton img_btn_home, img_btn_settings;
	RelativeLayout layoutForOrders, layoutForLogout,layoutForVendorLogin;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}

		return false;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Utils.trackError(getActivity());


		rootView = inflater.inflate(R.layout.menu_list_driver, container, false);
		layoutForOrders = (RelativeLayout)rootView.findViewById(R.id.layOrders);
		layoutForLogout = (RelativeLayout)rootView.findViewById(R.id.layDriverLogout);
		layoutForVendorLogin = (RelativeLayout)rootView.findViewById(R.id.layVendorLogin);
		layoutForOrders.setOnClickListener(this);
		layoutForLogout.setOnClickListener(this);
		layoutForVendorLogin.setOnClickListener(this);
		return rootView;

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.layOrders)
		{
			Intent intentToMyAcc = new Intent(getActivity(), OrdersActivity.class);
			startActivity(intentToMyAcc);                                            
			getActivity().finish();
		}
		
		if(v.getId()==R.id.layDriverLogout)
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());			 
			alertDialogBuilder.setTitle("Message");
	 		alertDialogBuilder	.setMessage("Are you sure want to Logout?")
					.setCancelable(false)
					.setPositiveButton("OK",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {								
							dialog.cancel();
							Constant.isDriverAvailable="notAvailable";
							SessionStore.saveSlidinMenu(null, getActivity());
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
		
		if(v.getId()==R.id.layVendorLogin){
			Intent intentToLocation = new Intent(getActivity(), LoginVendorActivity.class);
			startActivity(intentToLocation);
		}
	}

}
