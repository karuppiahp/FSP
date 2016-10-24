package com.fsp.blacksheep;

import java.util.Date;

public class Message_img implements Comparable<Message_img> {
	private String img;
	private Date date;

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img.trim();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(img);

		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + ((img == null) ? 0 : img.hashCode());
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
		Message_img other = (Message_img) obj;

		if (img == null) {
			if (other.img != null)
				return false;
		} else if (!img.equals(other.img))
			return false;
		return true;
	}

	public int compareTo(Message_img another) {
		if (another == null)
			return 1;

		return another.date.compareTo(date);
	}
}
