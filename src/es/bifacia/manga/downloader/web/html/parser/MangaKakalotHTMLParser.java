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

public class MangaKakalotHTMLParser {
	private static final String TITLE_ATTRIBUTE = "title";
	private static final String MANGAS_LIST_CLASS = "panel_story_list";
	private static final String CHAPTERS_LIST_CLASS = "chapter-list";
	private static final String PAGES_MAIN_DIV_ID = "vung-doc";

	public MangaKakalotHTMLParser() {
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
			final Element mangasList = JSOUPManager.getElementByAttributeValue(document, JSOUPManager.DIV_TAG_NAME,
					JSOUPManager.BODY_CLASS_ATTRIBUTE, MANGAS_LIST_CLASS);
			if (mangasList == null) {
				throw new LogException(
						"No se ha encontrado el elemento UL donde est� el listado de mangas de la b�squeda.");
			}
			final Elements rows = mangasList.select(JSOUPManager.DIV_TAG_NAME);
			if (rows == null) {
				throw new LogException("No se han encontrado resultados para la b�squeda realizada.");
			}
			for (int i = 1; i < rows.size(); i++) {
				final Elements aElement = rows.get(i).select(JSOUPManager.A_TAG_NAME);
				if (aElement != null) {
					final Elements iElement = aElement.select(JSOUPManager.IMG_TAG_NAME);
					final String elementTitle = iElement.attr(JSOUPManager.ALT_ATTRIBUTE);
					if (elementTitle != null && elementTitle.trim().equalsIgnoreCase(mangaTitle)) {
						url = aElement.attr(JSOUPManager.HREF_ATTRIBUTE);
						break;
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
			final Element chaptersList = JSOUPManager.getElementByAttributeValue(document, JSOUPManager.DIV_TAG_NAME,
					JSOUPManager.BODY_CLASS_ATTRIBUTE, CHAPTERS_LIST_CLASS);
			if (chaptersList == null) {
				throw new LogException(
						"No se ha encontrado el elemento UL donde est� el listado de cap�tulos del manga.");
			}
			final Elements rows = chaptersList.select(JSOUPManager.DIV_TAG_NAME);
			if (rows == null) {
				throw new LogException("No se han encontrado resultados para la b�squeda realizada.");
			}
			for (int i = 0; i < rows.size(); i++) {
				final Element divElement = JSOUPManager.getElementByAttributeValue(rows.get(i),
						JSOUPManager.DIV_TAG_NAME, JSOUPManager.BODY_CLASS_ATTRIBUTE, "row");
				if (divElement != null) {
					final Chapter chapter = this.parseChapter(divElement);
					if (chapter != null) {
						chapters.add(chapter);
					} else {
						System.out.println(
								"No se ha obtenido informaci�n v�lida de este cap�tulo. \n" + divElement.toString());
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
	private Chapter parseChapter(final Element element) {
		Chapter chapter = null;
		final Elements spanElements = element.select(JSOUPManager.SPAN_TAG_NAME);
		if (spanElements != null) {
			for (int i = 0; i < spanElements.size(); i++) {
				final Elements aElement = spanElements.get(i).select(JSOUPManager.A_TAG_NAME);
				final String chapterName = aElement.attr(TITLE_ATTRIBUTE);
				final String chapterPath = aElement.attr(JSOUPManager.HREF_ATTRIBUTE);
				if (chapterName != null && !chapterName.isEmpty() && chapterPath != null && !chapterPath.isEmpty()) {
					if (chapterName.contains("chapter 645")) {
						chapter = new Chapter();
					}
					final float chapterNumber = this.getChapterNumberFromChapterName(chapterName);
					chapter = new Chapter(chapterNumber, chapterPath);
					break;
				}
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
		int index = chapterName.indexOf("Chapter ");
		if (index == -1) {
			index = chapterName.indexOf("chapter ");
		}
		if (index != -1) {
			int auxIndex = index + 8;
			while (auxIndex < chapterName.length() && (chapterName.charAt(auxIndex) == '.'
					|| (chapterName.charAt(auxIndex) >= '0' && chapterName.charAt(auxIndex) <= '9'))) {
				auxIndex++;
			}
			chapterNumber = Float.parseFloat(chapterName.substring(index + 8, auxIndex));
		}
		return chapterNumber;
	}

	/**
	 * Obtiene el listado de im�genes para un cap�tulo.
	 * 
	 * @param document Documento de la p�gina del cap�tulo con la informaci�n de las
	 *                 p�ginas.
	 * @returnListado de im�genes para un cap�tulo.
	 * @throws LogException
	 */
	public final List<String> parsePagesURLs(final Document document) throws LogException {
		final List<String> pagesURLs = new ArrayList<>();
		try {
			final Element divElement = JSOUPManager.getElementByAttributeValue(document, JSOUPManager.DIV_TAG_NAME,
					JSOUPManager.BODY_CLASS_ATTRIBUTE, PAGES_MAIN_DIV_ID);
			if (divElement == null) {
				throw new LogException("No se ha encontrado el elemento div que contiene las im�genes del cap�tulo.");
			}
			final Elements imagesElements = divElement.select(JSOUPManager.IMG_TAG_NAME);
			if (imagesElements == null) {
				throw new LogException("No se han encontrado los elementos img con las im�genes.");
			}
			for (int i = 0; i < imagesElements.size(); i++) {
				String pageURL = imagesElements.get(i).attr(JSOUPManager.SRC_ATTRIBUTE);
				if (pageURL != null && !pageURL.isEmpty()) {
					pageURL = pageURL.trim();
					pagesURLs.add(pageURL);
				}
			}
		} catch (Exception ex) {
			throw new LogException("Se ha producido un error intentando obtener la URL del manga buscado.", ex);
		}
		return pagesURLs;
	}

}
