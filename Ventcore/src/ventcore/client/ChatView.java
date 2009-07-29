package ventcore.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.MyHorizontalSplitPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ChatView extends Composite {
	public ChatView() {
		MyHorizontalSplitPanel split = new MyHorizontalSplitPanel();
		split.setMinLeftWidth(275);
		split.setMinRightWidth(125);
		VerticalPanel vp = new VerticalPanel();
		HTML top = new HTML("");
		top.setStyleName("chat_view");
		vp.add(top);
		HTML space = new HTML("");
		vp.add(space);
		vp.setCellHeight(space, "5px");
		Widget input = createChatInput();
		vp.add(input);
		vp.setCellHeight(input, "25px");
		vp.setSize("100%", "375px");
		split.setLeftWidget(vp);
		HTML users = new HTML();
		users.setStyleName("userlist");
		split.setRightWidget(users);
		split.setSize("100%", "375px");
		split.setSplitPosition("525px");
		initWidget(split);
	}

	private Widget createChatInput() {
		HorizontalPanel panel = new HorizontalPanel();
		TextBox field = new TextBox();
		field.setWidth("100%");
		panel.add(field);
		HTML space = new HTML("&nbsp;");
		panel.add(space);
		panel.setCellWidth(space, "5px");
		Button button = new Button("Send");
		button.setWidth("100%");
		panel.add(button);
		panel.setCellWidth(button, "65px");
		panel.setWidth("100%");
		return panel;
	}
}
