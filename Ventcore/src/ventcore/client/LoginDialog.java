package ventcore.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoginDialog extends DialogBox {
	private static final String NICK_COOKIE = "VentcoreNick";
	private static final String USERNAME_COOKIE = "VentcoreUsername";
	
	private TextBox nick;
	private TextBox username;
	private TextBox password;

	public LoginDialog() {
		setText("Ventcore Login");
		setAnimationEnabled(true);
		VerticalPanel p = new VerticalPanel();
		addField(p, "Nick:", nick = createTextBox(getDefaultNick()));
		addField(p, "Login:", username = createTextBox(getDefaultUsername()));
		addField(p, "Password:", password = createTextBox(getDefaultPassword()));
		Button loginButton = new Button("Login");
		p.add(loginButton);
		p.setCellHorizontalAlignment(loginButton, VerticalPanel.ALIGN_RIGHT);
		loginButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				LoginInfo info = getLoginInfo();
				Cookies.setCookie(NICK_COOKIE, info.getNick());
				Cookies.setCookie(USERNAME_COOKIE, info.getUsername());
				// FIXME: handle failures
				Ventcore.login(getLoginInfo());
				LoginDialog.this.hide();
			}
		});
		setWidget(p);
	}

	private String getDefaultNick() {
		String nick = Cookies.getCookie(NICK_COOKIE);
		return (nick != null ? nick : "Ventcore User");
	}

	private String getDefaultUsername() {
		String username = Cookies.getCookie(USERNAME_COOKIE);
		return (username != null ? username : "guest");
	}

	private String getDefaultPassword() {
		return "";
	}
	
	private TextBox createTextBox(String text) {
		TextBox box = new TextBox();
		box.setText(text);
		return box;
	}
	
	private void addField(VerticalPanel vp, String name, Widget w) {
		HorizontalPanel panel = new HorizontalPanel();
		Label label = new Label(name);
		label.setHorizontalAlignment(Label.ALIGN_RIGHT);
		panel.add(label);
		panel.setCellWidth(label, "75px");
		panel.setCellVerticalAlignment(label, HorizontalPanel.ALIGN_MIDDLE);
		panel.add(w);
		panel.setCellVerticalAlignment(w, HorizontalPanel.ALIGN_MIDDLE);
		panel.setCellWidth(w, "125px");
		panel.setWidth("100%");
		panel.setHeight("24px");
		vp.add(panel);
	}

	private LoginInfo getLoginInfo() {
		LoginInfo info = new LoginInfo();
		info.setNick(nick.getText());
		info.setUsername(username.getText());
		info.setPassword(password.getText());
		return info;
	}
}
