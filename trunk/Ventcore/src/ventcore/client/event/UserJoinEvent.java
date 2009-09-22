package ventcore.client.event;

import ventcore.client.User;

public class UserJoinEvent extends RemoteEvent {
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
