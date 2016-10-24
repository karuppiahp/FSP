package com.fsp.blacksheep;

import java.net.URL;





import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class MultiLine_bar_detail_view extends BaseAdapter implements
		ListView.OnScrollListener {
	static ViewHolder holder;
	static boolean mBusy = false;
	static View view_name;
	LayoutInflater mInflater;
	static String[] VAL1;
	static String[] VAL2;
	static String[] VAL3;
	static URL img_value;
	private String[] VAL4;
	String name;
	String TAG = "MultiLine_img1";
	int i = -1;
	int j = -1;
	int k = -1;
	private AsyncImageLoader asyncImageLoader;
	static ImageView img;
	static Bitmap mIcon11;
	ListView lv;

	public MultiLine_bar_detail_view(Context context, String[] value1,
			String[] value2, String[] value3, String[] value4) {
		mInflater = LayoutInflater.from(context);
		// asyncImageLoader = new AsyncImageLoader();
		ImageThreadLoader imageLoader = new ImageThreadLoader();
		VAL1 = value1;
		VAL2 = value2;
		VAL3 = value3;
		VAL4 = value4;
	}

	public int getCount() {
		return VAL1.length;
	}

	public Object getItem(int arg0) {
		return arg0;
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.bs_content_list_item1,
					null);

			holder = new ViewHolder();
			holder.text1 = (TextView) convertView
					.findViewById(R.id.text1_detail1);
			holder.text2 = (TextView) convertView
					.findViewById(R.id.text3_detail1);
			convertView.setTag(holder);
		} else {
			holder.text1 = (TextView) convertView
					.findViewById(R.id.text1_detail1);
			holder.text2 = (TextView) convertView
					.findViewById(R.id.text3_detail1);
		}
		if (!mBusy) {
			try {
				img_value = new URL(VAL4[position]);
				mIcon11 = BitmapFactory.decodeStream(img_value.openConnection()
						.getInputStream());
				holder.icon.setImageBitmap(mIcon11);
			} catch (Exception e) {
				e.printStackTrace();
			}
			holder.icon.setTag(null);
		} else {
			holder.icon.setImageResource(R.drawable.loading_bs);

			// Non-null tag means the view still needs to load it's data
			holder.icon.setTag(this);
		}
		holder.text1.setText(VAL1[position]);
		holder.text2.setText(VAL3[position]);

		return convertView;
	}

	static class ViewHolder {
		TextView text1, text2, text3;
		ImageView icon;
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_IDLE:
			mBusy = false;

			int first = view.getFirstVisiblePosition();
			int count = view.getChildCount();
			for (int i = 0; i < count; i++) {

				if (holder.icon.getTag() != null) {
					try {

						img_value = new URL(VAL4[first + i]);
						mIcon11 = BitmapFactory.decodeStream(img_value
								.openConnection().getInputStream());
						holder.icon.setImageBitmap(mIcon11);
					} catch (Exception e) {
						e.printStackTrace();
					}
					holder.icon.setTag(null);

				}
			}

			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			mBusy = true;
			break;
		case OnScrollListener.SCROLL_STATE_FLING:
			mBusy = true;
			break;
		}

	}

}
