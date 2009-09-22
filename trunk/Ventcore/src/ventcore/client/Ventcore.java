package ventcore.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class Ventcore implements EntryPoint {
	private static final String userKey = generateRandomString();
	private static final VentcoreServiceAsync chatService = GWT.create(VentcoreService.class);
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
		chatService.login(null, new AsyncCallback<User[]>() {

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			public void onSuccess(User[] result) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	private void createNavLinks() {
		RootPanel.get("nav_chat").add(new HTML("<a href='javascript:;'>Chat</a>"));
		RootPanel.get("nav_private").add(new HTML("<a href='javascript:;'>Private</a>"));
		RootPanel.get("nav_files").add(new HTML("<a href='javascript:;'>Files</a>"));
		RootPanel.get("nav_news").add(new HTML("<a href='javascript:;'>News</a>"));
		RootPanel.get("nav_connection").add(new HTML("<a href='javascript:;'>Disconnect</a>"));
	}

	public static String getUserKey() {
		return userKey;
	}
	
	public static VentcoreServiceAsync getChatService() {
		return chatService;
	}

	public static EventServiceAsync getEventService() {
		return eventService;
	}

	public static void sendChatMessage(int chatId, String message) {
		chatService.sendChatMessage(userKey, chatId, message, callback);
	}

	public static void sendEmoteMessage(int chatId, String message) {
		chatService.sendEmoteMessage(userKey, chatId, message, callback);		
	}
}
