package ventcore.server;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.Icon;
import javax.swing.JFileChooser;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ventcore.client.*;
import ventcore.client.event.*;

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
			ArrayList<FileInfo> files = new ArrayList<FileInfo>();
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
				} else if (code == WiredClient.MSG_FILE_LISTING) {
					files.add(readFile(params));
				} else if (code == WiredClient.MSG_FILE_LISTING_DONE) {
					FileListEvent event = new FileListEvent();
					event.setFiles(files);
					files = new ArrayList<FileInfo>();
					EventDispatcher.getInstance().dispatch(event, user);
				} else if (code == WiredClient.MSG_PRIVATE_CHAT_INVITE) {
					InviteEvent event = new InviteEvent();
					event.setChatId(Integer.valueOf(params.get(0)));
					event.setUserId(Integer.valueOf(params.get(1)));
					EventDispatcher.getInstance().dispatch(event, user);
				} else if (code == WiredClient.MSG_PRIVATE_MESSAGE) {
					PrivateMessageEvent event = new PrivateMessageEvent();
					event.setFromUserId(Integer.valueOf(params.get(0)));
					event.setMessage(params.get(1));
					EventDispatcher.getInstance().dispatch(event, user);
				}
			}
		};
	}

	private static long parseDate(String dateString) {
		System.out.println(dateString);
		System.out.println(ISO8601.parse(dateString).toString());
		return ISO8601.parse(dateString).getTimeInMillis();
	}

	private static FileInfo readFile(List<String> params) {
		FileInfo file = new FileInfo();
		file.setPath(params.get(0));
		file.setName(new File(params.get(0)).getName());
		file.setType(Integer.valueOf(params.get(1)));		
		file.setSize(Integer.valueOf(params.get(2)));
		file.setCreationDate(parseDate(params.get(3)));
		file.setModificationDate(parseDate(params.get(4)));
		file.setIcon(getIconAsString(params.get(0)));
		return file;
	}
	
	private static SoftReference<JFileChooser> iconFileChooserRef;
	private static Icon getFileIcon(File file) {
		JFileChooser fc = null;
		if (iconFileChooserRef != null) {
			fc = iconFileChooserRef.get();
		}
		if (fc == null) {
			fc = new JFileChooser();
			iconFileChooserRef = new SoftReference<JFileChooser>(fc);
		}
		return fc.getIcon(file);
	}

	private static String getIconAsString(String path) {
		File file = new File("/Library/Wired/files/"+path);
		if (file.exists()) {
			Icon icon = getFileIcon(file);
			if (icon != null) {
				BufferedImage image =
					new BufferedImage(icon.getIconWidth(), icon.getIconWidth(), BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = image.createGraphics();
				icon.paintIcon(null, g, 0, 0);
				g.dispose();
				image.flush();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				try {
					ImageIO.write(image, "png", out);
					return WiredClient.bytesToBase64(out.toByteArray());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	private static User readUser(List<String> params) {
		User user = new User();
		user.setId(Integer.valueOf(params.get(1)));
		user.setIdle(Boolean.valueOf(params.get(2)));
		user.setAdmin(Boolean.valueOf(params.get(3)));
		user.setNick(params.get(5));
		user.setLogin(params.get(6));
		if (params.size() > 9)
			user.setImage(params.get(9));
		return user;
	}
}
