package ventcore.client;

import java.util.ArrayList;
import java.util.List;

import ventcore.client.event.ChatEvent;
import ventcore.client.event.RemoteEvent;
import ventcore.client.event.UserJoinEvent;
import ventcore.client.event.UserLeaveEvent;
import ventcore.client.event.UserListEvent;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class ChatView extends Composite {
	private ScrollPanel messageScrollPanel;
	private FlowPanel messageList;
	private FlowPanel userlist;
	private List<User> users;

	public ChatView() {
		users = new ArrayList<User>();
		MyHorizontalSplitPanel split = new MyHorizontalSplitPanel();
		split.setMinLeftWidth(275);
		split.setMinRightWidth(125);
		VerticalPanel vp = new VerticalPanel();
		messageList = new FlowPanel();
		messageList.setStyleName("chat_view");
		messageScrollPanel = new ScrollPanel(messageList);
		messageScrollPanel.setStyleName("chat_scroll_view");
		vp.add(messageScrollPanel);
		HTML space = new HTML("");
		vp.add(space);
		vp.setCellHeight(space, "5px");
		Widget input = createChatInput();
		vp.add(input);
		vp.setCellHeight(input, "25px");
		vp.setSize("100%", "375px");
		split.setLeftWidget(vp);
		userlist = new FlowPanel();
		userlist.setStyleName("userlist");
		split.setRightWidget(userlist);
		split.setSize("100%", "375px");
		split.setSplitPosition("525px");
		initWidget(split);
		Timer t = new Timer() {
			public void run() {
				Ventcore.getEventService().receiveEvents(Ventcore.getUserKey(), new AsyncCallback<List<RemoteEvent>>() {

					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					public void onSuccess(List<RemoteEvent> result) {
						for (RemoteEvent e : result) {
							if (e instanceof ChatEvent) {
								handleChatEvent((ChatEvent) e);
							} else if (e instanceof UserListEvent) {
								setUserList(((UserListEvent) e).getUsers());
							} else if (e instanceof UserJoinEvent) {
								// TODO chatid
								handleUserJoin(((UserJoinEvent)e).getUser());
							} else if (e instanceof UserLeaveEvent) {
								// TODO chatid
								handleUserLeave(getUser(((UserLeaveEvent)e).getUserId()));
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


	private void handleUserLeave(User user) {
		if (user != null) {
			users.remove(user);
			setUserList(users);
			appendMessage("<font color='red'>&lt;&lt;&lt; " +
				user.getNick() + " has left &gt;&gt;&gt;</font>");
		}
	}

	private void handleUserJoin(User user) {
		users.add(user);
		setUserList(users);
		appendMessage("<font color='red'>&lt;&lt;&lt; " +
			user.getNick() + " has joined &gt;&gt;&gt;</font>");
	}	
	
	public User getUser(int userId) {
		for (User user : users)
			if (user.getId() == userId)
				return user;
		return null;
	}
	
	public void handleChatEvent(ChatEvent event) {
		User user = getUser(event.getUserId());
		if (user != null) {
			if (event.isEmote()) {
				appendMessage("*** " + user.getNick() + " " + event.getMessage());
			} else {
				appendMessage(user.getNick() + " : " + event.getMessage());				
			}
			messageScrollPanel.scrollToBottom();
		}
	}

	public void appendMessage(String message) {
		HTML msg = new HTML("&nbsp;&nbsp;" + message);
		msg.setStyleName("info");
		messageList.add(msg);
	}

	public void setUserList(List<User> users) {
		this.users = users;
		userlist.clear();
		for (User user : users) {
			String img = (user.isAdmin() ? "<img src='/images/adminuser.png'/>" : "<img src='/images/reguser.png'/>");
			HTML userHtml = new HTML(img + "<span class='color" + user.getStatus() + "'>" + user.getNick() + "</span>");
			userHtml.setStyleName("user");
			userlist.add(userHtml);
		}
	}

	private Widget createChatInput() {
		HorizontalPanel panel = new HorizontalPanel();
		final TextBox field = new TextBox();
		field.setWidth("100%");
		panel.add(field);
		HTML space = new HTML("&nbsp;");
		panel.add(space);
		panel.setCellWidth(space, "5px");
		final Button button = new Button("Send");
		button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (Keyboard.isAltDown())
					Ventcore.sendEmoteMessage(1, field.getText());
				else
					Ventcore.sendChatMessage(1, field.getText());
				field.setText("");
			}
		});
		button.setWidth("100%");
		panel.add(button);
		panel.setCellWidth(button, "65px");
		panel.setWidth("100%");
		field.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == '\r') {
					button.click();
				}
			}
		});
		return panel;
	}
}
