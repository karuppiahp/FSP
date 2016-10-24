package com.fsp.blacksheep;

import java.util.Date;

public class Message_bar_date implements Comparable<Message_bar_date> {
	private String bar_date;
	private Date date;

	public String get_bar_date() {
		return bar_date;
	}

	public void set_bar_date(String bar_date) {
		this.bar_date = bar_date.trim();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(bar_date);

		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result
				+ ((bar_date == null) ? 0 : bar_date.hashCode());
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
		Message_bar_date other = (Message_bar_date) obj;

		if (bar_date == null) {
			if (other.bar_date != null)
				return false;
		} else if (!bar_date.equals(other.bar_date))
			return false;
		return true;
	}

	public int compareTo(Message_bar_date another) {
		if (another == null)
			return 1;

		return another.date.compareTo(date);
	}

}
