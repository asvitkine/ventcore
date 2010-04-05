package ventcore.client;

import java.util.List;

import ventcore.client.event.*;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
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
	private static final SoundController soundController = new SoundController();
	private static final Sound pmSound = Ventcore.createSound("pm");
	private static final String userKey = generateRandomString();
	private static final VentcoreServiceAsync ventcoreService = GWT.create(VentcoreService.class);
	private static final EventServiceAsync eventService = GWT.create(EventService.class);
	private static final AsyncCallback<Void> callback = new AsyncCallback<Void>() {
		public void onFailure(Throwable caught) {}
		public void onSuccess(Void result) {}
	};
	private static ChatView chatView;

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
		RootPanel.get("content").add(chatView = new ChatView());
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
								chatView.getChatPanel(event.getChatId()).handleChatEvent(event);
							} else if (e instanceof UserListEvent) {
								UserListEvent event = (UserListEvent) e;
								ChatPanel chat = chatView.getChatPanel(event.getChatId());
								if (chat == null) {
									chat = new ChatPanel(event.getChatId(), "Private Chat " + event.getChatId());
									chatView.addChatPanel(chat);
								}
								chat.setUserList(event.getUsers());
							} else if (e instanceof UserJoinEvent) {
								UserJoinEvent event = (UserJoinEvent) e;
								chatView.getChatPanel(event.getChatId()).handleUserJoin(event.getUser());
							} else if (e instanceof UserLeaveEvent) {
								UserLeaveEvent event = (UserLeaveEvent) e;
								ChatPanel chat = chatView.getChatPanel(event.getChatId());
								chat.handleUserLeave(chat.getUser(event.getUserId()));
							} else if (e instanceof FileListEvent) {
								Ventcore.handleFileList(((FileListEvent)e).getFiles());
							} else if (e instanceof InviteEvent) {
								InviteEvent event = (InviteEvent) e;
								User user = chatView.getChatPanel(1).getUser(event.getUserId());
								InviteReceivedDialog dialog =
									new InviteReceivedDialog(user, event.getChatId());
								dialog.center();
								dialog.show();
							} else if (e instanceof PrivateMessageEvent) {
								pmSound.play();
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
					setContent(chatView);
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
	
	public static void sendPrivateMessage(int userId, String message) {
		ventcoreService.sendPrivateMessage(userKey, userId, message, callback);
	}

	public static void requestFileList(String path) {
		ventcoreService.requestFileList(userKey, path, callback);		
	}
	
	public static void login(LoginInfo loginInfo) {
		ventcoreService.login(userKey, loginInfo, callback);		
	}

	public static void joinChat(int chatId) {
		ventcoreService.joinChat(userKey, chatId, callback);
		ventcoreService.requestUserList(userKey, chatId, callback);
	}
	
	public static void declineInvitation(int chatId) {
		ventcoreService.declineInvitation(userKey, chatId, callback);
	}
	
	public static void setContent(Widget w) {
		RootPanel rp = RootPanel.get("content");
		rp.clear();
		rp.add(w);
	}

	public static void handleFileList(List<FileInfo> files) {
		setContent(new FolderView(files));
	}
	
	public static Sound createSound(String name) {
		return soundController.createSound(Sound.MIME_TYPE_AUDIO_X_WAV, "sounds/" + name + ".wav");
	}
}
