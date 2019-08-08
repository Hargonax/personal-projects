package es.bifacia.manga.downloader.web.html.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import es.bifacia.manga.downloader.Exception.LogException;
import es.bifacia.manga.downloader.bean.Chapter;
import es.bifacia.manga.downloader.jsoup.JSOUPManager;
import es.bifacia.manga.downloader.utils.WebUtils;
import es.bifacia.manga.downloader.web.page.manager.MangaHereManager;

public class MangaHereHTMLParser {
	private static final String TITLE_ATTRIBUTE = "title";
	private static final String CHAPTERS_LIST_CLASS = "detail-main-list";
	private static final String PAGES_DIV_CLASS = "pager-list cp-pager-list";
	private static final String PAGES_MAIN_DIV_CLASS = "pager-list-left";
	private static final String IMAGE_CLASS = "reader-main-img";

	public MangaHereHTMLParser() {
		super();
	}

	/**
	 * Obtiene el path para llegar a la URL del manga.
	 * 
	 * @param document   Documento con el contenido de la b�squeda para el nombre
	 *                   indicado.
	 * @param mangaTitle Nombre del manga indicado en la b�squeda.
	 * @return Path de la URL del manga en la p�gina.
	 * @throws LogException
	 */
	public String parseMangaURL(final Document document, final String mangaTitle) throws LogException {
		String url = null;
		try {
			final Elements mangasList = document.select(JSOUPManager.UL_TAG_NAME);
			if (mangasList == null) {
				throw new LogException(
						"No se ha encontrado el elemento UL donde est� el listado de mangas de la b�squeda.");
			}
			final Elements rows = mangasList.select(JSOUPManager.LI_TAG_NAME);
			if (rows == null) {
				throw new LogException("No se han encontrado resultados para la b�squeda realizada.");
			}
			for (int i = 1; i < rows.size(); i++) {
				final Elements liElement = rows.get(i).select(JSOUPManager.LI_TAG_NAME);
				if (liElement != null) {
					final Elements aElement = liElement.select(JSOUPManager.A_TAG_NAME);
					if (aElement != null) {
						final String elementTitle = aElement.attr(TITLE_ATTRIBUTE);
						if (elementTitle != null && elementTitle.trim().equalsIgnoreCase(mangaTitle)) {
							url = aElement.attr(JSOUPManager.HREF_ATTRIBUTE);
							break;
						}
					}
				}
			}
		} catch (Exception ex) {
			throw new LogException("Se ha producido un error intentando obtener la URL del manga buscado.", ex);
		}
		return url;
	}

	/**
	 * Parsea la informaci�n de los mangas de la p�gina del manga.
	 * 
	 * @param document Documento con la informaci�n de los cap�tulos.
	 * @return Listado de cap�tulos para el manga.
	 * @throws LogException
	 */
	public List<Chapter> parseChapters(final Document document) throws LogException {
		List<Chapter> chapters = new ArrayList<>();
		try {
			final Element chaptersList = JSOUPManager.getElementByAttributeValue(document, JSOUPManager.UL_TAG_NAME,
					JSOUPManager.BODY_CLASS_ATTRIBUTE, CHAPTERS_LIST_CLASS);
			if (chaptersList == null) {
				throw new LogException(
						"No se ha encontrado el elemento UL donde est� el listado de cap�tulos del manga.");
			}
			final Elements rows = chaptersList.select(JSOUPManager.LI_TAG_NAME);
			if (rows == null) {
				throw new LogException("No se han encontrado resultados para la b�squeda realizada.");
			}
			for (int i = 1; i < rows.size(); i++) {
				final Elements liElement = rows.get(i).select(JSOUPManager.LI_TAG_NAME);
				if (liElement != null) {
					final Chapter chapter = this.parseChapter(liElement);
					if (chapter != null) {
						chapters.add(chapter);
					} else {
						System.out.println(
								"No se ha obtenido informaci�n v�lida de este cap�tulo. \n" + liElement.toString());
					}
				}
			}
			Collections.sort(chapters);
		} catch (Exception ex) {
			throw new LogException("Se ha producido un error intentando obtener la URL del manga buscado.", ex);
		}
		return chapters;
	}

	/**
	 * Parsea un cap�tulo a partir de un elemento.
	 * 
	 * @param element Elemento con la informaci�n del cap�tulo a parsear.
	 * @return Cap�tulo del manga.
	 */
	private Chapter parseChapter(final Elements element) {
		Chapter chapter = null;
		final Elements aElement = element.select(JSOUPManager.A_TAG_NAME);
		if (aElement != null) {
			final String chapterName = aElement.attr(TITLE_ATTRIBUTE);
			final String chapterPath = aElement.attr(JSOUPManager.HREF_ATTRIBUTE);
			if (chapterName != null && !chapterName.isEmpty() && chapterPath != null && !chapterPath.isEmpty()) {
				final float chapterNumber = this.getChapterNumberFromChapterName(chapterName);
				chapter = new Chapter(chapterNumber, chapterPath);
			}
		}
		return chapter;
	}

	/**
	 * Obtiene el nombre del cap�tulo a partir del nombre del cap�tulo.
	 * 
	 * @param chapterName Nombre del cap�tulo con la informaci�n del n�mero de
	 *                    cap�tulo.
	 * @return N�mero del cap�tulo.
	 */
	private float getChapterNumberFromChapterName(final String chapterName) {
		float chapterNumber = -1;
		int index = chapterName.indexOf("Ch.");
		if (index != -1) {
			int auxIndex = index + 3;
			while (chapterName.charAt(auxIndex) == '.'
					|| (chapterName.charAt(auxIndex) >= '0' && chapterName.charAt(auxIndex) <= '9')) {
				auxIndex++;
			}
			chapterNumber = Float.parseFloat(chapterName.substring(index + 3, auxIndex));
		}
		return chapterNumber;
	}

	public boolean parsePagesURLs(final List<String> pagesURLs, final Document document) throws LogException {
		boolean continueParsing = true;
		try {
			final String pageURL = this.getPageURL(document);
			if (pageURL != null && !pageURL.isEmpty()) {
				pagesURLs.add(pageURL);
			}
			String nextPagePath = this.getNextPagePath(document);
			if (nextPagePath == null || nextPagePath.isEmpty()) {
				continueParsing = false;
			}
			nextPagePath = WebUtils.addPathToURL(MangaHereManager.MAIN_URL, nextPagePath);
			final Document nextDocument = JSOUPManager.getHTMLDocument(nextPagePath);
			this.parsePagesURLs(pagesURLs, nextDocument);
		} catch (Exception ex) {
			throw new LogException("Se ha producido un error intentando obtener la URL del manga buscado.", ex);
		}
		return continueParsing;
	}

	/**
	 * Obtiene la URL de donde descargar la imagen.
	 * @param document
	 * @return
	 * @throws LogException
	 */
	private String getPageURL(final Document document) throws LogException {
		String pageURL = null;
		try {
			final Element imgElement = JSOUPManager.getElementByAttributeValue(document, JSOUPManager.IMG_TAG_NAME,
					JSOUPManager.BODY_CLASS_ATTRIBUTE, IMAGE_CLASS);
			if (imgElement == null) {
				throw new LogException("No se ha encontrado el elemento img con la ruta a la imagen.");
			}
			pageURL = imgElement.attr(JSOUPManager.SRC_ATTRIBUTE);
		} catch (Exception ex) {
			throw new LogException("Se ha producido un error al intentar obtener la URL de la siguiente p�gina.");
		}
		return pageURL;
	}

	private String getNextPagePath(final Document document) throws LogException {
		String nextPagePath = null;
		try {
			Element divElement = JSOUPManager.getElementByAttributeValue(document, JSOUPManager.DIV_TAG_NAME,
					JSOUPManager.BODY_CLASS_ATTRIBUTE, PAGES_DIV_CLASS);
			if (divElement == null) {
				throw new LogException("No se ha obtenido la informaci�n de la botonera de las p�ginas del cap�tulo.");
			}
			divElement = JSOUPManager.getElementByAttributeValue(divElement, JSOUPManager.DIV_TAG_NAME,
					JSOUPManager.BODY_CLASS_ATTRIBUTE, PAGES_MAIN_DIV_CLASS);
			if (divElement == null) {
				throw new LogException("No se ha obtenido la informaci�n de la botonera de las p�ginas del cap�tulo.");
			}
			final Element spanElement = divElement.selectFirst(JSOUPManager.SPAN_TAG_NAME);
			final Elements pagesElements = spanElement.select(JSOUPManager.A_TAG_NAME);
			if (pagesElements == null) {
				throw new LogException("No se ha obtenido informaci�n de las p�ginas.");
			}
			final Element aElement = pagesElements.get(pagesElements.size() - 1);
			nextPagePath = aElement.attr(JSOUPManager.HREF_ATTRIBUTE);
			if (nextPagePath != null) {
				nextPagePath = nextPagePath.trim();
			}
		} catch (Exception ex) {
			throw new LogException("Se ha producido un error al intentar obtener el path de la siguiente p�gina.");
		}
		return nextPagePath;
	}

}
