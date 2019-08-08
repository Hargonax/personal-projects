package es.bifacia.manga.downloader.utils;

public abstract class WebUtils {

	/**
	 * Se encarga de a�adir un path a una URL asegur�ndose que no se duplican las
	 * barras.
	 * 
	 * @param url  URL a la que queremos a�adir el path.
	 * @param path Path a a�adir a la URL.
	 * @return Nueva URL para los par�metros indicados.
	 */
	public static final String addPathToURL(final String url, String path) {
		String newURL = url;
		if (path != null && !path.trim().isEmpty()) {
			path = path.trim();
			if (path.startsWith("/")) {
				path = path.substring(1, path.length());
			}
			if (newURL.endsWith("/")) {
				newURL += path;
			} else {
				newURL = newURL + "/" + path;
			}
		}
		return newURL;
	}

}
