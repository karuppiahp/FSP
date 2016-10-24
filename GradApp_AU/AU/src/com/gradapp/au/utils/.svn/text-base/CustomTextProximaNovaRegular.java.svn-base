package com.gradapp.au.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextProximaNovaRegular extends TextView {

	public CustomTextProximaNovaRegular(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CustomTextProximaNovaRegular(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomTextProximaNovaRegular(Context context) {
		super(context);
		init();
	}

	public void init() {
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/ProximaNova-Regular.otf");
		setTypeface(tf, 1);

	}

}