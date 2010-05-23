package ventcore.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import wired.WiredClient;
import wired.event.WiredEvent;

public class UserData {
	private BlockingQueue<WiredEvent> eventQueue;
	private WiredClient client;
	
	public UserData() {
		eventQueue = new LinkedBlockingQueue<WiredEvent>();
	}

	public BlockingQueue<WiredEvent> getEventQueue() {
		return eventQueue;
	}

	public void setWiredClient(WiredClient client) {
		this.client = client;
	}
	
	public WiredClient getWiredClient() {
		return client;
	}
}
