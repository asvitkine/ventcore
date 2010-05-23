package ventcore.client;

import wired.event.User;

import com.allen_sauer.gwt.voices.client.Sound;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class InviteReceivedDialog extends DialogBox {
	private static final Sound inviteSound = Ventcore.createSound("invite");

	public InviteReceivedDialog(User user, final long chatId) {
		setText("Private Chat Invitation");
		setAnimationEnabled(true);
		VerticalPanel p = new VerticalPanel();
		Label label = new Label(user.getNick() + " has invited you to a private chat.\n"
			+ "Do you wish to accept the invitation?");
		label.setHorizontalAlignment(Label.ALIGN_RIGHT);
		HorizontalPanel panel = new HorizontalPanel();
		Button accept = new Button("Accept");
		accept.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Ventcore.joinChat(chatId);
				hide();
			}
		});
		Button reject = new Button("Reject");
		reject.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Ventcore.declineInvitation(chatId);
				hide();
			}
		});
		panel.add(accept);
		panel.add(reject);
		p.add(label);
		p.add(panel);
		setWidget(p);
	}

	@Override
	public void show() {
		super.show();
		inviteSound.play();
	}
}
