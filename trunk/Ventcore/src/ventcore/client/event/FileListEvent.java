package ventcore.client.event;

import java.util.List;

import ventcore.client.FileInfo;

public class FileListEvent extends RemoteEvent {
	private List<FileInfo> files;

	public List<FileInfo> getFiles() {
		return files;
	}

	public void setFiles(List<FileInfo> files) {
		this.files = files;
	}
}
