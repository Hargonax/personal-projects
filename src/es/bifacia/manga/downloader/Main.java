package es.bifacia.manga.downloader;

import java.io.InputStream;
import java.util.Properties;

import es.bifacia.manga.downloader.Exception.LogException;
import es.bifacia.manga.downloader.manager.MainManager;
import es.bifacia.manga.downloader.utils.ExceptionUtils;

public class Main {
	private static final String PROPERTIES_FILE_NAME = "manga.properties";
	private static final String MANGA_TITLE_PROPERTY = "manga_title";
	private static final String WEB_PAGE_NAME_PROPERTY = "web_page";
	private static final String WORKING_DIRECTORY_PROPERTY = "working_directory";
	private static final String IMAGES_EXTENSION_PROPERTY = "images_extension";

	public static void main(String[] args) {
		try {
			System.out.println("Comienza la descarga.");
			final MainManager mainManager = new MainManager();
			final Properties properties = new Properties();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			try (final InputStream inputStream = loader.getResourceAsStream(PROPERTIES_FILE_NAME)) {
				properties.load(inputStream);
			}
			final String mangaTitle = properties.getProperty(MANGA_TITLE_PROPERTY);
			if (mangaTitle == null || mangaTitle.isEmpty()) {
				throw new LogException(
						"No se ha indicado en el fichero de propiedades el nombre del manga a descargar.");
			}
			final String webPageName = properties.getProperty(WEB_PAGE_NAME_PROPERTY);
			if (webPageName == null || webPageName.isEmpty()) {
				throw new LogException(
						"No se ha indicado en el fichero de propiedades el nombre de la página de la que se quiere descargar el manga.");
			}
			final String workingDirectory = properties.getProperty(WORKING_DIRECTORY_PROPERTY);
			if (workingDirectory == null || workingDirectory.isEmpty()) {
				throw new LogException(
						"No se ha indicado en el fichero de propiedades el directorio donde se quiere descargar el manga.");
			}
			final String imagesExtension = properties.getProperty(IMAGES_EXTENSION_PROPERTY);
			if (imagesExtension == null || imagesExtension.isEmpty()) {
				throw new LogException(
						"No se ha indicado en el fichero de propiedades la extensión de las imágenes que se va a descargar.");
			}
			mainManager.downloadManga(webPageName, mangaTitle, workingDirectory, imagesExtension);
			System.out.println("Finalizó la descarga con éxito.");
		} catch (Exception ex) {
			System.out.println(
					"Ha fallado el proceso de descarga del manga. \n" + ExceptionUtils.getExceptionMessage(ex));
		}
	}

}
