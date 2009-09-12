package ventcore.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class Ventcore implements EntryPoint {
	private static final ChatServiceAsync chatService = GWT.create(ChatService.class);

	public void onModuleLoad() {
		Window.enableScrolling(false);
		createNavLinks();
		RootPanel.get("content").add(new ChatView());
	}

	private void createNavLinks() {
		RootPanel.get("nav_chat").add(new HTML("<a href='javascript:;'>Chat</a>"));
		RootPanel.get("nav_private").add(new HTML("<a href='javascript:;'>Private</a>"));
		RootPanel.get("nav_files").add(new HTML("<a href='javascript:;'>Files</a>"));
		RootPanel.get("nav_news").add(new HTML("<a href='javascript:;'>News</a>"));
		RootPanel.get("nav_connection").add(new HTML("<a href='javascript:;'>Disconnect</a>"));
	}

	public static ChatServiceAsync getChatService() {
		return chatService;
	}
}
