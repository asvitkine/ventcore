package ventcore.server;

import java.util.*;

import ventcore.client.EventService;
import ventcore.client.event.RemoteEvent;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class EventServiceImpl extends RemoteServiceServlet implements EventService {
	
	public synchronized List<RemoteEvent> receiveEvents(String user) {
		UserManager.getInstance().getUserData(user);
		List<RemoteEvent> events = new ArrayList<RemoteEvent>();
		EventDispatcher.getInstance().accumulateEvents(user, events, 100);//50000);
		return events;
	}
}
