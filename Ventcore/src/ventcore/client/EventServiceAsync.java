package ventcore.client;

import java.util.List;

import wired.event.WiredEvent;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EventServiceAsync {
	public void receiveEvents(String user, AsyncCallback<List<WiredEvent>> async);
}
