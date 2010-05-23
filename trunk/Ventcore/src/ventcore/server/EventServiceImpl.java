package ventcore.server;

import java.util.*;

import ventcore.client.EventService;
import wired.event.WiredEvent;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class EventServiceImpl extends RemoteServiceServlet implements EventService {
	
	public synchronized List<WiredEvent> receiveEvents(String user) {
		UserManager.getInstance().getUserData(user);
		List<WiredEvent> events = new ArrayList<WiredEvent>();
		EventDispatcher.getInstance().accumulateEvents(user, events, 100);//50000);
		return events;
	}
}
