package com.gradapp.au.support;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gradapp.au.homescreen.NotificationsActivity;
import com.gradapp.au.utils.BadgeUtils;
import com.gradapp.au.utils.SessionStores;
import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

public class IntentReceiver extends BroadcastReceiver {

	String appid;

	@SuppressLint("InlinedApi")
	@Override
	public void onReceive(Context context, Intent intent) {

		// For device appid value from intent receiver page
		String action = intent.getAction();
		appid = intent.getStringExtra(PushManager.EXTRA_APID);
		SessionStores.saveAppId(appid, context);

		if (action.equals(PushManager.ACTION_PUSH_RECEIVED)) {
			// Count of unread count is set
			String count = SessionStores.getUnreadCount(context);
			if (count != null && count.length() >= 0) {
				if (Integer.parseInt(count) >= 0) {
					int finalCount = Integer.parseInt(count) + 1;
					SessionStores.saveUnreadCount("" + finalCount, context);
					BadgeUtils.setBadge(context, finalCount);
				}
			} else {
				BadgeUtils.setBadge(context, -1);
			}
		} else if (action.equals(PushManager.ACTION_NOTIFICATION_OPENED)) {
			Intent in = new Intent(Intent.ACTION_MAIN);
			in.setClass(UAirship.shared().getApplicationContext(),
					NotificationsActivity.class);
			in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			UAirship.shared().getApplicationContext().startActivity(in);
		} else if (action.equals(PushManager.ACTION_REGISTRATION_FINISHED)) {
		}
	}
}