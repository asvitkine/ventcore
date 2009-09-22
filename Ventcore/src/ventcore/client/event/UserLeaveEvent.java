package ventcore.client.event;

public class UserLeaveEvent extends RemoteEvent {
	private int chatId;
	private int userId;

	public int getChatId() {
		return chatId;
	}
	public void setChatId(int chatId) {
		this.chatId = chatId;
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
