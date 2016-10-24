package com.gradapp.au.support;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gradapp.au.AsyncTasks.SurveyAnswerTasks;
import com.gradapp.au.activities.R;
import com.gradapp.au.hamburger.menus.SurveyActivity;
import com.gradapp.au.hamburger.menus.SurveyActivity.ViewHolderSurvey;
import com.gradapp.au.utils.SessionStores;

public class SurveyAdapter extends BaseAdapter {

	ArrayList<HashMap<String, String>> surveyArray = new ArrayList<HashMap<String, String>>();
	SurveyActivity context;
	Typeface typeFace;
	ViewHolderSurvey holder;

	public SurveyAdapter(SurveyActivity mContext, Typeface typeface,
			ArrayList<HashMap<String, String>> surveyarray,
			ViewHolderSurvey holderView) {
		context = mContext;
		typeFace = typeface;
		surveyArray = surveyarray;
		holder = holderView;
	}

	@Override
	public int getCount() {
		return surveyArray.size();
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
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.survey_list_items, null);
		TextView textForQues = (TextView) v
				.findViewById(R.id.textForSurveyQuestion);
		holder.btnYes = (ImageButton) v.findViewById(R.id.imageBtnForSurveyYes);
		holder.btnNo = (ImageButton) v.findViewById(R.id.imageBtnForSurveyNo);
		textForQues.setText(surveyArray.get(arg0).get("question"));
		holder.btnYes.setTag(arg0);
		holder.btnNo.setTag(arg0);
		//Yes/No button backgrounds are set
		if (surveyArray.get(arg0).get("answer").equals("Yes")) {
			holder.btnYes.setBackgroundResource(R.drawable.yes_btn);
		}
		if (surveyArray.get(arg0).get("answer").equals("No")) {
			holder.btnNo.setBackgroundResource(R.drawable.no_btn);
		}
		textForQues.setTypeface(typeFace);
		//Yes button clicks
		holder.btnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String index = v.getTag().toString();
				final int position = Integer.parseInt(index);
				v.setBackgroundResource(R.drawable.yes_btn);
				final String ques = surveyArray.get(position).get("question");
				final String ques_id = surveyArray.get(position).get("quesid");
				new SurveyAnswerTasks(context,
						SessionStores.getUserId(context), surveyArray.get(
								position).get("quesid"), "Yes").execute();
				Handler handlerTimer = new Handler();
				handlerTimer.postDelayed(new Runnable() {
					public void run() {
						surveyArray.remove(position);
						HashMap<String, String> hashValue = new HashMap<String, String>();
						hashValue.put("quesid", ques_id);
						hashValue.put("question", ques);
						hashValue.put("answer", "Yes");
						surveyArray.add(position, hashValue);
						notifyDataSetChanged();
					}
				}, 500); //Wait for the background process finished
			}
		});
		//No button clicks
		holder.btnNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String index = v.getTag().toString();
				final int position = Integer.parseInt(index);
				v.setBackgroundResource(R.drawable.no_btn);
				final String ques = surveyArray.get(position).get("question");
				final String ques_id = surveyArray.get(position).get("quesid");
				new SurveyAnswerTasks(context,
						SessionStores.getUserId(context), surveyArray.get(
								position).get("quesid"), "No").execute();
				Handler handlerTimer = new Handler();
				handlerTimer.postDelayed(new Runnable() {
					public void run() {
						surveyArray.remove(position);
						HashMap<String, String> hashValue = new HashMap<String, String>();
						hashValue.put("quesid", ques_id);
						hashValue.put("question", ques);
						hashValue.put("answer", "No");
						surveyArray.add(position, hashValue);
						notifyDataSetChanged();
					}
				}, 500); //Wait for the background process finished
			}
		});
		return v;
	}

}
