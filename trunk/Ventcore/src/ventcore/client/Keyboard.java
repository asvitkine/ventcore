package ventcore.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;

public abstract class Keyboard {
	private static final class WindowCloseHandlerImpl implements ClosingHandler {
		public native void onWindowClosing(ClosingEvent event)
		/*-{
			$doc.onkeydown = null;
			$doc.onkeyup = null;
		}-*/;

		private native void init()
		/*-{
			$doc.onkeydown = function(evt) {
				@ventcore.client.Keyboard::onKeyDown(Lcom/google/gwt/user/client/Event;)(evt || $wnd.event);
			}
    		$doc.onkeyup = function(evt) {
    			@ventcore.client.Keyboard::onKeyUp(Lcom/google/gwt/user/client/Event;)(evt || $wnd.event);
    		}
    	}-*/;
	}

	// Forces installation of event handling hooks.
	public static void init() {
		WindowCloseHandlerImpl handler = new WindowCloseHandlerImpl();
		Window.addWindowClosingHandler(handler);
		handler.init();
	};

	private static boolean altDown;
	
	@SuppressWarnings("unused")
	private static void onKeyDown(Event event) {
		if (DOM.eventGetAltKey(event)) {
			altDown = true;
		}
	}

	@SuppressWarnings("unused")
	private static void onKeyUp(Event event) {
		if (DOM.eventGetAltKey(event)) {
			altDown = false;
		}
	}

	private Keyboard() { }

	public static boolean isAltDown() {
		return altDown;
	}
}
