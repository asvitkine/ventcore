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
import wired.EventBasedWiredClient;
import wired.WiredClient;
import wired.WiredEventHandler;
import wired.WiredUtils;
import wired.event.FileInfo;
import wired.event.WiredEvent;

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
		return new EventBasedWiredClient(socket.getInputStream(), socket.getOutputStream(),
			new WiredEventHandler() {
				public void handleEvent(WiredEvent event) {
					EventDispatcher.getInstance().dispatch(event, user);					
				}
			}
		) {
			protected FileInfo readFile(List<String> params) {
				FileInfo file = super.readFile(params);
				file.setIcon(getIconAsString(params.get(0)));
				return file;
			}
		};
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
					new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = image.createGraphics();
				icon.paintIcon(null, g, 0, 0);
				g.dispose();
				image.flush();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				try {
					ImageIO.write(image, "png", out);
					return WiredUtils.bytesToBase64(out.toByteArray());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
