package com.fsp.blacksheep;

import java.util.Date;

public class Message_title implements Comparable<Message_title> {
	private String title;
	private Date date;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title.trim();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(title);

		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

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
		Message_title other = (Message_title) obj;

		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	public int compareTo(Message_title another) {
		if (another == null)
			return 1;

		return another.date.compareTo(date);
	}
}
