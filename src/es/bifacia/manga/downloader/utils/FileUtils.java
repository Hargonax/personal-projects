package es.bifacia.manga.downloader.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import es.bifacia.manga.downloader.Exception.LogException;

public abstract class FileUtils {

	/**
	 * Añade al path base otro elemento para generar un nuevo path.
	 * 
	 * @param basePath   Path base.
	 * @param newElement Elemento a añadir al path.
	 * @return
	 */
	public static final String addElementsToPath(final String basePath, final String... elements) {
		String newPath = basePath;
		if (elements != null && elements.length > 0) {
			for (String element : elements) {
				if (element != null && !element.isEmpty()) {
					if (element.startsWith("/") || element.startsWith("\\")) {
						element = element.substring(1, element.length());
					}
					if (newPath.endsWith("/") || newPath.endsWith("\\")) {
						newPath += element;
					} else {
						newPath = newPath + File.separator + element;
					}
				}
			}
		}
		return newPath;
	}

	/**
	 * Descarga una imagen de la URL indicada a la ubicación indicada.
	 * 
	 * @param imageURL             URL de la imagen a descargar.
	 * @param imageDestinationPath Path de destino de la imagen.
	 * @throws LogException
	 */
	public static final void downloadImage(final String imageURL, final String imageDestinationPath)
			throws LogException {
		try {
			byte[] content = null;
			final URL url = new URL(imageURL);
			final HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.addRequestProperty("User-Agent", "Mozilla/4.0");
			try (final InputStream in = httpConnection.getInputStream()) {
//			try (final InputStream in = new BufferedInputStream(url.openStream())) {
				try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
					byte[] buf = new byte[1024];
					int n = 0;
					while (-1 != (n = in.read(buf))) {
						out.write(buf, 0, n);
					}
					content = out.toByteArray();
				}
			}
			writeByteArrayToFile(content, imageDestinationPath);
		} catch (Exception ex) {
			throw new LogException("Se ha producido un error al intentar descargar la imagen '" + imageURL + "'.", ex);
		}
	}

	/**
	 * Crea un fichero a partir de un array de bytes.
	 * 
	 * @param bytes               Bytes a partir de los cuales se quiere crear el
	 *                            fichero.
	 * @param destinationFilePath Path del fichero a crear.
	 * @throws LogException
	 */
	public static void writeByteArrayToFile(byte[] bytes, final String destinationFilePath) throws LogException {
		try (final OutputStream os = new FileOutputStream(destinationFilePath)) {
			// Starts writing the bytes in it
			os.write(bytes);
		} catch (Exception ex) {
			throw new LogException(
					"Se ha producido un error al intentar crear un fichero a partir de un array de bytes.", ex);
		}
	}

	/**
	 * Crea el directorio indicado en caso de no existir.
	 * 
	 * @param path Path del directorio a crear.
	 */
	public static void createDirectoryIfNeccessary(final String path) {
		final File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * Intenta recuperar la extensión de una imagen a partir de su URL.
	 * 
	 * @param imageURL URL de la imagen de la que queremos la extensión.
	 * @return Extensión de una imagen a partir de su URL.
	 */
	public static String getImageExtensionFromURL(final String imageURL) {
		String extension = null;
		if (imageURL.contains("jpg") || imageURL.contains("jpeg")) {
			extension = "jpg";
		} else if (imageURL.contains("png")) {
			extension = "png";
		}
		return extension;
	}

}
