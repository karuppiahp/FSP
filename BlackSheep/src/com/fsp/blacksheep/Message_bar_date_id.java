package com.fsp.blacksheep;

import java.util.Date;

public class Message_bar_date_id implements Comparable<Message_bar_date_id> {
	private String bar_date_id;
	private Date date;

	public String get_bar_date_id() {
		return bar_date_id;
	}

	public void set_bar_date_id(String bar_date_id) {
		this.bar_date_id = bar_date_id.trim();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(bar_date_id);

		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result
				+ ((bar_date_id == null) ? 0 : bar_date_id.hashCode());
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
		Message_bar_date_id other = (Message_bar_date_id) obj;

		if (bar_date_id == null) {
			if (other.bar_date_id != null)
				return false;
		} else if (!bar_date_id.equals(other.bar_date_id))
			return false;
		return true;
	}

	public int compareTo(Message_bar_date_id another) {
		if (another == null)
			return 1;

		return another.date.compareTo(date);
	}

}
