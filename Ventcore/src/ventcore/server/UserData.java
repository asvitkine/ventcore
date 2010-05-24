package ventcore.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import wired.event.WiredEvent;

public class UserData {
	private BlockingQueue<WiredEvent> eventQueue;
	private VentcoreWiredClient client;
	
	public UserData() {
		eventQueue = new LinkedBlockingQueue<WiredEvent>();
	}

	public BlockingQueue<WiredEvent> getEventQueue() {
		return eventQueue;
	}

	public void setWiredClient(VentcoreWiredClient client) {
		this.client = client;
	}
	
	public VentcoreWiredClient getWiredClient() {
		return client;
	}
}
