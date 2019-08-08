package es.bifacia.manga.downloader.interfaz;

import java.util.List;

import es.bifacia.manga.downloader.Exception.LogException;
import es.bifacia.manga.downloader.bean.Chapter;

public interface IWebPage {

	/**
	 * Obtiene la URL dentro de la p�gina del manga indicado.
	 * 
	 * @param mangaName Nombre del manga.
	 * @return URL del manga en la p�gina.
	 */
	String getMangaURL(final String mangaName) throws LogException;

	/**
	 * Obtiene le listado de cap�tulos del manga.
	 * 
	 * @param mangaURL URL del manga del que queremos obtener los cap�tulos.
	 * @return Listado de cap�tulos del manga.
	 */
	List<Chapter> getMangaChapters(final String mangaURL) throws LogException;

	/**
	 * Obtiene el listado de las URLs de donde descargar las im�genes de un
	 * cap�tulo.
	 * 
	 * @param chapterPath Path del cap�tulo dentro de la p�gina.
	 * @return Listado de las URLs de donde descargar las im�genes de un cap�tulo.
	 * @throws Exception
	 */
	List<String> getPagesURLs(final String chapterPath) throws LogException;

}
