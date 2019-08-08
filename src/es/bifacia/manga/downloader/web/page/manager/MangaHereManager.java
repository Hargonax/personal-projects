package es.bifacia.manga.downloader.web.page.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;

import es.bifacia.manga.downloader.Exception.LogException;
import es.bifacia.manga.downloader.bean.Chapter;
import es.bifacia.manga.downloader.interfaz.IWebPage;
import es.bifacia.manga.downloader.jsoup.JSOUPManager;
import es.bifacia.manga.downloader.utils.FileUtils;
import es.bifacia.manga.downloader.utils.WebUtils;
import es.bifacia.manga.downloader.web.html.parser.MangaHereHTMLParser;

public class MangaHereManager implements IWebPage {
	public static final String MAIN_URL = "https://www.mangahere.cc";
	private static final String SEARCH_PATH = "/search?title=";

	/**
	 * Obtiene la URL dentro de la p�gina del manga indicado.
	 * 
	 * @param mangaTitle Nombre del manga.
	 * @return URL del manga en la p�gina.
	 */
	@Override
	public String getMangaURL(String mangaTitle) throws LogException {
		String url = null;
		final MangaHereHTMLParser parser = new MangaHereHTMLParser();
		try {
			final String searchURL = MAIN_URL + SEARCH_PATH + mangaTitle;
			final Document document = JSOUPManager.getHTMLDocument(searchURL);
			if (document == null) {
				throw new LogException(
						"No se ha recuperado correctamente el contenido de la URL de b�squeda " + searchURL + ".");
			}
			String mangaPath = parser.parseMangaURL(document, mangaTitle);
			if (mangaPath == null || mangaPath.trim().isEmpty()) {
				throw new LogException("No se ha recuperado un path v�lido");
			}
			url = WebUtils.addPathToURL(MAIN_URL, mangaPath);
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
			final MangaHereHTMLParser parser = new MangaHereHTMLParser();
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
		final List<String> pagesPaths = new ArrayList<>();
		try {
			final MangaHereHTMLParser parser = new MangaHereHTMLParser();
			final String chapterURL = WebUtils.addPathToURL(MAIN_URL, chapterPath);
			final Document document = JSOUPManager.getHTMLDocument(chapterURL);
			if (document == null) {
				throw new LogException(
						"No se ha recuperado correctamente el contenido de la URL del cap�tulo '" + chapterURL + "'.");
			}
			parser.parsePagesURLs(pagesPaths, document);
		} catch (Exception ex) {
			throw new LogException(
					"Se ha producido un error al intentar obtener los paths de las p�ginas del cap�tulo.", ex);
		}
		return pagesPaths;
	}

}
