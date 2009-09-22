package ventcore.server;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
	private static UserManager instance = new UserManager();
	private Map<String, UserData> users;

	private UserManager() {
		users = new HashMap<String, UserData>();
	}

	public static UserManager getInstance() {
		return instance;
	}

	public UserData getUserData(String user) {
		UserData d;
		synchronized (users) {
			d = users.get(user);
			if (d == null) {
				d = new UserData(user);
				users.put(user, d);
			}
		}
		return d;
	}
}
