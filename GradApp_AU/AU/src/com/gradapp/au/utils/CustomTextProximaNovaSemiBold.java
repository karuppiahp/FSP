package com.gradapp.au.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextProximaNovaSemiBold extends TextView {

	public CustomTextProximaNovaSemiBold(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CustomTextProximaNovaSemiBold(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomTextProximaNovaSemiBold(Context context) {
		super(context);
		init();
	}

	public void init() {
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/ProximaNova-Semibold.otf");
		setTypeface(tf, 1);

	}

}
