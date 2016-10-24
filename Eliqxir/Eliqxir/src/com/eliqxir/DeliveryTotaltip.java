package com.eliqxir;

import java.text.NumberFormat;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.eliqxir.adapter.SpinnerAdapter;
import com.eliqxir.slidermenu.SlidingMenuActivity;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.DecimalDigitsInputFilter;
import com.eliqxir.utils.Utils;
import com.eliqxir.vendor.AddDriver;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class DeliveryTotaltip extends SlidingMenuActivity implements
		OnClickListener {

	TextView label_percentage, label_enterManually, label_tipTotal,
			value_tipTotal, textForHeader;
	Button btn_percentage, btn_enterManually, btnDone;
	Spinner value_percentage;
	EditText value_enterManually;
	ImageButton backImg, btnSlideMenu, cartBtn;
	ArrayList<String> tipPercentList = new ArrayList<String>();
	int selectedPercent;
	CheckBox checkBox1;
	Double tot;
	private String current = "", selectedIteration,selected_Value,
			tipsAmount1 = "notAvailable";
	int tipPosition;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());
		setContentView(R.layout.delivery_tip1);
		tot = Double.parseDouble(getIntent().getExtras().getString("total"));
		tipPosition = (getIntent().getExtras().getInt("tip_postion"));
		Log.e("tipPosition", tipPosition + "");
		Log.e("Subtotal value", tot + "");
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
		btnDone = (Button) findViewById(R.id.btnDone);
		btnDone.setOnClickListener(this);
		label_percentage = (TextView) findViewById(R.id.percentage_label);
		label_enterManually = (TextView) findViewById(R.id.enter_manual_label);
		value_percentage = (Spinner) findViewById(R.id.percentage_value);
//		value_percentage = (TextView) findViewById(R.id.percentage_value);
		value_enterManually = (EditText) findViewById(R.id.enter_manual_value);

		label_tipTotal = (TextView) findViewById(R.id.total_label);
		value_tipTotal = (TextView) findViewById(R.id.total_value);
//		value_manualTotal= (TextView) findViewById(R.id.total_manual_value);

		btn_percentage = (Button) findViewById(R.id.btn_percentage);
		btn_enterManually = (Button) findViewById(R.id.btn_enter_manually);

		btnSlideMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
		backImg = (ImageButton) findViewById(R.id.backBtn);
		cartBtn = (ImageButton) findViewById(R.id.cartBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		backImg.setOnClickListener(this);
		btnSlideMenu.setVisibility(View.GONE);
		cartBtn.setVisibility(View.GONE);
		backImg.setVisibility(View.VISIBLE);
		backImg.setBackgroundResource(R.drawable.close_btn);
		textForHeader.setText("TIP");

		btn_percentage.setOnClickListener(this);
		btn_enterManually.setOnClickListener(this);
		
		/*Log.e("Inside Percent Value",Constant.percentValue);
		if(Constant.percentValue.equals("null")){
			value_percentage.setText("0%");			
		}else{
		value_percentage.setText(Constant.percentValue);
		}*/
		
		tipPercentList.clear();
		tipPercentList.add("0 %");
		tipPercentList.add("1 %");
		tipPercentList.add("2 %");
		tipPercentList.add("3 %");
		tipPercentList.add("4 %");
		tipPercentList.add("5 %");
		tipPercentList.add("6 %");
		tipPercentList.add("7 %");
		tipPercentList.add("8 %");
		tipPercentList.add("9 %");
		tipPercentList.add("10 %");
		tipPercentList.add("11 %");
		tipPercentList.add("12 %");
	
		value_enterManually.addTextChangedListener(watch);
		
//		value_enterManually.setFilters(new InputFilter[] { new DecimalDigitsInputFilter(15, 2) });
		
		/*value_enterManually.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {			
				value_tipTotal.setText("$"+value_enterManually.getText().toString());
				if (!s.toString().equals(current)) {
					try {
						value_enterManually.removeTextChangedListener(this);
						String cleanString = s.toString().replaceAll("[$,.]", "");
						double parsed = Double.parseDouble(cleanString);
						String formatted = NumberFormat.getCurrencyInstance().format((parsed / 100));
						current = formatted;
						value_enterManually.setText(formatted);
						value_tipTotal.setText(formatted);
						String formattedDouble = formatted.replace("$", "").replace(",", "");
						formattedDouble = String.format("%.2f",Double.parseDouble(formattedDouble));
						tipsAmount1 = formattedDouble;						
						double tmp = (Double.parseDouble(formattedDouble) / tot) * 100;
						Constant.tipsPercent = Math.round(tmp);
						value_enterManually.setSelection(formatted.length());
						value_enterManually.addTextChangedListener(this);
					} catch (NumberFormatException e) {						
						Log.e("Exception  3333",""+e);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				Log.e("Inside before","Text changed");
				value_tipTotal.setText("$0.00");				
			}

			@Override
			public void afterTextChanged(Editable s) {
			}

		});*/

		/*value_percentage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openSpinnerYearView(tipPercentList);							
			}
		});*/		
		
		SpinnerAdapter spinAdapter = new SpinnerAdapter(this,
				R.layout.tips_percentage, tipPercentList, "fromTips");
		value_percentage.setAdapter(spinAdapter);
		Log.e("Constant.tipPosition", Constant.tipPosition + "");
		value_percentage.setSelection(Constant.tipPosition);
		
		value_percentage
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						
						selectedIteration = tipPercentList.get(position);
						tipPosition = position;
						Log.e("Selected Iteration", selectedIteration);
						Log.e("tipPosition value",""+tipPosition);
						Log.e("position value",""+position);
						
						if (position != 0) {
							// tipPosition = position;
							Constant.manualTipAmount = "$0.00";
							String sp[] = selectedIteration.split(" ");
							Log.e("sp[0]", sp[0]);
							selectedPercent = Integer.parseInt(sp[0]);
							Log.e("Percentage selectedPercent",""+selectedPercent);
							// Constant.tipsPercent = selectedPercent;
							// double t = tot - ((tot * selectedPercent)) / 100;
							double t = (tot * selectedPercent) / 100;
                            Log.e("Percentage total amount",""+t);
							tipsAmount1 = String.format("%.2f", t);
							value_tipTotal.setText("$" + tipsAmount1);
							// Constant.tipsAmount = Double
							// .parseDouble(tipsAmount1);

							value_enterManually.setFocusable(false);
							value_enterManually.setEnabled(false);
			//				value_enterManually.setText("$0.00");
							// checkBox1.setEnabled(false);
						} else {
							if (checkBox1.isChecked()) {
								Log.e("!!!!!!!!!!!!!!1","@@@@@@@@@@2");
								value_enterManually.setFocusable(true);
								value_enterManually	.setFocusableInTouchMode(true);
								value_enterManually.setEnabled(true);
							} else {
								Log.e("**************","$$$$$$$$");
								value_enterManually.setFocusable(false);
								value_enterManually.setEnabled(false);
							}
							
							if (Constant.manualTipAmount.equals("null")) {
				//				value_enterManually.setText("$0.00");
								Log.e("**************","11111111111");
								value_tipTotal.setText("$0.00");
								tipsAmount1 = "0";
							} else {								
								Log.e("**************","22222222222"+Constant.manualTipAmount);			
								if(Constant.manualTipAmount.equals("$0.00")){
									value_enterManually.setHint("0.0");
								}else{
				//				value_enterManually.setText("$"	+ String.format("%.2f",	Double.parseDouble(Constant.manualTipAmount.replace("$", ""))));
								value_enterManually.setText(String.format("%.2f",	Double.parseDouble(Constant.manualTipAmount.replace("$", ""))));
								}
								value_tipTotal.setText("$"+ String.format("%.2f",Double.parseDouble(Constant.manualTipAmount.replace("$", ""))));
								
							}
							// checkBox1.setEnabled(true);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}

				});
		
		if (Constant.manualTipAmount.equals("null") ) {
			try {
				Log.e("**************","33333333333333");
	//			value_enterManually.setText("$0.00");
				value_tipTotal.setText("$0.00");
				
			/*	Log.e("Inside Percent Value",Constant.TotalTipAmount);
				if(Constant.TotalTipAmount.equals("null")){
					value_tipTotal.setText("$0.00");
				}else{
			    	value_tipTotal.setText("$"+Constant.TotalTipAmount);
				}*/
				
			} catch (Exception e) {
				Log.e("Exception  22",""+e);
			}
		} else {
			Log.e("**************","44444444");
	//		value_enterManually.setText("$"	+ String.format("%.2f", Double.parseDouble(Constant.manualTipAmount.replace("$", ""))));
			value_enterManually.setText(String.format("%.2f", Double.parseDouble(Constant.manualTipAmount.replace("$", ""))));
			value_tipTotal.setText("$"	+ String.format("%.2f", Double.parseDouble(Constant.manualTipAmount.replace("$", ""))));
			checkBox1.setChecked(true);
		}
		
		if (checkBox1.isChecked()) {
			Log.e("333332 oncreate method", "4444442");
			value_percentage.setSelection(0);
			
//			value_percentage.setText("0%");
		
			value_percentage.setEnabled(false);
			value_enterManually.setFocusable(true);
			value_enterManually.setFocusableInTouchMode(true);
			value_enterManually.setEnabled(true);
			checkBox1.setEnabled(true);
		} else {
			Log.e("355555", "36666666");
			// value_percentage.setSelection(0);
			value_percentage.setEnabled(true);
			value_enterManually.setFocusable(false);
			value_enterManually.setEnabled(false);
		}

		checkBox1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					Log.e("1", "1");
					if (checkBox1.isChecked()) {
						value_tipTotal.setText("$0.00");
						Log.e("2", "2");
						value_percentage.setSelection(0);
						
		//				value_percentage.setText("0%");
						
						value_percentage.setEnabled(false);
						value_enterManually.setFocusable(true);
						value_enterManually.setFocusableInTouchMode(true);
						value_enterManually.setEnabled(true);
						checkBox1.setEnabled(true);
						
			//			value_tipTotal.setVisibility(View.GONE);
			//			value_manualTotal.setVisibility(View.VISIBLE);
			//			value_manualTotal.setText("$0.00");
					} else {
						Log.e("3", "3");
						// value_percentage.setSelection(0);
						value_percentage.setEnabled(true);
						value_enterManually.setFocusable(false);
						value_enterManually.setEnabled(false);
					}

				} else {
					Log.e("4", "4");
		//			value_enterManually.setText("$0.00");
					// isTermsChecked = false;
					value_percentage.setEnabled(true);
					value_enterManually.setFocusable(false);
					value_enterManually.setEnabled(false);
					
					value_enterManually.setText("");
					value_tipTotal.setText("$0.00");
//					value_tipTotal.setVisibility(View.VISIBLE);
//					value_manualTotal.setVisibility(View.GONE);
				}
			}
		});
	}
	
	// Method to display Year  list
		/*	private void openSpinnerYearView(final ArrayList<String> list) {
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(DeliveryTotaltip.this,
						android.R.layout.simple_list_item_1, list);
				new AlertDialog.Builder(DeliveryTotaltip.this)
						.setAdapter(adapter, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int position) {
								selected_Value= list.get(position).toString();
								value_percentage.setText(selected_Value);
								Log.e("Selected Year Item", selected_Value);							
								Constant.percentValue=selected_Value;								
						//		selectedIteration = tipPercentList.get(position);
				//				tipPosition = position;
								Log.e("Selected Iteration", selected_Value);
						//		Log.e("tipPosition value",""+tipPosition);
					//			Log.e("position value",""+position);
								String sp[] = selected_Value.split(" ");
								Log.e("sp[0]", sp[0]);
								if (Integer.parseInt(sp[0]) != 0) {
									// tipPosition = position;
									Constant.manualTipAmount = "$0.00";								
									selectedPercent = Integer.parseInt(sp[0]);
									Log.e("Percentage selectedPercent",""+selectedPercent);
									// Constant.tipsPercent = selectedPercent;
									// double t = tot - ((tot * selectedPercent)) / 100;
									double t = (tot * selectedPercent) / 100;
		                            Log.e("Percentage total amount",""+t);
									tipsAmount1 = String.format("%.2f", t);
									value_tipTotal.setText("$" + tipsAmount1);
									Constant.TotalTipAmount=tipsAmount1;
									// Constant.tipsAmount = Double
									// .parseDouble(tipsAmount1);

									value_enterManually.setFocusable(false);
									value_enterManually.setEnabled(false);
					//				value_enterManually.setText("$0.00");
									// checkBox1.setEnabled(false);
								} else {
									if (checkBox1.isChecked()) {
										Log.e("!!!!!!!!!!!!!!1","@@@@@@@@@@2");
										value_enterManually.setFocusable(true);
										value_enterManually	.setFocusableInTouchMode(true);
										value_enterManually.setEnabled(true);
									} else {
										Log.e("**************","$$$$$$$$");
										value_enterManually.setFocusable(false);
										value_enterManually.setEnabled(false);
									}
									
									if (Constant.manualTipAmount.equals("null")) {
						//				value_enterManually.setText("$0.00");
										Log.e("**************","11111111111");
										value_tipTotal.setText("$0.00");
										tipsAmount1 = "0";
									} else {								
										Log.e("**************","22222222222"+Constant.manualTipAmount);			
										if(Constant.manualTipAmount.equals("$0.00")){
											value_enterManually.setHint("0.0");
										}else{
						//				value_enterManually.setText("$"	+ String.format("%.2f",	Double.parseDouble(Constant.manualTipAmount.replace("$", ""))));
										value_enterManually.setText(String.format("%.2f",	Double.parseDouble(Constant.manualTipAmount.replace("$", ""))));
										}
										value_tipTotal.setText("$"+ String.format("%.2f",Double.parseDouble(Constant.manualTipAmount.replace("$", ""))));
										
									}
									// checkBox1.setEnabled(true);
								}
							}
			//				}
						}).create().show();
			}*/

	@Override
	public void onClick(View v) {
		boolean manualAmountStatus=false;

		switch (v.getId()) {
		case R.id.btn_percentage:
			break;
		case R.id.btn_enter_manually:
			break;
		case R.id.backBtn:					
			if (tipsAmount1.equals("notAvailable")) {
				Log.e("comes here ", "1");
				Constant.tipPosition = 0;
				Constant.tipsPercent = 0;
				Constant.tipsAmount = 0;
				Constant.manualTipAmount = "null";

			} else {
			//	if (Double.parseDouble(tipsAmount1) > 0) {
				if (!value_tipTotal.getText().toString().trim().equals("$0.00")) {				
					Log.e("comes here ", "2");
					if (tipPosition == 0) {
						Log.e("comes here ", "3");
						Constant.tipPosition = 0;
						Constant.tipsPercent = 0;						
					} else {
						Log.e("comes here ", "4");
						Constant.tipPosition = tipPosition;
						Constant.tipsPercent = selectedPercent;
					}
					Constant.tipsAmount = Double.parseDouble(tipsAmount1);
				} else {
					Log.e("comes here ", "5");
					Constant.tipPosition = 0;
					Constant.tipsPercent = 0;
					Constant.tipsAmount = 0;
					Constant.manualTipAmount = "null";
				}
				
			}
			if (checkBox1.isChecked()) {				
				try {
					tipsAmount1=value_tipTotal.getText().toString().trim();
//	Constant.manualTipAmount = value_tipTotal.getText().toString().trim();
					Log.e("Inside checked Manually",tipsAmount1);
					if(tipsAmount1.startsWith("$")){
						tipsAmount1=tipsAmount1.replace("$","");
					}
					Log.e("After Removed symbol Manually",tipsAmount1);
					Constant.tipsAmount= Double.parseDouble(tipsAmount1);
					Constant.manualTipAmount = value_tipTotal.getText().toString().trim();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Constant.manualTipAmount = "null";
			}
			/*Constant.tipsAmount = 0;
			Constant.tipsPercent = 0;*/
			finish();			
			break;
		case R.id.btnDone:
	//		Log.e("Double.parseDouble(tipsAmount1)",Double.parseDouble(tipsAmount1) + "");		

			if (tipsAmount1.equals("notAvailable")) {
				Log.e("comes here ", "1");
				Constant.tipPosition = 0;
				Constant.tipsPercent = 0;
				Constant.tipsAmount = 0;
				Constant.manualTipAmount = "null";

			} else {
			//	if (Double.parseDouble(tipsAmount1) > 0) {
				if (!value_tipTotal.getText().toString().trim().equals("$0.00")) {				
					Log.e("comes here ", "2");
					if (tipPosition == 0) {
						Log.e("comes here ", "3");
						Constant.tipPosition = 0;
						Constant.tipsPercent = 0;						
					} else {
						Log.e("comes here ", "4");
						Constant.tipPosition = tipPosition;
						Constant.tipsPercent = selectedPercent;
					}
					Constant.tipsAmount = Double.parseDouble(tipsAmount1);
				} else {
					Log.e("comes here ", "5");
					Constant.tipPosition = 0;
					Constant.tipsPercent = 0;
					Constant.tipsAmount = 0;
					Constant.manualTipAmount = "null";
					tipsAmount1="0";
				}
				
			}
			if (checkBox1.isChecked()) {				
				try {
					tipsAmount1=value_tipTotal.getText().toString().trim();
//	Constant.manualTipAmount = value_tipTotal.getText().toString().trim();
					Log.e("Inside checked Manually",tipsAmount1);
					if(tipsAmount1.startsWith("$")){
						tipsAmount1=tipsAmount1.replace("$","");
					}
					Log.e("After Removed symbol Manually",tipsAmount1);
					if(Double.parseDouble(tipsAmount1)>tot){
						Utils.ShowAlert(DeliveryTotaltip.this,
								"Manually entered value should be less than subtotal value.");
					}else{
					Constant.tipsAmount= Double.parseDouble(tipsAmount1);
					Constant.manualTipAmount = value_tipTotal.getText().toString().trim();
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Constant.manualTipAmount = "null";
			}			
			if(Double.parseDouble(tipsAmount1)>tot){
				Log.e("Before Finish Manually !!!!!",tipsAmount1);
			}else{
			finish();
			}
			break;
		}
	}
	
	TextWatcher watch = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			try {

				Log.d("Percentage", "input: " + s);

				String	temp = s.toString();
			
				if(temp.contains("."))
				{
					String tempSplitting[] = temp.split("\\.");
					if (tempSplitting[0].length() > 15) {
						String tempChar = tempSplitting[0].substring(0, 15);
						s.replace(0, s.length(), tempChar + "\\."
								+ tempSplitting[1]);
					}
				}
				else
				{
					if(temp.length()>15)
					{
						String tempSplit=temp.substring(0,15);
						s.replace(0, s.length(),tempSplit );
					}
				}
				if (temp.contains(".")) {
					String tempSplitting[] = temp.split("\\.");
					Log.e("The", "Length is=>" + tempSplitting.length);
					if (tempSplitting.length > 1) {
						Log.e("The", "size is=>" + tempSplitting[1].length());
						if (tempSplitting[1].length() > 2) {
							String tempChar = tempSplitting[1].substring(0, 2);
							s.replace(0, s.length(), tempSplitting[0] + "\\."
									+ tempChar);
						}
						Log.e("replacing", "replacing");
					}
				}
				value_tipTotal.setText("$"+value_enterManually.getText().toString());
			}

			catch (NumberFormatException nfe) {
			}
			 if (s.toString().trim().length() == 0) {
				    value_tipTotal.setText("$0.00");
				   }
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			Log.e("The", "before text changed=>" + arg0);
					
		}

		@Override
		public void onTextChanged(CharSequence s, int a, int b, int c) {
			// TODO Auto-generated method stub
			
			// editText2.setText(s);

		}
	};
}
