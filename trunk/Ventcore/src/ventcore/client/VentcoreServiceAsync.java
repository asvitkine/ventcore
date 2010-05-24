package ventcore.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VentcoreServiceAsync {
	void login(String user, LoginInfo login,
			AsyncCallback<Void> async);

	void banUser(String user, long userId, String message,
			AsyncCallback<Void> callback);

	void broadcastMessage(String user, String message,
			AsyncCallback<Void> callback);

	void clearNews(String user,
			AsyncCallback<Void> callback);

	void setFileComment(String user, String path, String comment,
			AsyncCallback<Void> callback);

	void declineInvitation(String user, long chatId,
			AsyncCallback<Void> callback);

	void deleteFile(String user, String path,
			AsyncCallback<Void> callback);

	void deleteUser(String user, String name,
			AsyncCallback<Void> callback);

	void deleteGroup(String user, String name,
			AsyncCallback<Void> callback);

	void createFolder(String user, String path,
			AsyncCallback<Void> callback);

	void listUserAccounts(String user,
			AsyncCallback<Void> callback);

	void changeTopic(String user, long chatId, String topic,
			AsyncCallback<Void> callback);

	void createPrivateChat(String user,
			AsyncCallback<Void> callback);

	void getFile(String user, String path, int offset,
			AsyncCallback<Void> callback);

	void inviteToChat(String user, long userId, long chatId,
			AsyncCallback<Void> callback);

	void joinChat(String user, long chatId,
			AsyncCallback<Void> callback);

	void kickUser(String user, long userId, String message,
			AsyncCallback<Void> callback);

	void leaveChat(String user, long chatId,
			AsyncCallback<Void> callback);

	void moveFile(String user, String from, String to,
			AsyncCallback<Void> callback);

	void postNews(String user, String message,
			AsyncCallback<Void> callback);

	void readGroupInfo(String user, String name,
			AsyncCallback<Void> callback);

	void readUserInfo(String user, String name,
			AsyncCallback<Void> callback);

	void requestFileInfo(String user, String path,
			AsyncCallback<Void> callback);

	void requestFileList(String user, String path,
			AsyncCallback<Void> callback);

	void requestGroupList(String user,
			AsyncCallback<Void> callback);

	void requestNews(String user,
			AsyncCallback<Void> callback);

	void requestPrivilegeMask(String user,
			AsyncCallback<Void> callback);

	void requestUserInfo(String user, long userId,
			AsyncCallback<Void> callback);

	void requestUserList(String user, long chatId,
			AsyncCallback<Void> callback);

	void searchFor(String user, String query,
			AsyncCallback<Void> callback);

	void sendChatMessage(String user, long chatId, String message,
			AsyncCallback<Void> callback);

	void sendEmoteMessage(String user, long chatId, String message,
			AsyncCallback<Void> callback);

	void sendPrivateMessage(String user, long userId, String message,
			AsyncCallback<Void> callback);

	void sendStatusMessage(String user, String status,
			AsyncCallback<Void> callback);

	void disconnect(String userKey, AsyncCallback<Void> callback);
}
