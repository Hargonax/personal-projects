package es.bifacia.manga.downloader.manager;

import java.io.File;
import java.util.List;

import es.bifacia.manga.downloader.Exception.LogException;
import es.bifacia.manga.downloader.bean.Chapter;
import es.bifacia.manga.downloader.interfaz.IWebPage;
import es.bifacia.manga.downloader.utils.FileUtils;
import es.bifacia.manga.downloader.web.page.manager.MangaHereManager;
import es.bifacia.manga.downloader.web.page.manager.MangaKakalotManager;

public class MainManager {
	private static final String MANGA_HERE = "MangaHere";
	private static final String MANGA_KAKALOT = "MangaKakalot";

	public MainManager() {
		super();
	}

	/**
	 * Descarga el manga en la ubicaci�n indicada.
	 * 
	 * @param webPageName      Nombre de la p�gina web de la que queremos descargar
	 *                         el manga.
	 * @param mangaTitle       T�tulo del manga.
	 * @param workingDirectory Directorio donde se va a descargar el manga.
	 * @throws LogException
	 */
	public void downloadManga(final String webPageName, final String mangaTitle, final String workingDirectory,
			final String imagesExtension) throws LogException {
		FileUtils.createDirectoryIfNeccessary(workingDirectory);
		final IWebPage webPageManager = this.getWebPageInterface(webPageName);
		final String mangaURL = webPageManager.getMangaURL(mangaTitle);
		System.out.println("Obtenemos la informaci�n de los cap�tulos del manga " + mangaTitle + ".");
		final List<Chapter> chapters = webPageManager.getMangaChapters(mangaURL);
		if (chapters == null || chapters.isEmpty()) {
			throw new LogException("No se han obtenido cap�tulos para el manga.");
		}
		System.out.println("Se han encontrado " + chapters.size() + " cap�tulos.");
		int i = 1;
		for (final Chapter chapter : chapters) {
			final List<String> chapterPagesURLs = webPageManager.getPagesURLs(chapter.getPath());
			if (chapterPagesURLs == null || chapterPagesURLs.isEmpty()) {
				throw new LogException("No se han recuperado p�ginas para el cap�tulo.");
			}
			this.downloadChapterPages(chapterPagesURLs, chapter.getNumber(), workingDirectory, imagesExtension);
			System.out.println("Descargado cap�tulo " + i + ".");
			i++;
		}
	}

	/**
	 * Descarga las im�genes de un cap�tulo.
	 * 
	 * @param pagesURLs        Listado de las URLs de las p�ginas del cap�tulo.
	 * @param chapterNumber    N�mero de cap�tulo que se est� descargando.
	 * @param workingDirectory Directorio de descarga.
	 * @throws LogException
	 */
	private void downloadChapterPages(final List<String> pagesURLs, final float chapterNumber,
			final String workingDirectory, final String imagesExtension) throws LogException {
		final String chapterFolder = this.getChapterFolderName(chapterNumber, workingDirectory);
		FileUtils.createDirectoryIfNeccessary(chapterFolder);
		int pageNumber = 1;
		for (final String pageURL : pagesURLs) {
			try {
				String pagePath = FileUtils.addElementsToPath(chapterFolder,
						this.getPageName(chapterNumber, pageNumber));
				String extension = FileUtils.getImageExtensionFromURL(pageURL);
				if (extension == null) {
					extension = imagesExtension;
				}
				pagePath += "." + extension;
				FileUtils.downloadImage(pageURL, pagePath);
				pageNumber++;
			} catch (Exception ex) {
				throw new LogException("Se ha producido un error al descargar la p�gina " + pageNumber
						+ " del cap�tulo " + chapterNumber + ".");
			}

		}
	}

	/**
	 * Obtiene el interfaz de trabajo correspondiente a la p�gina indicada.
	 * 
	 * @param webPageName Nombre de la p�gina web de la que se quiere descargar el
	 *                    manga.
	 * @return Interfaz para la p�gina indicada.
	 * @throws LogException
	 */
	public IWebPage getWebPageInterface(final String webPageName) throws LogException {
		IWebPage ifaz = null;
		if (MANGA_HERE.equalsIgnoreCase(webPageName)) {
			ifaz = new MangaHereManager();
		} else if (MANGA_KAKALOT.equalsIgnoreCase(webPageName)) {
			ifaz = new MangaKakalotManager();
		} else {
			throw new LogException("No se ha facilitado un nombre de p�gina web v�lido.");
		}
		return ifaz;
	}

	private String getChapterFolderName(final float chapterNumber, final String workingDirectory) {
		final String chapterName = this.getValidChapterNumberName(chapterNumber);
		String folderName = FileUtils.addElementsToPath(workingDirectory, "" + chapterName);
		String folderNameAux = folderName;
		int i = 1;
		while (new File(folderName).exists()) {
			folderName = folderNameAux + " " + i;
			i++;
		}
		return folderName;
	}

	private String getValidChapterNumberName(final float chapterNumber) {
		String chapterName = "";
		if (chapterNumber < 1000) {
			chapterName = "0";
		}
		if (chapterNumber < 100) {
			chapterName += "0";
		}
		if (chapterNumber < 10) {
			chapterName += "0";
		}
		chapterName += "" + chapterNumber;
		return chapterName;
	}

	private String getPageName(final float chapterNumber, final int pageNumber) {
		String pageName = "Ch" + this.getValidChapterNumberName(chapterNumber) + "Pag";
		if (pageNumber < 1000) {
			pageName += "0";
		}
		if (pageNumber < 100) {
			pageName += "0";
		}
		if (pageNumber < 10) {
			pageName += "0";
		}
		pageName += "" + pageNumber;
		return pageName;
	}

}
