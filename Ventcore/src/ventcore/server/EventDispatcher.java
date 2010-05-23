package ventcore.server;

import java.util.*;
import java.util.concurrent.*;

import wired.event.WiredEvent;

public class EventDispatcher {
	private static final EventDispatcher instance = new EventDispatcher();

	private EventDispatcher() {
	}

	private BlockingQueue<WiredEvent> getUserEventQueue(String user) {
		return UserManager.getInstance().getUserData(user).getEventQueue();
	}
	
	public synchronized void dispatch(WiredEvent event, String user) {
		getUserEventQueue(user).add(event);
	}

	public void accumulateEvents(String user, List<WiredEvent> events, long timeoutMillis) {
		BlockingQueue<WiredEvent> queue = getUserEventQueue(user);
		int ndrained = queue.drainTo(events);
		if (ndrained == 0) {
			long tooLate = System.currentTimeMillis() + timeoutMillis;
			while (System.currentTimeMillis() < tooLate) {
				WiredEvent e = null;;
				try {
					e = queue.poll(50, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e1) { }
				if (e != null) {
					events.add(e);
					while (!queue.isEmpty()) {
						events.add(queue.remove());
					}
					break;
				}
			}
		}
	}

	public static EventDispatcher getInstance() {
		return instance;
	}
}
