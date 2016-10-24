package com.gradapp.au.support;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class LocationMethods {

	public static double[] getlocation(Context context) {
		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = lm.getProviders(true);

		Location l = null;
		for (int i = 0; i < providers.size(); i++) {
			l = lm.getLastKnownLocation(providers.get(i));
			if (l != null)
				break;
		}
		double[] gps = new double[2];

		if (l != null) {
			gps[0] = l.getLatitude();
			gps[1] = l.getLongitude();
		}
		return gps;
	}

	public static String getMapsApiDirectionsUrl(double latitudeCurrent,
			double longitudeCurrent, double eventLatitude, double eventLongitude) {
		String waypoints = "waypoints=optimize:true|" + latitudeCurrent + ","
				+ longitudeCurrent + "|" + "|" + eventLatitude + ","
				+ eventLongitude;

		String sensor = "sensor=false";
		String params = waypoints + "&" + sensor + "&mode=driving";
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + params + "&alternatives=true";
		return url;
	}
}
