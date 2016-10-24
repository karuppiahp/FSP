package com.fsp.blacksheep;

import java.util.HashMap;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class ArticleDetailedTask extends AsyncTask<Void, Void, Void> {
	
	private final ProgressDialog dialog;
	private String url = ParserClass.articleDetails;
	private String school, articleId, image, author, description = "", date;
	private TextView descText, articleDateAuthor;
	private ImageGetter imgGetter;
	private int density;
	
	public ArticleDetailedTask(Context context, TextView descText, TextView articleDateAuthor, String school, String articleId, ImageGetter imgGetter, int density) {
		this.school = school;
		this.articleId = articleId;
		this.descText = descText;
		this.imgGetter = imgGetter;
		this.density = density;
		this.articleDateAuthor = articleDateAuthor;
		dialog = new ProgressDialog(context);
	}

	protected void onPreExecute() {
		this.dialog.setMessage("Loading...");
		this.dialog.setCancelable(false);
		this.dialog.show();
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		String res = Parsing_JSON.readFeed(url + "LSU" + "&article=" + articleId);
		Log.e("The", "Articles detail response is=>>" + res);
		// Log.v("registration response >>",res);
		JSONObject job1;
		try {
			job1 = new JSONObject(res);

				JSONObject 	datObj = job1.getJSONObject("data");

				image = datObj.getString("image");
				author = datObj.getString("author");
				date = datObj.getString("article_date");
				description = datObj.getString("article_content");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	protected void onPostExecute(Void result) {
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}
		
		if(description.length() > 0) {
			String[] dateSplits = date.split(" ");
			String dateConvertion = Utils.dateConvertion(dateSplits[0]);
			
			String date = "<font color='#111111'>" + dateConvertion +" by </font>";
	        String authorName = "<font color='#00baf2'>" + author + "</font>";
			
			articleDateAuthor.setText(Html.fromHtml(date + authorName));
			
			if(description.length() == 0) {
				description = " ";
			}
			
			descText.setText(Html.fromHtml(description, imgGetter, null));
			
			int s = descText.getText().toString().length();
			Log.e("Total Length of the string",""+s);
			
			LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			descText.setLayoutParams(lp);
			
		}
	}
}
