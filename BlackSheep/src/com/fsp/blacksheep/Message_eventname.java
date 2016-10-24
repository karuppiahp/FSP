package com.fsp.blacksheep;

import java.util.Date;

public class Message_eventname implements Comparable<Message_eventname> {
	private String eventname;
	private Date date;

	public String get_eventname() {
		return eventname;
	}

	public void set_eventname(String eventname) {
		this.eventname = eventname.trim();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(eventname);

		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result
				+ ((eventname == null) ? 0 : eventname.hashCode());
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
		Message_eventname other = (Message_eventname) obj;

		if (eventname == null) {
			if (other.eventname != null)
				return false;
		} else if (!eventname.equals(other.eventname))
			return false;
		return true;
	}

	public int compareTo(Message_eventname another) {
		if (another == null)
			return 1;

		return another.date.compareTo(date);
	}

}
