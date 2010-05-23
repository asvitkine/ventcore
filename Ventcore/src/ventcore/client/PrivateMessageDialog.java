package ventcore.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PrivateMessageDialog extends DialogBox {
	private TextArea message;

	public PrivateMessageDialog(final long toUserId) {
		setText("Private Message");
		setAnimationEnabled(true);
		VerticalPanel p = new VerticalPanel();
		p.add(message = new TextArea());
		HorizontalPanel panel = new HorizontalPanel();
		Button sendButton = new Button("Send");
		Button cancelButton = new Button("Cancel");
		panel.add(cancelButton);
		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		panel.add(sendButton);
		sendButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Ventcore.sendPrivateMessage(toUserId, message.getText());
				hide();
			}
		});
		p.add(panel);
		p.setCellHorizontalAlignment(panel, VerticalPanel.ALIGN_RIGHT);
		setWidget(p);
	}

	public void show() {
		super.show();
		message.setFocus(true);
	}
}
