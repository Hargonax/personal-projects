package es.bifacia.manga.downloader.bean;

public class Chapter implements Comparable<Chapter> {
	private float number;
	private String path;

	public Chapter() {
		super();
	}

	public Chapter(final float number, final String path) {
		this.number = number;
		this.path = path;
	}

	public float getNumber() {
		return number;
	}

	public void setNumber(float number) {
		this.number = number;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int compare(Chapter o1, Chapter o2) {
		if (o1.getNumber() > o2.getNumber()) {
			return -1;
		}
		if (o1.getNumber() < o2.getNumber()) {
			return 1;
		}
		return 0;
	}

	@Override
	public int compareTo(Chapter o) {
		if (this.number > o.getNumber()) {
			return 1;
		}
		if (this.number < o.getNumber()) {
			return -1;
		}
		return 0;
	}

	public String toString() {
		return this.number + " - " + this.path;
	}
}
