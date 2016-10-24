package com.fsp.blacksheep;

import java.util.Date;

public class Message_eventdate implements Comparable<Message_eventdate> {
	private String eventdate;
	private Date date;

	public String get_eventdate() {
		return eventdate;
	}

	public void set_eventdate(String eventdate) {
		this.eventdate = eventdate.trim();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(eventdate);

		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result
				+ ((eventdate == null) ? 0 : eventdate.hashCode());
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
		Message_eventdate other = (Message_eventdate) obj;

		if (eventdate == null) {
			if (other.eventdate != null)
				return false;
		} else if (!eventdate.equals(other.eventdate))
			return false;
		return true;
	}

	public int compareTo(Message_eventdate another) {
		if (another == null)
			return 1;

		return another.date.compareTo(date);
	}

}
