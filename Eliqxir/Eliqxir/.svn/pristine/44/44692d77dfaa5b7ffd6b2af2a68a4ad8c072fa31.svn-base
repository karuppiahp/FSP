package com.eliqxir.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextOpensansSemiBold extends TextView {

	public CustomTextOpensansSemiBold(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CustomTextOpensansSemiBold(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomTextOpensansSemiBold(Context context) {
		super(context);
		init();
	}

	public void init() {
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
				"OpenSans-Semibold_0.ttf");
		setTypeface(tf, 1);

	}

}