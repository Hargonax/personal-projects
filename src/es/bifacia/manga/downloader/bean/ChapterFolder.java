package es.bifacia.manga.downloader.bean;

public class ChapterFolder {
	private String name;
	private String path;
	private int numberOfFiles;

	public ChapterFolder() {
		super();
	}

	public ChapterFolder(final String name, final String path, final int numberOfFiles) {
		this.name = name;
		this.path = path;
		this.numberOfFiles = numberOfFiles;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getNumberOfFiles() {
		return numberOfFiles;
	}

	public void setNumberOfFiles(int numberOfFiles) {
		this.numberOfFiles = numberOfFiles;
	}

}
