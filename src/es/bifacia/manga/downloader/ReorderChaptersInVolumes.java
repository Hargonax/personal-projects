package es.bifacia.manga.downloader;

import es.bifacia.manga.downloader.manager.ReorderManager;
import es.bifacia.manga.downloader.utils.ExceptionUtils;

public class ReorderChaptersInVolumes {

	public static void main(String[] args) {
		try {
			System.out.println("Comienza la transformación de capítulos a tomos.");
			final ReorderManager reorderManager = new ReorderManager();
//			reorderManager.compressVolumes("C:\\Varios\\Manga\\Cargar en la tablet\\Psyren");
			reorderManager.transformChaptersToVolumes("C:\\Varios\\Manga\\Me and the Devil Blues",
					"C:\\Varios\\Manga\\Transformado", true);
			System.out.println("Ha finalizado la transformación con éxito.");
		} catch (Exception ex) {
			System.out.println(
					"Ha finalizado la transformación a tomos con error. \n" + ExceptionUtils.getExceptionMessage(ex));
		}
	}

}
