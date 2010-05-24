package ventcore.client;

import java.io.IOException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("chat")
public interface VentcoreService extends RemoteService {
	public void login(String user, LoginInfo login);
	public void banUser(String user, long userId, String message) throws IOException;
	public void broadcastMessage(String user, String message) throws IOException;
	public void clearNews(String user) throws IOException;
	public void setFileComment(String user, String path, String comment) throws IOException;
	public void declineInvitation(String user, long chatId) throws IOException;
	public void deleteFile(String user, String path) throws IOException;
	public void deleteUser(String user, String name) throws IOException;
	public void deleteGroup(String user, String name) throws IOException;
	public void createFolder(String user, String path) throws IOException;
	public void getFile(String user, String path, int offset) throws IOException;
	public void requestGroupList(String user) throws IOException;
	public void requestUserInfo(String user, long userId) throws IOException;
	public void inviteToChat(String user, long userId, long chatId) throws IOException;
	public void joinChat(String user, long chatId) throws IOException;
	public void kickUser(String user, long userId, String message) throws IOException;
	public void leaveChat(String user, long chatId) throws IOException;
	public void requestFileList(String user, String path) throws IOException;
	public void sendEmoteMessage(String user, long chatId, String message) throws IOException;
	public void moveFile(String user, String from, String to) throws IOException;
	public void sendPrivateMessage(String user, long userId, String message) throws IOException;
	public void requestNews(String user) throws IOException;
	public void postNews(String user, String message) throws IOException;
	public void createPrivateChat(String user) throws IOException;
	public void requestPrivilegeMask(String user) throws IOException;
	public void readUserInfo(String user, String name) throws IOException;
	public void readGroupInfo(String user, String name) throws IOException;
	public void sendChatMessage(String user, long chatId, String message) throws IOException;
	public void searchFor(String user, String query) throws IOException;
	public void requestFileInfo(String user, String path) throws IOException;
	public void sendStatusMessage(String user, String status) throws IOException;
	public void changeTopic(String user, long chatId, String topic) throws IOException;
	public void listUserAccounts(String user) throws IOException;
	public void requestUserList(String user, long chatId) throws IOException;
	public void disconnect(String user);
}
