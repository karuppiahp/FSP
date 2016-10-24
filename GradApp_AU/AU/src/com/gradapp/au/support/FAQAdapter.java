package com.gradapp.au.support;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gradapp.au.activities.R;
import com.gradapp.au.hamburger.menus.FAQActivity.ViewHolder;

public class FAQAdapter extends BaseAdapter {
	Context context;
	ArrayList<HashMap<String, String>> faqArray;
	Typeface typeFace, typeFaceLight;
	ViewHolder holder;

	public FAQAdapter(Context baseContext,
			ArrayList<HashMap<String, String>> questionArray,
			Typeface typeface, Typeface typefaceLight, ViewHolder holderView) {
		context = baseContext;
		faqArray = questionArray;
		typeFace = typeface;
		typeFaceLight = typefaceLight;
		holder = holderView;
		Log.i("FAQ array size:", "" + faqArray.size());
	}

	@Override
	public int getCount() {
		return faqArray.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View v = arg1;
		LayoutInflater inflater = LayoutInflater.from(context);
		v = inflater.inflate(R.layout.faq_list_item, null);
		TextView textForQues = (TextView) v
				.findViewById(R.id.textForFaqQuestion);
		textForQues.setText(faqArray.get(arg0).get("faqQues"));
		textForQues.setTypeface(typeFace);
		holder.textForAnswer = (TextView) v.findViewById(R.id.textForFaqAnswer);
		holder.textForAnswer.setText(faqArray.get(arg0).get("faqAns"));
		holder.textForAnswer.setTypeface(typeFaceLight);
		holder.textForAnswer.setVisibility(View.GONE);
		return v;
	}

}
