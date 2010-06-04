package ventcore.client;

import java.util.Date;

import wired.event.User;

public class PrivateMessage {
	private User fromUser;
	private Date timeReceived;
	private String message;

	public PrivateMessage(User fromUser, Date timeReceived, String message) {
		this.fromUser = fromUser;
		this.timeReceived = timeReceived;
		this.message = message;
	}

	public User getFromUser() {
		return fromUser;
	}

	public Date getTimeReceived() {
		return timeReceived;
	}

	public String getMessage() {
		return message;
	}
}
