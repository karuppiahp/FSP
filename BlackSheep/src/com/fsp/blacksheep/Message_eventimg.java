package com.fsp.blacksheep;

import java.util.Date;

public class Message_eventimg implements Comparable<Message_eventimg> {
	private String eventimg;
	private Date date;

	public String get_eventimg() {
		return eventimg;
	}

	public void set_eventimg(String eventimg) {
		this.eventimg = eventimg.trim();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(eventimg);

		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result
				+ ((eventimg == null) ? 0 : eventimg.hashCode());
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
		Message_eventimg other = (Message_eventimg) obj;

		if (eventimg == null) {
			if (other.eventimg != null)
				return false;
		} else if (!eventimg.equals(other.eventimg))
			return false;
		return true;
	}

	public int compareTo(Message_eventimg another) {
		if (another == null)
			return 1;

		return another.date.compareTo(date);
	}

}
