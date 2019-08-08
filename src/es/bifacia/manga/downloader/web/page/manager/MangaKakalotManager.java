package es.bifacia.manga.downloader.web.page.manager;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;

import es.bifacia.manga.downloader.Exception.LogException;
import es.bifacia.manga.downloader.bean.Chapter;
import es.bifacia.manga.downloader.interfaz.IWebPage;
import es.bifacia.manga.downloader.jsoup.JSOUPManager;
import es.bifacia.manga.downloader.utils.WebUtils;
import es.bifacia.manga.downloader.web.html.parser.MangaKakalotHTMLParser;

public class MangaKakalotManager implements IWebPage {
	public static final String MAIN_URL = "https://mangakakalot.com";
	private static final String SEARCH_PATH = "/search/";

	/**
	 * Obtiene la URL dentro de la p�gina del manga indicado.
	 * 
	 * @param mangaTitle Nombre del manga.
	 * @return URL del manga en la p�gina.
	 */
	@Override
	public String getMangaURL(String mangaTitle) throws LogException {
		String url = null;
		final MangaKakalotHTMLParser parser = new MangaKakalotHTMLParser();
		try {
			final String searchURL = MAIN_URL + SEARCH_PATH + mangaTitle;
			final Document document = JSOUPManager.getHTMLDocument(searchURL);
			if (document == null) {
				throw new LogException(
						"No se ha recuperado correctamente el contenido de la URL de b�squeda " + searchURL + ".");
			}
			url = parser.parseMangaURL(document, mangaTitle);
			if (url == null || url.trim().isEmpty()) {
				throw new LogException("No se ha recuperado un path v�lido");
			}
			url = url.trim();
		} catch (Exception ex) {
			throw new LogException("Se ha producido un error al intentar obtener la p�gina del manga en MangaHere.",
					ex);
		}
		return url;
	}

	/**
	 * Obtiene le listado de cap�tulos del manga.
	 * 
	 * @param mangaURL URL del manga del que queremos obtener los cap�tulos.
	 * @return Listado de cap�tulos del manga.
	 */
	@Override
	public List<Chapter> getMangaChapters(final String mangaURL) throws LogException {
		List<Chapter> chapters = null;
		try {
			final MangaKakalotHTMLParser parser = new MangaKakalotHTMLParser();
			final Document document = JSOUPManager.getHTMLDocument(mangaURL);
			if (document == null) {
				throw new LogException(
						"No se ha recuperado correctamente el contenido de la URL del manga '" + mangaURL + "'.");
			}
			chapters = parser.parseChapters(document);
		} catch (Exception ex) {
			throw new LogException("Se ha producido un error al obtener los cap�tulos del manga.", ex);
		}
		return chapters;
	}

	/**
	 * Obtiene el listado de las URLs de donde descargar las im�genes de un
	 * cap�tulo.
	 * 
	 * @param chapterPath Path del cap�tulo dentro de la p�gina.
	 * @return Listado de las URLs de donde descargar las im�genes de un cap�tulo.
	 * @throws Exception
	 */
	public List<String> getPagesURLs(final String chapterPath) throws LogException {
		List<String> pagesPaths = null;
		try {
			final MangaKakalotHTMLParser parser = new MangaKakalotHTMLParser();
			final Document document = JSOUPManager.getHTMLDocument(chapterPath);
			if (document == null) {
				throw new LogException(
						"No se ha recuperado correctamente el contenido de la URL del cap�tulo '" + chapterPath + "'.");
			}
			pagesPaths = parser.parsePagesURLs(document);
		} catch (Exception ex) {
			throw new LogException(
					"Se ha producido un error al intentar obtener los paths de las p�ginas del cap�tulo.", ex);
		}
		return pagesPaths;
	}

}
