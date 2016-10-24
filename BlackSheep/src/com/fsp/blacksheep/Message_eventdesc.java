package com.fsp.blacksheep;

import java.util.Date;

public class Message_eventdesc implements Comparable<Message_eventdesc> {
	private String eventdesc;
	private Date date;

	public String get_eventdesc() {
		return eventdesc;
	}

	public void set_eventdesc(String eventdesc) {
		this.eventdesc = eventdesc.trim();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(eventdesc);

		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result
				+ ((eventdesc == null) ? 0 : eventdesc.hashCode());
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
		Message_eventdesc other = (Message_eventdesc) obj;

		if (eventdesc == null) {
			if (other.eventdesc != null)
				return false;
		} else if (!eventdesc.equals(other.eventdesc))
			return false;
		return true;
	}

	public int compareTo(Message_eventdesc another) {
		if (another == null)
			return 1;

		return another.date.compareTo(date);
	}

}
