package ventcore.client;

import java.io.Serializable;

public class FileInfo implements Serializable {
	public static final int TYPE_FILE = 0;
	public static final int TYPE_FOLDER = 1;
	public static final int TYPE_UPLOADS_FOLDER = 2;
	public static final int TYPE_DROPBOX_FOLDER = 3;

	private String path;
	private int type;
	private long size;
	private long creationDate;
	private long modificationDate;

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public long getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
	public long getModificationDate() {
		return modificationDate;
	}
	public void setModificationDate(long modificationDate) {
		this.modificationDate = modificationDate;
	}
}
