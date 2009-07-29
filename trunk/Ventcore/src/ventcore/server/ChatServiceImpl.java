package ventcore.server;

import ventcore.client.ChatService;
import ventcore.client.LoginInfo;
import ventcore.client.User;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ChatServiceImpl extends RemoteServiceServlet implements ChatService {

	public User[] login(LoginInfo login) {
		// TODO Auto-generated method stub
		return null;
	}
}
