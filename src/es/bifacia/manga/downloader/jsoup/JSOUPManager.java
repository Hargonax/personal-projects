package es.bifacia.manga.downloader.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public abstract class JSOUPManager {
	public static final String A_TAG_NAME = "a";
	public static final String LI_TAG_NAME = "li";
	public static final String UL_TAG_NAME = "ul";
	public static final String DIV_TAG_NAME = "div";
	public static final String IMG_TAG_NAME = "img";
	public static final String BODY_TAG_NAME = "body";
	public static final String SPAN_TAG_NAME = "span";
	public static final String TABLE_TAG_NAME = "table";
	public static final String TABLE_ROW_TAG_NAME = "tr";
	public static final String TABLE_BODY_TAG_NAME = "tbody";
	public static final String TABLE_COLUMN_TAG_NAME = "td";
	public static final String ID_ATTRIBUTE = "id";
	public static final String ALT_ATTRIBUTE = "alt";
	public static final String SRC_ATTRIBUTE = "src";
	public static final String HREF_ATTRIBUTE = "href";
	public static final String BODY_CLASS_ATTRIBUTE = "class";

	/**
	 * Obtiene el documento de un HTML.
	 * 
	 * @param url URL de la que se quiere recuperar el documento.
	 * @return Documento de un HTML.
	 * @throws Exception
	 */
	public static Document getHTMLDocument(final String url) throws Exception {
		Document document = null;
		try {
			document = Jsoup.connect(url).get();
		} catch (Exception ex) {
			throw new Exception(
					"Se ha producido un error la intentar obtener el documento del HTML.\n" + ex.getMessage());
		}
		return document;
	}

	/**
	 * Obtiene el elemento hijo (directo o indirecto) del elemento indicado que
	 * tenga el valor indicado para el atributo indicado.
	 * 
	 * @param element        Elemento en el que vamos a buscar el elemento que
	 *                       cumpla los parámetros indicados.
	 * @param tagName        Nombre de la etiqueta que ha de tener el hijo.
	 * @param attributeName  Atributo que ha de tener el hijo.
	 * @param attributeValue Valor de atributo que ha de tener el hijo.
	 * @return Elemento hijo para los parámetros indicados.
	 */
	public static Element getElementByAttributeValue(final Element element, final String tagName,
			final String attributeName, final String attributeValue) {
		return element.selectFirst(tagName + "[" + attributeName + "=" + attributeValue + "]");
	}

	/**
	 * Obtiene el contenido de texto de un elemento.
	 * 
	 * @param element Elemento del que queremos obtener el contenido de texto.
	 * @return Contenido de texto de un elemento.
	 */
	public static String getElementContent(final Element element) {
		String content = null;
		for (final Node node : element.childNodes()) {
			if (node instanceof TextNode) {
				content = ((TextNode) node).text();
				break;
			}
		}
		return content;
	}

}
