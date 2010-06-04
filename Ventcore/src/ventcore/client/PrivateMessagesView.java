package ventcore.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.MyVerticalSplitPanel;
import com.google.gwt.user.client.ui.TextArea;

public class PrivateMessagesView extends Composite {
	private MyVerticalSplitPanel split;
	private List<PrivateMessage> privateMessages;

	public PrivateMessagesView() {
		privateMessages = new ArrayList<PrivateMessage>();
		FlexTable table = createTopTable();
		TextArea textArea = new TextArea();
		textArea.setReadOnly(true);
		textArea.setSize("100%", "100%");
		split = new MyVerticalSplitPanel();
	    split.setTopWidget(table);
		split.setBottomWidget(textArea);
		split.setSplitPosition("200px");
		split.setSize("100%", "100%");
		initWidget(split);
	}

	public void addPrivateMessage(PrivateMessage pm) {
		privateMessages.add(pm);
	    split.setTopWidget(createTopTable());
	}

	private FlexTable createTopTable() {
		FlexTable table = new FlexTable();
		table.setStyleName("list");
	    table.setText(0, 0, "Message");
	    table.setText(0, 1, "From");
	    table.setText(0, 2, "Time");
	    table.getRowFormatter().setStyleName(0, "th");
	    for (int i = 0; i < privateMessages.size(); i++) {
	    	PrivateMessage pm = privateMessages.get(i);
	    	table.setText(i + 1, 0, pm.getMessage());
	    	table.getCellFormatter().setWordWrap(i + 1, 0, false);
	    	table.setText(i + 1, 1, pm.getFromUser().getNick());
	    	table.getCellFormatter().setWordWrap(i + 1, 1, false);
	    	table.setText(i + 1, 2, pm.getTimeReceived().toString());
	    	table.getCellFormatter().setWordWrap(i + 1, 1, false);
	    }
	    return table;
	}
	
}
