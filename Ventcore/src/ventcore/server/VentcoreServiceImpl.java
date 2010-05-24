package ventcore.server;

import java.io.IOException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ventcore.client.*;;

public class VentcoreServiceImpl extends RemoteServiceServlet implements VentcoreService {
	public void login(String user, LoginInfo login) {
		try {
			VentcoreWiredClient client = VentcoreWiredClient.createClientFor(user, "localhost", 2000);
			int result = client.login(login.getNick(), login.getUsername(), login.getPassword());
			if (result == 0) {
				client.requestUserList(1);
				UserManager.getInstance().getUserData(user).setWiredClient(client);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private VentcoreWiredClient getWiredClient(String user) {
		return UserManager.getInstance().getUserData(user).getWiredClient();
	}
	
	public void banUser(String user, long userId, String message) throws IOException {
		getWiredClient(user).banUser(userId, message);
	}

	public void broadcastMessage(String user, String message) throws IOException {
		getWiredClient(user).broadcastMessage(message);
	}

	public void clearNews(String user) throws IOException {
		getWiredClient(user).clearNews();
	}
	
	public void setFileComment(String user, String path, String comment) throws IOException {
		getWiredClient(user).setFileComment(path, comment);
	}

	public void declineInvitation(String user, long chatId) throws IOException {
		getWiredClient(user).declineInvitation(chatId);
	}

	public void deleteFile(String user, String path) throws IOException {
		getWiredClient(user).deleteFile(path);
	}

	public void deleteUser(String user, String name) throws IOException {
		getWiredClient(user).deleteUser(name);
	}

	public void deleteGroup(String user, String name) throws IOException {
		getWiredClient(user).deleteGroup(name);
	}

	public void createFolder(String user, String path) throws IOException {
		getWiredClient(user).createFolder(path);
	}

	public void getFile(String user, String path, int offset) throws IOException {
		getWiredClient(user).getFile(path, offset);
	}
	
	public void requestGroupList(String user) throws IOException {
		getWiredClient(user).requestGroupList();
	}

	public void requestUserInfo(String user, long userId) throws IOException {
		getWiredClient(user).requestUserInfo(userId);
	}
	
	public void inviteToChat(String user, long userId, long chatId) throws IOException {
		getWiredClient(user).inviteToChat(userId, chatId);
	}

	public void joinChat(String user, long chatId) throws IOException {
		getWiredClient(user).joinChat(chatId);
	}

	public void kickUser(String user, long userId, String message) throws IOException {
		getWiredClient(user).kickUser(userId, message);
	}

	public void leaveChat(String user, long chatId) throws IOException {
		getWiredClient(user).leaveChat(chatId);
	}

	public void requestFileList(String user, String path) throws IOException {
		getWiredClient(user).requestFileList(path);
	}
	
	public void sendEmoteMessage(String user, long chatId, String message) throws IOException {
		getWiredClient(user).sendEmoteMessage(chatId, message);
	}

	public void moveFile(String user, String from, String to) throws IOException {
		getWiredClient(user).moveFile(from, to);
	}
	
	public void sendPrivateMessage(String user, long userId, String message) throws IOException {
		getWiredClient(user).sendPrivateMessage(userId, message);
	}

	public void requestNews(String user) throws IOException {
		getWiredClient(user).requestNews();
	}

	public void postNews(String user, String message) throws IOException {
		getWiredClient(user).postNews(message);
	}

	public void createPrivateChat(String user) throws IOException {
		getWiredClient(user).createPrivateChat();
	}

	public void requestPrivilegeMask(String user) throws IOException {
		getWiredClient(user).requestPrivilegeMask();
	}

	public void readUserInfo(String user, String name) throws IOException {
		getWiredClient(user).readUserInfo(name);
	}

	public void readGroupInfo(String user, String name) throws IOException {
		getWiredClient(user).readGroupInfo(name);
	}

	public void sendChatMessage(String user, long chatId, String message) throws IOException {
		getWiredClient(user).sendChatMessage(chatId, message);
	}

	public void searchFor(String user, String query) throws IOException {
		getWiredClient(user).searchFor(query);
	}
	
	public void requestFileInfo(String user, String path) throws IOException {
		getWiredClient(user).requestFileInfo(path);
	}
	
	public void sendStatusMessage(String user, String status) throws IOException {
		getWiredClient(user).sendStatusMessage(status);
	}
	
	public void changeTopic(String user, long chatId, String topic) throws IOException {
		getWiredClient(user).changeTopic(chatId, topic);
	}

	public void listUserAccounts(String user) throws IOException {
		getWiredClient(user).listUserAccounts();
	}

	public void requestUserList(String user, long chatId) throws IOException {
		getWiredClient(user).requestUserList(chatId);
	}

	public void disconnect(String user) {
		getWiredClient(user).disconnect();
	}
}
