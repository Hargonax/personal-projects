package es.bifacia.manga.downloader.utils;

public abstract class WebUtils {

	/**
	 * Se encarga de añadir un path a una URL asegurándose que no se duplican las
	 * barras.
	 * 
	 * @param url  URL a la que queremos añadir el path.
	 * @param path Path a añadir a la URL.
	 * @return Nueva URL para los parámetros indicados.
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
