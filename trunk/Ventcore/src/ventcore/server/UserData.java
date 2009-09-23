package ventcore.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import ventcore.client.event.*;

public class UserData {
	private BlockingQueue<RemoteEvent> eventQueue;
	private WiredClient client;
	
	public UserData() {
		eventQueue = new LinkedBlockingQueue<RemoteEvent>();
	}

	public BlockingQueue<RemoteEvent> getEventQueue() {
		return eventQueue;
	}

	public void setWiredClient(WiredClient client) {
		this.client = client;
	}
	
	public WiredClient getWiredClient() {
		return client;
	}
}
