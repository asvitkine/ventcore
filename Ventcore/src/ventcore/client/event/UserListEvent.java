package ventcore.client.event;

import java.util.List;

import ventcore.client.User;

public class UserListEvent extends RemoteEvent {
	private int chatId;
	private List<User> users;

	public int getChatId() {
		return chatId;
	}
	public void setChatId(int chatId) {
		this.chatId = chatId;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	public List<User> getUsers() {
		return users;
	}
}
