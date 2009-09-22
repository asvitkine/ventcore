package ventcore.client;

import java.util.List;

import ventcore.client.event.RemoteEvent;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EventServiceAsync {
	public void receiveEvents(String user, AsyncCallback<List<RemoteEvent>> async);
}
