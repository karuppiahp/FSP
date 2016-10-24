package com.fsp.blacksheep;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class Message_thbnail2 implements Comparable<Message_thbnail2> {
	static SimpleDateFormat FORMATTER = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss Z");
	private String title;
	private URL link;
	private String description;
	private Date date;
	private String thumbnail;

	public void setThumbnail(String thumbnail) {
		Log.v("thumbnail", thumbnail);
		if (thumbnail.contains("%20%20")) {
			// thumbnail=thumbnail.replace("%20%20"," ");
			// Log.v("thumbnail###", thumbnail);
			this.thumbnail = thumbnail;
		} else {
			this.thumbnail = thumbnail;
			Log.v("thumbnail***", thumbnail);
		}
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public Message_thbnail2 copy() {
		Message_thbnail2 copy = new Message_thbnail2();
		copy.title = title;
		copy.link = link;
		copy.description = description;
		copy.date = date;
		copy.thumbnail = thumbnail;
		return copy;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(thumbnail);
		// sb.append(zoom);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message_thbnail2 other = (Message_thbnail2) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	public int compareTo(Message_thbnail2 another) {
		if (another == null)
			return 1;

		return another.date.compareTo(date);
	}

}
