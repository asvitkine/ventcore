package ventcore.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface ChatServiceAsync {
	void login(LoginInfo login, AsyncCallback<User[]> async);
}
