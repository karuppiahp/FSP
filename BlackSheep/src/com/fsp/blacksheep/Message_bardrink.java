package com.fsp.blacksheep;

import java.util.Date;

public class Message_bardrink implements Comparable<Message_bardrink> {
	private String bardrink;
	private Date date;

	public String get_bardrink() {
		return bardrink;
	}

	public void set_bardrink(String bardrink) {
		this.bardrink = bardrink.trim();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(bardrink);

		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result
				+ ((bardrink == null) ? 0 : bardrink.hashCode());
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
		Message_bardrink other = (Message_bardrink) obj;

		if (bardrink == null) {
			if (other.bardrink != null)
				return false;
		} else if (!bardrink.equals(other.bardrink))
			return false;
		return true;
	}

	public int compareTo(Message_bardrink another) {
		if (another == null)
			return 1;

		return another.date.compareTo(date);
	}

}
