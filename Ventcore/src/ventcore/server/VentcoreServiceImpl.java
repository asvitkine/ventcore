package ventcore.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ventcore.client.VentcoreService;
import ventcore.client.LoginInfo;
import ventcore.client.User;
import ventcore.client.event.ChatEvent;
import ventcore.client.event.UserJoinEvent;
import ventcore.client.event.UserLeaveEvent;
import ventcore.client.event.UserListEvent;

public class VentcoreServiceImpl extends RemoteServiceServlet implements VentcoreService {
	public void login(String user, LoginInfo login) {
		try {
			WiredClient client = createClientFor(user, "localhost", 2000);
			int result = client.login(login.getNick(), login.getUsername(), login.getPassword());
			if (result == 0) {
				client.requestUserList(1);
				UserManager.getInstance().getUserData(user).setWiredClient(client);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private WiredClient getWiredClient(String user) {
		return UserManager.getInstance().getUserData(user).getWiredClient();
	}
	
	public void banUser(String user, int userId, String message) throws IOException {
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

	public void declineInvitation(String user, int chatId) throws IOException {
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

	public void requestUserInfo(String user, int userId) throws IOException {
		getWiredClient(user).requestUserInfo(userId);
	}
	
	public void inviteToChat(String user, int userId, int chatId) throws IOException {
		getWiredClient(user).inviteToChat(userId, chatId);
	}

	public void joinChat(String user, int chatId) throws IOException {
		getWiredClient(user).joinChat(chatId);
	}

	public void kickUser(String user, int userId, String message) throws IOException {
		getWiredClient(user).kickUser(userId, message);
	}

	public void leaveChat(String user, int chatId) throws IOException {
		getWiredClient(user).leaveChat(chatId);
	}

	public void requestFileList(String user, String path) throws IOException {
		getWiredClient(user).requestFileList(path);
	}
	
	public void sendEmoteMessage(String user, int chatId, String message) throws IOException {
		getWiredClient(user).sendEmoteMessage(chatId, message);
	}

	public void moveFile(String user, String from, String to) throws IOException {
		getWiredClient(user).moveFile(from, to);
	}
	
	public void sendPrivateMessage(String user, int userId, String message) throws IOException {
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

	public void sendChatMessage(String user, int chatId, String message) throws IOException {
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
	
	public void changeTopic(String user, int chatId, String topic) throws IOException {
		getWiredClient(user).changeTopic(chatId, topic);
	}

	public void identifyTransfer(String user, String hash) throws IOException {
		getWiredClient(user).identifyTransfer(hash);
	}

	public void listUserAccounts(String user) throws IOException {
		getWiredClient(user).listUserAccounts();
	}

	public void requestUserList(String user, int chatId) throws IOException {
		getWiredClient(user).requestUserList(chatId);
	}

	// FIXME: all this stuff doesn't belong here!
	public static WiredClient createClientFor(final String user, String host, int port) throws Exception {
		File certificatesFile = new File("/Users/shadowknight/Projects/ventcore/ssl_certs");
		InputStream in = new FileInputStream(certificatesFile);
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(in, "changeit".toCharArray());
		in.close();

		SSLContext context = SSLContext.getInstance("TLS");
		String algorithm = TrustManagerFactory.getDefaultAlgorithm();
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
		tmf.init(ks);
		context.init(null, new TrustManager[] {tmf.getTrustManagers()[0]}, null);
		SSLSocketFactory factory = context.getSocketFactory();
		SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
		socket.setEnabledProtocols(new String[] {"TLSv1"});
		return new WiredClient(socket.getInputStream(), socket.getOutputStream()) {
			HashMap<Integer,List<User>> users = new HashMap<Integer,List<User>>();
			protected void processServerMessage(int code, List<String> params) {
				System.out.println("Got " + code);
				if (params != null) {
					System.out.println("With params: ");
					for (String p : params)
						System.out.println("  " + p);
				}
				if (code == WiredClient.MSG_CHAT || code == WiredClient.MSG_ACTION_CHAT) {
					ChatEvent event = new ChatEvent();
					event.setChatId(Integer.valueOf(params.get(0)));
					event.setUserId(Integer.valueOf(params.get(1)));
					event.setMessage(params.get(2));
					event.setEmote(code == WiredClient.MSG_ACTION_CHAT);
					EventDispatcher.getInstance().dispatch(event, user);
				} else if (code == WiredClient.MSG_USERLIST) {
					int chatId = Integer.valueOf(params.get(0));
					List<User> userlist = users.get(chatId);
					if (userlist == null) {
						userlist = new ArrayList<User>();
						users.put(chatId, userlist);
					}
					userlist.add(readUser(params));
				} else if (code == WiredClient.MSG_USERLIST_DONE) {
					int chatId = Integer.valueOf(params.get(0));
					List<User> userlist = users.get(chatId);
					if (userlist != null) {
						UserListEvent event = new UserListEvent();
						event.setChatId(chatId);
						event.setUsers(userlist);
						EventDispatcher.getInstance().dispatch(event, user);
						users.remove(chatId);
					}
				} else if (code == WiredClient.MSG_CLIENT_JOIN) {
					UserJoinEvent event = new UserJoinEvent();
					event.setChatId(Integer.valueOf(params.get(0)));
					event.setUser(readUser(params));
					EventDispatcher.getInstance().dispatch(event, user);					
				} else if (code == WiredClient.MSG_CLIENT_LEAVE) {
					UserLeaveEvent event = new UserLeaveEvent();
					event.setChatId(Integer.valueOf(params.get(0)));
					event.setUserId(Integer.valueOf(params.get(1)));
					EventDispatcher.getInstance().dispatch(event, user);	
				}
			}
		};
	}
	
	private static User readUser(List<String> params) {
		User user = new User();
		user.setId(Integer.valueOf(params.get(1)));
		user.setIdle(Boolean.valueOf(params.get(2)));
		user.setAdmin(Boolean.valueOf(params.get(3)));
		user.setNick(params.get(5));
		user.setLogin(params.get(6));
		return user;
	}
}
