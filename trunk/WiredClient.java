
import java.io.*;
import java.net.*;
import java.util.*;
import java.security.*;

public class WiredClient {
	private static final byte EOT = 4;
	private static final byte FS = 28;
	private static final byte GS = 29;
	private static final byte RS = 30;
	private static final byte SP = 32;
	
	private static final int MSG_SERVER_INFO = 200;
	private static final int MSG_LOGIN_SUCCESS = 201;
	private static final int MSG_PING_REPLY = 202;
	private static final int MSG_SERVER_BANNER = 203;

	private static final int MSG_CHAT = 300;
	private static final int MSG_ACTION_CHAT = 301;
	private static final int MSG_CLIENT_JOIN = 302;
	private static final int MSG_CLIENT_LEAVE = 303;
	private static final int MSG_STATUS_CHANGE = 304;
	private static final int MSG_PRIVATE_MESSAGE = 305;
	private static final int MSG_CLIENT_KICKED = 306;
	private static final int MSG_CLIENT_BANNED = 307;
	private static final int MSG_CLIENT_INFO = 308;
	private static final int MSG_BROADCAST = 309;
	private static final int MSG_USERLIST = 310;
	private static final int MSG_USERLIST_DONE = 311;
	private static final int MSG_NEWS = 320;
	private static final int MSG_NEWS_DONE = 321;
	private static final int MSG_NEWS_POSTED = 322;
	private static final int MSG_PRIVATE_CHAT_CREATED = 330;
	private static final int MSG_PRIVATE_CHAT_INVITE = 331;
	private static final int MSG_PRIVATE_CHAT_DECLINED = 332;
	private static final int MSG_PRIVATE_IMAGE_CHANGE = 340;
	private static final int MSG_CHAT_TOPIC = 341;

	private static final int MSG_TRANSFER_READY = 400;
	private static final int MSG_TRANSFER_QUEUED = 401;
	private static final int MSG_FILE_INFO = 402;
	private static final int MSG_FILE_LISTING = 410;
	private static final int MSG_FILE_LISTING_DONE = 411;
	private static final int MSG_SEARCH_LISTING = 420;
	private static final int MSG_SEARCH_LISTING_DONE = 421;

	private static final int MSG_COMMAND_FAILED = 500;
	private static final int MSG_COMMAND_NOT_RECOGNIZED = 501;
	private static final int MSG_COMMAND_NOT_IMPLEMENTED = 502;
	private static final int MSG_SYNTAX_ERROR = 503;
	private static final int MSG_LOGIN_FAILED = 510;
	private static final int MSG_BANNED = 511;
	private static final int MSG_CLIENT_NOT_FOUND = 512;
	private static final int MSG_ACCOUNT_NOT_FOUND = 513;
	private static final int MSG_ACCOUNT_EXISTS = 514;
	private static final int MSG_CANNOT_BE_DISCONNECTED = 515;
	private static final int MSG_PERMISSION_DENIED = 516;
	private static final int MSG_FILE_OR_DIRECTORY_NOT_FOUND = 520;
	private static final int MSG_FILE_OR_DIRECTORY_EXISTS = 521;
	private static final int MSG_CHECKSUM_MISMATCH = 522;
	private static final int MSG_QUEUE_LIMIT_EXCEEDED = 523;

	private static final int MSG_USER_SPECIFICATION = 600;
	private static final int MSG_GROUP_SPECIFICATION = 601;
	private static final int MSG_PRIVILEGES_SPECIFICATION = 602;
	private static final int MSG_USER_LISTING = 610;
	private static final int MSG_USER_LISTING_DONE = 611;
	private static final int MSG_GROUP_LISTING = 620;
	private static final int MSG_GROUP_LISTING_DONE = 621;

	private String host;
	private int port;
	private OutputStream out;
	private DataInputStream in;

	public WiredClient(InputStream in, OutputStream out) {
		this.in = new DataInputStream(in);
		this.out = out;
	}

	public int login(String nick, String user, String pass) throws IOException {
		ServerMessage msg;
		sendHello();
		msg = readServerMessage();
		if (msg.code != 200)
			return msg.code;
		sendNick(nick);
		sendUsername(user);
		sendPassword(pass);
		msg = readServerMessage();
		if (msg.code != 201)
			return msg.code;
		new Thread(new Runnable() { public void run() { processServerMessages(); } }).start();
		final Timer timer = new Timer();
		TimerTask tt = new TimerTask() { public void run() { try { sendPing(); } catch (IOException e) { timer.cancel(); } }};
		timer.scheduleAtFixedRate(tt, 0, 60 * 1000);
		return 0;
	}
	
	private void processServerMessages() {
		while (true) {
			try {
				ServerMessage msg = readServerMessage();
				System.out.println("Got " + msg.code);
				if (msg.params != null) {
					System.out.println("With params: ");
					for (String p : msg.params)
						System.out.println("  " + p);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public synchronized void banUser(int userId, String message) throws IOException {
		send("BAN").send(SP).send(""+userId).send(FS).send(message).send(EOT);
	}

	public synchronized void requestBanner() throws IOException {
		send("BANNER").send(EOT);
	}

	public synchronized void broadcastMessage(String message) throws IOException {
		send("BROADCAST").send(SP).send(message).send(EOT);
	}

	public synchronized void clearNews() throws IOException {
		send("CLEARNEWS").send(EOT);
	}

	public synchronized void sendClientVersion(String clientVersion) throws IOException {
		send("CLIENT").send(SP).send(clientVersion).send(EOT);
	}
	
	public synchronized void setFileComment(String path, String comment) throws IOException {
		send("COMMENT").send(SP).send(path).send(FS).send(comment).send(EOT);
	}

	public synchronized void createUser(String name, String password, String group, Object privileges) throws IOException {
	// FIXME: what is privileges?
	// send("CREATEUSER").send(SP).send(name).send(FS).send(SHA1(password)).send(FS).send(group).send(privileges).send(EOT);
	}

	public synchronized void createGroup(String name, Object privileges) throws IOException {
	// FIXME: what is privileges?
	// send("CREATEGROUP").send(SP).send(name).send(FS).send(privileges).send(EOT);
	}

	public synchronized void declineInvitation(int chatId) throws IOException {
		send("DECLINE").send(SP).send(""+chatId).send(EOT);
	}

	public synchronized void deleteFile(String path) throws IOException {
		send("DELETE").send(SP).send(path).send(EOT);
	}

	public synchronized void deleteUser(String name) throws IOException {
		send("DELETEUSER").send(SP).send(name).send(EOT);
	}

	public synchronized void deleteGroup(String name) throws IOException {
		send("DELETEGROUP").send(SP).send(name).send(EOT);
	}

	public synchronized void editUser(String name, String password, String group, Object privileges) throws IOException {
	// FIXME: what is privileges?
	// send("EDITUSER").send(SP).send(name).send(FS).send(SHA1(password)).send(FS).send(group).send(privileges).send(EOT);
	}

	public synchronized void editGroup(String name, Object privileges) throws IOException {
	// FIXME: what is privileges?
	// send("EDITGROUP").send(SP).send(name).send(FS).send(privileges).send(EOT);
	}

	public synchronized void createFolder(String path) throws IOException {
		send("FOLDER").send(SP).send(path).send(EOT);
	}

	public synchronized void getFile(String path, int offset) throws IOException {
		send("GET").send(SP).send(path).send(FS).send(""+offset).send(EOT);
	}
	
	public synchronized void requestGroupList() throws IOException {
		send("GROUPS").send(EOT);
	}

	public synchronized void sendHello() throws IOException {
		send("HELLO").send(EOT);
	}
	
	public synchronized void sendIcon(int iconId, byte[] image) throws IOException {
		send("ICON").send(SP).send(""+iconId);
		if (image != null) {
			send(FS).send(bytesToBase64(image));
		}
		send(EOT);
	}

	public synchronized void requestUserInfo(int userId) throws IOException {
		send("INFO").send(SP).send(""+userId).send(EOT);
	}
	
	public synchronized void inviteToChat(int userId, int charId) throws IOException {
		send("INVITE").send(SP).send(""+userId).send(FS).send(EOT);
	}

	public synchronized void joinChat(int chatId) throws IOException {
		send("JOIN").send(SP).send(""+chatId).send(EOT);
	}

	public synchronized void kickUser(int userId, String message) throws IOException {
		send("KICK").send(SP).send(""+userId).send(FS).send(message).send(EOT);
	}

	public synchronized void leaveChat(int chatId) throws IOException {
		send("LEAVE").send(SP).send(""+chatId).send(EOT);
	}

	public synchronized void requestFileList(String path) throws IOException {
		send("LIST").send(SP).send(path).send(EOT);
	}
	
	public synchronized void sendEmoteMessage(int chatId, String message) throws IOException {
		send("ME").send(SP).send(""+chatId).send(FS).send(message).send(EOT);
	}

	public synchronized void moveFile(String from, String to) throws IOException {
		send("MOVE").send(SP).send(from).send(FS).send(to).send(EOT);
	}
	
	public synchronized void sendPrivateMessage(int userId, String message) throws IOException {
		send("MSG").send(SP).send(""+userId).send(FS).send(message).send(EOT);
	}

	public synchronized void requestNews() throws IOException {
		send("NEWS").send(EOT);
	}

	public synchronized void sendNick(String nick) throws IOException {
		send("NICK").send(SP).send(nick).send(EOT);
	}

	public synchronized void sendPassword(String password) throws IOException {
		send("PASS").send(SP).send((password != null ? SHA1(password) : "")).send(EOT);
	}

	public synchronized void sendPing() throws IOException {
		send("PING").send(EOT);
	}

	public synchronized void postNews(String message) throws IOException {
		send("POST").send(SP).send(message).send(EOT);
	}

	public synchronized void createPrivateChat() throws IOException {
		send("PRIVCHAT").send(EOT);
	}

	public synchronized void requestPrivilegeMask() throws IOException {
		send("PRIVILEGES").send(EOT);
	}

	public synchronized void putFile(String path, int size, String checksum) throws IOException {
		send("PUT").send(SP).send(path).send(FS).send(""+size).send(FS).send(checksum).send(EOT);
	}

	public synchronized void readUserInfo(String name) throws IOException {
		send("READUSER").send(SP).send(name).send(EOT);
	}

	public synchronized void readGroupInfo(String name) throws IOException {
		send("READGROUP").send(SP).send(name).send(EOT);
	}

	public synchronized void sendChatMessage(int chatId, String message) throws IOException {
		send("SAY").send(SP).send(""+chatId).send(FS).send(message).send(EOT);
	}

	public synchronized void searchFor(String query) throws IOException {
		send("SEARCH").send(SP).send(query).send(EOT);
	}
	
	public synchronized void requestFileInfo(String path) throws IOException {
		send("STAT").send(SP).send(path).send(EOT);
	}
	
	public synchronized void sendStatusMessage(String status) throws IOException {
		send("STATUS").send(SP).send(status).send(EOT);
	}
	
	public synchronized void changeTopic(int chatId, String topic) throws IOException {
		send("TOPIC").send(SP).send(""+chatId).send(FS).send(topic).send(EOT);
	}

	public synchronized void identifyTransfer(String hash) throws IOException {
		send("TRANSFER").send(SP).send(hash).send(EOT);
	}
	
	public synchronized void sendFolderType(String path, Object folderType) {
		// FIXME - folderType
		// send("TYPE").send(SP).send(path).send(folderType).send(EOT);
	}

	public synchronized void sendUsername(String username) throws IOException {
		send("USER").send(SP).send(username).send(EOT);
	}

	public synchronized void listUserAccounts() throws IOException {
		send("USERS").send(EOT);
	}

	public synchronized void requestUserList(int chatId) throws IOException {
		send("WHO").send(SP).send(""+chatId).send(EOT);
	}

	private WiredClient send(String str) throws IOException {
		out.write(str.getBytes("UTF-8"));
		return this;
	}

	private WiredClient send(byte b) throws IOException {
		out.write(b);
		return this;
	}

	private static class ServerMessage {
		int code;
		ArrayList<String> params;
	}

	private ServerMessage readServerMessage() throws IOException {
		byte[] code = new byte[3];
		in.readFully(code);
		ServerMessage msg = new ServerMessage();
		msg.code = Integer.parseInt(new String(code));
		ArrayList<String> params;
		byte b = in.readByte();
		if (b == SP) {
			msg.params = new ArrayList<String>();
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			while (true) {
				b = in.readByte();
				if (b == EOT) {
					if (bytes.size() > 0) {
						msg.params.add(bytes.toString("UTF-8"));
					}
					break;
				} else if (b == FS) {
					if (bytes.size() > 0) {
						msg.params.add(bytes.toString("UTF-8"));
						bytes = new ByteArrayOutputStream();
					}
				} else {
					bytes.write(b);
				}
			}
		}
		return msg;
	}

	private static String SHA1(String text) {
		try {
			MessageDigest d = MessageDigest.getInstance("SHA-1");
			d.update(text.getBytes("iso-8859-1"), 0, text.length());
			return bytesToHex(d.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static final char[] HEX_CHARS = {
		'0', '1', '2', '3', '4', '5', '6', '7',
		'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
	};
	private static String bytesToHex(byte[] bytes) {
		StringBuilder buf = new StringBuilder(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			buf.append(HEX_CHARS[(b & 0xF0) >> 4]);
			buf.append(HEX_CHARS[b & 0x0F]);
		}
		return buf.toString();
	}

	private static final char[] BASE64_CHARS = {
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
		'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
		'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
	};
	private static String bytesToBase64(byte[] bytes) {
		int numFullGroups = bytes.length / 3;
		int numBytesInPartialGroup = bytes.length - 3 * numFullGroups;
		int resultLen = 4*((bytes.length + 2)/3);
		StringBuilder sb = new StringBuilder(resultLen);

		// Translate all full groups from byte array elements to Base64
		int inCursor = 0;
		for (int i = 0; i < numFullGroups; i++) {
			int byte0 = bytes[inCursor++] & 0xff;
			int byte1 = bytes[inCursor++] & 0xff;
			int byte2 = bytes[inCursor++] & 0xff;
			sb.append(BASE64_CHARS[byte0 >> 2]);
			sb.append(BASE64_CHARS[(byte0 << 4)&0x3f | (byte1 >> 4)]);
			sb.append(BASE64_CHARS[(byte1 << 2)&0x3f | (byte2 >> 6)]);
			sb.append(BASE64_CHARS[byte2 & 0x3f]);
		}

		// Translate partial group if present
		if (numBytesInPartialGroup != 0) {
			int byte0 = bytes[inCursor++] & 0xff;
			sb.append(BASE64_CHARS[byte0 >> 2]);
			if (numBytesInPartialGroup == 1) {
				sb.append(BASE64_CHARS[(byte0 << 4) & 0x3f]);
				sb.append("==");
			} else {
				int byte1 = bytes[inCursor++] & 0xff;
				sb.append(BASE64_CHARS[(byte0 << 4)&0x3f | (byte1 >> 4)]);
				sb.append(BASE64_CHARS[(byte1 << 2)&0x3f]);
				sb.append('=');
			}
		}
		return sb.toString();
	}
}
