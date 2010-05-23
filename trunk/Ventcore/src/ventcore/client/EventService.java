package ventcore.client;

import java.util.List;

import wired.event.WiredEvent;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("events")
public interface EventService extends RemoteService {
	public List<WiredEvent> receiveEvents(String user);
}
