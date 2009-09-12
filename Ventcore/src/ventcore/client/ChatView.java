package ventcore.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MyHorizontalSplitPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ChatView extends Composite {
	private FlowPanel messageList;
	private FlowPanel userlist;

	public ChatView() {
		MyHorizontalSplitPanel split = new MyHorizontalSplitPanel();
		split.setMinLeftWidth(275);
		split.setMinRightWidth(125);
		VerticalPanel vp = new VerticalPanel();
		messageList = new FlowPanel();
		messageList.setStyleName("chat_view");
		for (int i = 0; i < 100; i++)
			appendMessage("&nbsp;<<< Fred is now known as Bob >>>");
		vp.add(messageList);
		HTML space = new HTML("");
		vp.add(space);
		vp.setCellHeight(space, "5px");
		Widget input = createChatInput();
		vp.add(input);
		vp.setCellHeight(input, "25px");
		vp.setSize("100%", "375px");
		split.setLeftWidget(vp);
		userlist = new FlowPanel();
		setUserList(createSampleUserList());
		userlist.setStyleName("userlist");
		split.setRightWidget(userlist);
		split.setSize("100%", "375px");
		split.setSplitPosition("525px");
		initWidget(split);
	}

	private User[] createSampleUserList() {
		User[] users = new User[2];
		users[0] = new User();
		users[0].setName("Haravikk");
		users[1] = new User();
		users[1].setName("WarriorMouse");
		users[1].setAdmin(true);
		for (int i = 2; i < users.length; i++) {
			users[i] = new User();
			users[i].setName("WarriorMouse");
			users[i].setAdmin(true);
		}
		return users;
	}
	
	public void appendMessage(String message) {
		HTML msg = new HTML(message);
		msg.setStyleName("info");
		messageList.add(msg);
	}

	public void setUserList(User[] users) {
		userlist.clear();
		for (User user : users) {
			String img = (user.isAdmin() ? "<img src='/images/adminuser.png'/>" : "<img src='/images/reguser.png'/>");
			HTML userHtml = new HTML(img + "<span class='color" + user.getStatus() + "'>" + user.getName() + "</span>");
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
