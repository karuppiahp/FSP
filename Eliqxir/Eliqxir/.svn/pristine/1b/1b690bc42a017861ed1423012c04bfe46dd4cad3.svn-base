package com.eliqxir.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextOpensansRegular extends TextView {

	public CustomTextOpensansRegular(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CustomTextOpensansRegular(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomTextOpensansRegular(Context context) {
		super(context);
		init();
	}

	public void init() {
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
				"OpenSans-Regular.ttf");
		setTypeface(tf, 1);

	}

}