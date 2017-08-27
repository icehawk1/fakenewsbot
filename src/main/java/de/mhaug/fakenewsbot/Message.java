package de.mhaug.fakenewsbot;

import java.util.Date;

public class Message {
	private final long id;
	private final String username;
	private final String text;
	private final Date createdAt;

	public Message(long id, String username, String text, Date createdAt) {
		this.createdAt = createdAt;
		assert username!=null;
		assert text!=null;
		this.id = id;
		this.username = username;
		this.text = text;
	}

	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getText() {
		return text;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	@Override public String toString() {
		return "Message{" +
				"id=" + id +
				", username='" + username + '\'' +
				", text='" + text + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Message)) return false;

		Message message = (Message) o;
		return getId() == message.getId();

	}

	@Override
	public int hashCode() {
		return (int) (getId()%Integer.MAX_VALUE);
	}
}
