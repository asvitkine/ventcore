package ventcore.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ventcore.client.event.ChatEvent;
import ventcore.client.event.FileListEvent;
import ventcore.client.event.RemoteEvent;
import ventcore.client.event.UserJoinEvent;
import ventcore.client.event.UserLeaveEvent;
import ventcore.client.event.UserListEvent;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class Ventcore implements EntryPoint {
	private static final String userKey = generateRandomString();
	private static final VentcoreServiceAsync ventcoreService = GWT.create(VentcoreService.class);
	private static final EventServiceAsync eventService = GWT.create(EventService.class);
	private static final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
		public void onFailure(Throwable caught) {}
		public void onSuccess(Void result) {}
	};
	private Map<Integer, ChatView> chats;
	private static int lastChatId = 1;

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
		chats = new HashMap<Integer, ChatView>();
		ChatView cv = new ChatView(1);
		chats.put(1, cv);
		RootPanel.get("content").add(cv);
		LoginDialog dialog = new LoginDialog();
		dialog.center();
		dialog.show();
		runTimer();
	}

	private void runTimer() {
		Timer t = new Timer() {
			public void run() {
				Ventcore.getEventService().receiveEvents(Ventcore.getUserKey(), new AsyncCallback<List<RemoteEvent>>() {

					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					public void onSuccess(List<RemoteEvent> result) {
						for (RemoteEvent e : result) {
							if (e instanceof ChatEvent) {
								ChatEvent event = (ChatEvent) e;
								chats.get(event.getChatId()).handleChatEvent(event);
							} else if (e instanceof UserListEvent) {
								UserListEvent event = (UserListEvent) e;
								chats.get(event.getChatId()).setUserList(event.getUsers());
							} else if (e instanceof UserJoinEvent) {
								UserJoinEvent event = (UserJoinEvent) e;
								chats.get(event.getChatId()).handleUserJoin(event.getUser());
							} else if (e instanceof UserLeaveEvent) {
								UserLeaveEvent event = (UserLeaveEvent) e;
								ChatView cv = chats.get(event.getChatId());
								cv.handleUserLeave(cv.getUser(event.getUserId()));
							} else if (e instanceof FileListEvent) {
								Ventcore.handleFileList(((FileListEvent)e).getFiles());
							}
						}
						schedule(1);
					}				
				});
			}
		};
		t.run();
		t.schedule(1);
	}

	private void createNavLinks() {
		createNavLink("nav_chat", "<a href='javascript:;'>Chat</a>").addClickHandler(
			new ClickHandler() {
				public void onClick(ClickEvent event) {
					ChatView cv = chats.get(lastChatId);
					if (cv == null) {
						cv = chats.get(1);
					}
					RootPanel.get("content").clear();
					RootPanel.get("content").add(cv);
				}
			});
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

	public static void setContent(Widget w) {
		RootPanel rp = RootPanel.get("content");
		if (rp.getWidgetCount() > 0) {
			Widget old = rp.getWidget(0);
			if (old instanceof ChatView) {
				lastChatId = ((ChatView)old).getChatId();
			}
			rp.clear();
		}
		rp.add(w);
	}

	public static void handleFileList(List<FileInfo> files) {
		setContent(new FolderView(files));
	}
}
