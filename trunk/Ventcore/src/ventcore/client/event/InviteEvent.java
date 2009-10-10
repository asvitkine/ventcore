package ventcore.client.event;

public class InviteEvent extends RemoteEvent {
	private int userId;
	private int chatId;

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getChatId() {
		return chatId;
	}
	public void setChatId(int chatId) {
		this.chatId = chatId;
	}	
}
