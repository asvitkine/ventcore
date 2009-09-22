package ventcore.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import ventcore.client.User;
import ventcore.client.event.*;

public class UserData {
	private BlockingQueue<RemoteEvent> eventQueue;
	private WiredClient client;
	
	public UserData(String user) {
		eventQueue = new LinkedBlockingQueue<RemoteEvent>();
		String host = "localhost";
		int port = 2000;
		try {
			client = createClientFor(user, host, port);
			int result = client.login("Wired User", "guest", null);
			if (result == 0) {
				client.requestUserList(1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BlockingQueue<RemoteEvent> getEventQueue() {
		return eventQueue;
	}

	public WiredClient getWiredClient() {
		return client;
	}

	// FIXME: all this stuff doesn't belong here!
	public static WiredClient createClientFor(final String user, String host, int port) throws Exception {
		File certificatesFile = new File("/tmp/ssl_certs");
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
			List<User> users = new ArrayList<User>();
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
					// FIXME: userlists for different chats (first param)
					users.add(readUser(params));
				} else if (code == WiredClient.MSG_USERLIST_DONE) {
					UserListEvent event = new UserListEvent();
					event.setUsers(users);
					EventDispatcher.getInstance().dispatch(event, user);					
					users = new ArrayList<User>();
				} else if (code == WiredClient.MSG_CLIENT_JOIN) {
					// TODO chatid
					UserJoinEvent event = new UserJoinEvent();
					event.setUser(readUser(params));
					EventDispatcher.getInstance().dispatch(event, user);					
				} else if (code == WiredClient.MSG_CLIENT_LEAVE) {
					// TODO chatid
					// TODO chatid
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
