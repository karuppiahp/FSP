package com.fsp.blacksheep;

import java.util.Date;

public class Message_eventid implements Comparable<Message_eventid> {
	private String eventid;
	private Date date;

	public String get_eventid() {
		return eventid;
	}

	public void set_eventid(String eventid) {
		this.eventid = eventid.trim();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(eventid);

		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + ((eventid == null) ? 0 : eventid.hashCode());
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
		Message_eventid other = (Message_eventid) obj;

		if (eventid == null) {
			if (other.eventid != null)
				return false;
		} else if (!eventid.equals(other.eventid))
			return false;
		return true;
	}

	public int compareTo(Message_eventid another) {
		if (another == null)
			return 1;

		return another.date.compareTo(date);
	}

}
