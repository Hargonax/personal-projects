package es.bifacia.manga.downloader;

import es.bifacia.manga.downloader.manager.ReorderManager;
import es.bifacia.manga.downloader.utils.ExceptionUtils;

public class ReorderChaptersInVolumes {

	public static void main(String[] args) {
		try {
			System.out.println("Comienza la transformación de capítulos a tomos.");
			final ReorderManager reorderManager = new ReorderManager();
			reorderManager.transformChaptersToVolumes("C:\\Varios\\Manga\\Watashitachi no Shiawase na Jikan",
					"C:\\Varios\\Manga\\Transformado");
			System.out.println("Ha finalizado la transformación con éxito.");
		} catch (Exception ex) {
			System.out.println(
					"Ha finalizado la transformación a tomos con error. \n" + ExceptionUtils.getExceptionMessage(ex));
		}
	}

}
