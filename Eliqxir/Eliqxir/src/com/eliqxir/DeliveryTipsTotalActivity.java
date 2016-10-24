package com.eliqxir;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.eliqxir.adapter.SpinnerAdapter;
import com.eliqxir.slidermenu.SlidingMenuActivity;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class DeliveryTipsTotalActivity extends SlidingMenuActivity implements
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
	private String current = "", selectedIteration,
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
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
		btnDone = (Button) findViewById(R.id.btnDone);
		btnDone.setOnClickListener(this);
		label_percentage = (TextView) findViewById(R.id.percentage_label);
		label_enterManually = (TextView) findViewById(R.id.enter_manual_label);
		value_percentage = (Spinner) findViewById(R.id.percentage_value);
		value_enterManually = (EditText) findViewById(R.id.enter_manual_value);

		label_tipTotal = (TextView) findViewById(R.id.total_label);
		value_tipTotal = (TextView) findViewById(R.id.total_value);

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
		
		
		value_enterManually.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (!s.toString().equals(current)) {
					try {
						value_enterManually.removeTextChangedListener(this);
						String cleanString = s.toString().replaceAll("[$,.]", "");
						double parsed = Double.parseDouble(cleanString);
						String formatted = NumberFormat.getCurrencyInstance()	.format((parsed / 100));
						current = formatted;
						value_enterManually.setText(formatted);
						value_tipTotal.setText(formatted);
						String formattedDouble = formatted.replace("$", "").replace(",", "");
						formattedDouble = String.format("%.2f",Double.parseDouble(formattedDouble));
						tipsAmount1 = formattedDouble;		
						Log.e("Amount inside textwatcher",tipsAmount1);
						double tmp = (Double.parseDouble(formattedDouble) / tot) * 100;
						Log.e("tmp value textwatcher",""+tmp);
						Constant.tipsPercent = Math.round(tmp);
						Log.e("Constant.tipsPercent value textwatcher",""+Constant.tipsPercent);
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
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
			}

		});

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
						if (position != 0) {
							// tipPosition = position;
							Constant.manualTipAmount = "$0.00";
							String sp[] = selectedIteration.split(" ");
							Log.e("sp[0]", sp[0]);
							selectedPercent = Integer.parseInt(sp[0]);
							// Constant.tipsPercent = selectedPercent;
							// double t = tot - ((tot * selectedPercent)) / 100;
							double t = (tot * selectedPercent) / 100;

							tipsAmount1 = String.format("%.2f", t);
							value_tipTotal.setText("$" + tipsAmount1);
							// Constant.tipsAmount = Double
							// .parseDouble(tipsAmount1);

							value_enterManually.setFocusable(false);
							value_enterManually.setEnabled(false);
							value_enterManually.setText("$0.00");
							// checkBox1.setEnabled(false);
						} else {
							if (checkBox1.isChecked()) {
								value_enterManually.setFocusable(true);
								value_enterManually
										.setFocusableInTouchMode(true);
								value_enterManually.setEnabled(true);
							} else {
								value_enterManually.setFocusable(false);
								value_enterManually.setEnabled(false);
							}
							if (Constant.manualTipAmount.equals("null")) {
								Log.e("******************888", "11111");
								value_enterManually.setText("$0.00");
								value_tipTotal.setText("$0.00");
								tipsAmount1 = "0";
							} else {
								Log.e("!!!!!!!!!!!!!!!!!!!!!", "11111");
								value_enterManually.setText("$"
										+ String.format("%.2f",
												Double.parseDouble(
												Constant.manualTipAmount.replace("$", ""))));
								value_tipTotal.setText("$"
										+ String.format("%.2f",
												Double.parseDouble(
												Constant.manualTipAmount.replace("$", ""))));
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
				Log.e("############", "11111");
				value_enterManually.setText("$0.00");
				value_tipTotal.setText("$0.00");
			} catch (Exception e) {
				Log.e("Exception  22",""+e);
			}
		} else {
			Log.e("************", "11111");
			value_enterManually.setText("$"
					+ String.format("%.2f", Double.parseDouble(Constant.manualTipAmount.replace("$", ""))));
			value_tipTotal.setText("$"
					+ String.format("%.2f", Double.parseDouble(Constant.manualTipAmount.replace("$", ""))));
			checkBox1.setChecked(true);

		}
		if (checkBox1.isChecked()) {

			Log.e("2232323232323", "223232332323");
			value_percentage.setSelection(0);
			value_percentage.setEnabled(false);
			value_enterManually.setFocusable(true);
			value_enterManually.setFocusableInTouchMode(true);
			value_enterManually.setEnabled(true);
			checkBox1.setEnabled(true);
		} else {
			Log.e("3454545455", "345454554");
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
					Log.e("1111", "11111");
					if (checkBox1.isChecked()) {
						value_tipTotal.setText("$0.00");
						Log.e("2222", "2222");
						value_percentage.setSelection(0);
						value_percentage.setEnabled(false);
						value_enterManually.setFocusable(true);
						value_enterManually.setFocusableInTouchMode(true);
						value_enterManually.setEnabled(true);
						checkBox1.setEnabled(true);
					} else {
						Log.e("3333", "3333");
						// value_percentage.setSelection(0);
						value_percentage.setEnabled(true);
						value_enterManually.setFocusable(false);
						value_enterManually.setEnabled(false);
					}

				} else {
					Log.e("4333", "4333");
					value_enterManually.setText("$0.00");
					// isTermsChecked = false;
					value_percentage.setEnabled(true);
					value_enterManually.setFocusable(false);
					value_enterManually.setEnabled(false);
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		boolean manualAmountStatus=false;

		switch (v.getId()) {
		case R.id.btn_percentage:
			break;
		case R.id.btn_enter_manually:
			break;
		case R.id.backBtn:					
			Constant.tipsAmount = 0;
			Constant.tipsPercent = 0;
			finish();			
			break;
		case R.id.btnDone:
			Log.e("Double.parseDouble(tipsAmount1)",
					Double.parseDouble(tipsAmount1) + "");
			
			
//			if (!value_enterManually.getText().toString().equals("$0.00")) {
//				Constant.manualTipAmount =value_enterManually.getText().toString();
//			}
//			else {
//				Constant.manualTipAmount = 0;
//			}
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
				Log.e("Manual Amount",value_tipTotal.getText().toString().trim());
				Constant.manualTipAmount = value_tipTotal.getText().toString().trim();				
			} else {
				Constant.manualTipAmount = "null";
			}
			finish();
			break;

		}

	}
}
