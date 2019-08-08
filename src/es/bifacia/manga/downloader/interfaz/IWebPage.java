package es.bifacia.manga.downloader.interfaz;

import java.util.List;

import es.bifacia.manga.downloader.Exception.LogException;
import es.bifacia.manga.downloader.bean.Chapter;

public interface IWebPage {

	/**
	 * Obtiene la URL dentro de la página del manga indicado.
	 * 
	 * @param mangaName Nombre del manga.
	 * @return URL del manga en la página.
	 */
	String getMangaURL(final String mangaName) throws LogException;

	/**
	 * Obtiene le listado de capítulos del manga.
	 * 
	 * @param mangaURL URL del manga del que queremos obtener los capítulos.
	 * @return Listado de capítulos del manga.
	 */
	List<Chapter> getMangaChapters(final String mangaURL) throws LogException;

	/**
	 * Obtiene el listado de las URLs de donde descargar las imágenes de un
	 * capítulo.
	 * 
	 * @param chapterPath Path del capítulo dentro de la página.
	 * @return Listado de las URLs de donde descargar las imágenes de un capítulo.
	 * @throws Exception
	 */
	List<String> getPagesURLs(final String chapterPath) throws LogException;

}
