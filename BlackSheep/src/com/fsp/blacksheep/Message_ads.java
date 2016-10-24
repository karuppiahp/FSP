package com.fsp.blacksheep;

import java.util.Date;

public class Message_ads implements Comparable<Message_ads> {
	private String ads;
	private Date date;

	public String get_ads() {
		return ads;
	}

	public void set_ads(String ads) {
		this.ads = ads.trim();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(ads);

		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + ((ads == null) ? 0 : ads.hashCode());
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
		Message_ads other = (Message_ads) obj;

		if (ads == null) {
			if (other.ads != null)
				return false;
		} else if (!ads.equals(other.ads))
			return false;
		return true;
	}

	public int compareTo(Message_ads another) {
		if (another == null)
			return 1;

		return another.date.compareTo(date);
	}

}
