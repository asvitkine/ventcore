package ventcore.client;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FolderView extends Composite {
	public FolderView(List<FileInfo> files) {
		FlexTable table = new FlexTable();
		table.setStyleName("list");
	    table.setText(0, 0, "Name");
	    table.setText(0, 1, "Size");
	    table.getRowFormatter().setStyleName(0, "th");
	    for (int i = 0; i < files.size(); i++) {
	    	table.setWidget(i + 1, 0, getNameWidget(files.get(i)));
	    	table.getCellFormatter().setWordWrap(i + 1, 0, false);
	    	table.setText(i + 1, 1, getSizeString(files.get(i)));
	    	table.getCellFormatter().setWordWrap(i + 1, 1, false);
	    }
	    initWidget(table);
	}

	private Widget getNameWidget(final FileInfo file) {
		String img ="";
		if (file.getIcon() != null) {
			img = "<img src='data:image;base64," + file.getIcon() + "' />";
		}
		HorizontalPanel p = new HorizontalPanel();
		p.add(new HTML(img));
		SimpleLink link = new SimpleLink(file.getName());
		link.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (file.isDirectory()) {
					Ventcore.requestFileList(file.getPath());
				}
			}
		});
		p.add(link);
		return p;
	}

	private String getSizeString(FileInfo file) {
		if (file.isDirectory()) {
			return file.getSize() + " items";
		} else {
			long size = file.getSize();
			if (size > 1024*1024*1024) {
				return NumberFormat.getFormat("0.0 GB").format(size / 1024.0*1024*1024);			
			} else if (size > 1024*1024) {
				return NumberFormat.getFormat("0.0 MB").format(size / 1024.0*1024);			
			} else if (size > 1024) {
				return NumberFormat.getFormat("0.0 KB").format(size / 1024.0);			
			} else {
				return size + " bytes";		
			}			
		}
	}
}
