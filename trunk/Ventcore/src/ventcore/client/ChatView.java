package ventcore.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ChatView extends Composite implements ChangeHandler {
	private List<ChatPanel> chats;
	private ListBox chatList;
	private VerticalPanel p;

	public ChatView() {
		chats = new ArrayList<ChatPanel>();
		chatList = new ListBox(false);
		chatList.setWidth("200px");
		chatList.addChangeHandler(this);
		p = new VerticalPanel();
		p.setSize("100%", "100%");
		addChatPanel(new ChatPanel(1, "Main Chat"));
		initWidget(p);
	}

	public void onChange(ChangeEvent event) {
		p.clear();
		p.add(chatList);
		p.setCellHeight(chatList, "25px");
		p.add(chats.get(chatList.getSelectedIndex()));
	}

	public void addChatPanel(ChatPanel cp) {
		chats.add(cp);
		chatList.addItem(cp.getName());
		chatList.setSelectedIndex(chats.size() - 1);
		onChange(null);
	}
	
	public ChatPanel getChatPanel(long chatId) {
		for (ChatPanel cp : chats) {
			if (cp.getChatId() == chatId) {
				return cp;
			}
		}
		return null;
	}
}
