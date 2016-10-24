package com.fsp.blacksheep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fsp.blacksheep.R;









import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridAdapter_thumb extends BaseAdapter {

	String[] VAL1, VAL2, VAL3;
	Context context;
	ViewHolder holder;
	LayoutInflater mInflater;
	int i = -1;
	int j = -1;
	static int k = 0;
	static int k1 = 0;
	String[] bufferarray = new String[422];
	static String[] zoombufferarray1 = new String[422];
	imageclass img = imageclass.getInstance();

	public GridAdapter_thumb(Context context, String[] url) {
		Log.v("Url here in GAthumb", "sdfds");
		for (int i = 0; i < url.length; i++) {
			Log.v("url", url[i]);
		}
		mInflater = LayoutInflater.from(context);

		bufferarray = url;

	}

	public int getCount() {
		// TODO Auto-generated method stub
		return bufferarray.length;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			arg1 = mInflater.inflate(R.layout.picture_grid_item, null);

			holder = new ViewHolder();
			holder.icon = (ImageView) arg1.findViewById(R.id.jr_lb_list_icon);

			arg1.setTag(holder);
		} else {
			holder.icon = (ImageView) arg1.findViewById(R.id.jr_lb_list_icon);
		}
		if (holder.icon != null) {
			try {
				new Pictures_DrawableManager().fetchDrawableOnThread(
						bufferarray[arg0], holder.icon);
			} catch (Exception e) {
				e.printStackTrace();
			}
			holder.icon.setTag(null);
		} else {
			holder.icon.setImageResource(R.drawable.loading_bs);

			// Non-null tag means the view still needs to load it's data
			holder.icon.setTag(this);
			holder.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
		}
		return arg1;
	}

	static class ViewHolder {
		ImageView icon;
	}

	protected void finalize() {
		img = null;

	}

}
