package ventcore.client;

import java.util.List;

import ventcore.client.event.RemoteEvent;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("events")
public interface EventService extends RemoteService {
	public List<RemoteEvent> receiveEvents(String user);
}
