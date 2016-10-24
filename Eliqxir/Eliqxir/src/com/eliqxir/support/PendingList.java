package com.eliqxir.support;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eliqxir.R;

public class PendingList extends BaseAdapter {

	Context mContext;
	List<UserPendingOrderClass> userPendingOrders_item;
	public PendingList(Context context,
			List<UserPendingOrderClass> userPendingOrders) {
		// TODO Auto-generated constructor stub
		mContext = context;
		userPendingOrders_item = userPendingOrders;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userPendingOrders_item.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		v = inflater.inflate(R.layout.history_list_items, null);

		TextView nameText = (TextView) v
				.findViewById(R.id.textForHistoryName);
		TextView orderText = (TextView) v
				.findViewById(R.id.textForHistoryOrderField);
		TextView totalText = (TextView) v
				.findViewById(R.id.textForHistoryTotalField);
		UserPendingOrderClass user_pending_orders = userPendingOrders_item.get(position);
		nameText.setText(user_pending_orders.vendor);
		orderText.setText(user_pending_orders.order_id);
		//totalText.setText("$"+ user_pending_orders.total);
		
		double totprice = Double.parseDouble(user_pending_orders.total);		
		totalText.setText("$" + String.format("%.2f", totprice));
		
		return v;
	}

	

}
