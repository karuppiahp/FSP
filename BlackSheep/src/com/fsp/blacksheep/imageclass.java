package com.fsp.blacksheep;

import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;

public class imageclass {
	public ArrayList<String> thumbimage = new ArrayList<String>();
	public ArrayList<String> zoomimage = new ArrayList<String>();
	String[] thumbimage_arrray;
	String[] zoomimage_arrray;
	int startup = 0;
	int but_starup = 0;
	private static imageclass instance;

	private imageclass() {

	}

	public static imageclass getInstance() {
		if (instance == null) {
			instance = new imageclass();

		}

		return instance;
	}

	public void clear() {
		instance = null;
	}

	public void setThumbimage(String url) {
		// Log.v("tag",url);
		this.thumbimage.add(url);
	}

	public void setZoomimage(String url) {
		this.zoomimage.add(url);
	}

	public ArrayList<String> getThumbimage() {
		return this.thumbimage;
	}

	public ArrayList<String> getZoomimage() {
		return this.zoomimage;
	}

	public int thumbcount() {
		return this.thumbimage.size();
	}

	public int zoomcount() {
		return this.zoomimage.size();
	}

	public void print_thumbimage() {
		Iterator<String> it = thumbimage.iterator();
		while (it.hasNext()) {
			Log.v("thumbimage", it.next().toString());

		}

	}

	public void print_zoomimage() {
		Iterator<String> it = zoomimage.iterator();
		while (it.hasNext()) {
			Log.v("zoomimage", it.next().toString());

		}

	}

	public String[] ary_thumpimage() {

		thumbimage_arrray = (String[]) thumbimage.toArray(new String[thumbimage
				.size()]);
		return this.thumbimage_arrray;

	}

	public String[] ary_zoomimage() {

		zoomimage_arrray = (String[]) zoomimage.toArray(new String[zoomimage
				.size()]);
		return this.zoomimage_arrray;

	}

	public void print_thumbimage_array() {
		thumbimage_arrray = this.ary_thumpimage();
		for (int i = 0; i < thumbimage_arrray.length; i++) {
			Log.v("thumb_arrray" + i, thumbimage_arrray[i]);
		}
	}

	public void print_zoomimage_array() {
		zoomimage_arrray = this.ary_zoomimage();
		for (int i = 0; i < zoomimage_arrray.length; i++) {
			Log.v("zoom_arrray" + i, zoomimage_arrray[i]);
		}
	}

	protected void finalize() {

	}

}
