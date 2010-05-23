package ventcore.client;

import java.util.Date;
import java.util.List;

import wired.event.NewsPost;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;

public class NewsView extends Composite {
	private TextArea news;

	public NewsView(List<NewsPost> newsList) {
		news = new TextArea();
		StringBuilder sb = new StringBuilder();
		for (NewsPost post : newsList) {
			if (post != newsList.get(0)) {
				sb.append("_________________________________________________\n");
			}
			sb.append("From ");
			sb.append(post.getNick());
			sb.append(" (");
			sb.append(new Date(post.getPostTime()).toString());
			sb.append("):\n\n");
			sb.append(post.getPost().replaceAll("\r", "\n"));
			sb.append("\n");
			//sb.append(String.format("From %s (%s):\r\r%s\r",
			//	post.getNick(), new Date(post.getPostTime()).toString(), post.getPost()));
		}
		news.setText(sb.toString());
		news.setSize("100%", "100%");
	    initWidget(news);
	}

}
