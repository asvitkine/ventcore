package ventcore.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class Ventcore implements EntryPoint {
	private static final String userKey = generateRandomString();
	private static final VentcoreServiceAsync ventcoreService = GWT.create(VentcoreService.class);
	private static final EventServiceAsync eventService = GWT.create(EventService.class);
	private static final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
		public void onFailure(Throwable caught) {}
		public void onSuccess(Void result) {}
	};

	private static String generateRandomString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 4; i++)
			sb.append(Integer.toString(Math.abs(Random.nextInt()), 36));
		return sb.toString();
	}
	
	public void onModuleLoad() {
		Window.enableScrolling(false);
		Keyboard.init();
		createNavLinks();
		RootPanel.get("content").add(new ChatView());
		LoginDialog dialog = new LoginDialog();
		dialog.center();
		dialog.show();
	}

	private void createNavLinks() {
		createNavLink("nav_chat", "<a href='javascript:;'>Chat</a>");
		createNavLink("nav_private", "<a href='javascript:;'>Private</a>");
		createNavLink("nav_files", "<a href='javascript:;'>Files</a>").addClickHandler(
			new ClickHandler() {
				public void onClick(ClickEvent event) {
					Ventcore.requestFileList("/");
				}
			});
		createNavLink("nav_news", "<a href='javascript:;'>News</a>");
		createNavLink("nav_connection", "<a href='javascript:;'>Disconnect</a>");
	}
	
	private HTML createNavLink(String name, String content) {
		HTML link = new HTML(content);
		RootPanel.get(name).add(link);
		return link;
	}

	public static String getUserKey() {
		return userKey;
	}
	
	public static VentcoreServiceAsync getVentcoreService() {
		return ventcoreService;
	}

	public static EventServiceAsync getEventService() {
		return eventService;
	}

	public static void sendChatMessage(int chatId, String message) {
		ventcoreService.sendChatMessage(userKey, chatId, message, callback);
	}

	public static void sendEmoteMessage(int chatId, String message) {
		ventcoreService.sendEmoteMessage(userKey, chatId, message, callback);		
	}

	public static void requestFileList(String path) {
		ventcoreService.requestFileList(userKey, path, callback);		
	}
	
	public static void login(LoginInfo loginInfo) {
		ventcoreService.login(userKey, loginInfo, callback);		
	}
}
