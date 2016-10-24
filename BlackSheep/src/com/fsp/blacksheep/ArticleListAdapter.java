package com.fsp.blacksheep;

import java.util.ArrayList;

import java.util.HashMap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import android.app.Activity;
import android.graphics.Bitmap.CompressFormat;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ArticleListAdapter extends BaseAdapter {
	
	Activity context;
	TextView txtForTitle, txtForDesc;
	ImageView imageView;
	private ImageLoader imageLoader;
	private ImageLoaderConfiguration config;
	DisplayImageOptions  options;
	ArrayList<HashMap<String, String>> articlesArrayList = new ArrayList<HashMap<String,String>>();
	
	public ArticleListAdapter(Activity context, ArrayList<HashMap<String, String>> articlesArrayList, ImageLoader imageLoader) {
		this.context = context;
		this.articlesArrayList = articlesArrayList;
		this.imageLoader = imageLoader;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return articlesArrayList.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View v = convertView;
		LayoutInflater inflator = LayoutInflater.from(context);
		v = inflator.inflate(R.layout.articles_list_item, null);
		txtForTitle = (TextView) v.findViewById(R.id.txtForArticleTitle);
		txtForDesc = (TextView) v.findViewById(R.id.txtForArticleDesc);
		imageView = (ImageView) v.findViewById(R.id.imgForArticle);
		if(articlesArrayList.get(position).get("image").length() > 0) {
			imageLoader.displayImage(articlesArrayList.get(position).get("image"), imageView);
		}
		txtForTitle.setText(Html.fromHtml(articlesArrayList.get(position).get("title")));
		if(articlesArrayList.get(position).get("description").length() > 0) {
			URLImageParserHtml p = new URLImageParserHtml(txtForDesc, context);
			Spanned htmlSpan = Html.fromHtml(articlesArrayList.get(position).get("description"), p, null);
			txtForDesc.setText(htmlSpan);
		}
		return v;
	}

}
