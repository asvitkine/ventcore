package ventcore.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTML;

/**
 * SimpleLink is an HTML link Widget.
 *
 */
public class SimpleLink extends HTML {
	public SimpleLink(String text) {
		super("<a href='javascript:;'>" + escapeHtml(text) + "</a>");
	}

	private static String escapeHtml(String maybeHtml) {
		Element div = DOM.createDiv();
		DOM.setInnerText(div, maybeHtml);
		return DOM.getInnerHTML(div);
	}
}
