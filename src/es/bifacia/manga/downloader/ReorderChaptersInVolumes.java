package es.bifacia.manga.downloader;

import es.bifacia.manga.downloader.manager.ReorderManager;
import es.bifacia.manga.downloader.utils.ExceptionUtils;

public class ReorderChaptersInVolumes {

	public static void main(String[] args) {
		try {
			System.out.println("Comienza la transformaci�n de cap�tulos a tomos.");
			final ReorderManager reorderManager = new ReorderManager();
//			reorderManager.compressVolumes("C:\\Varios\\Manga\\Cargar en la tablet\\Psyren");
			reorderManager.transformChaptersToVolumes("C:\\Varios\\Manga\\Me and the Devil Blues",
					"C:\\Varios\\Manga\\Transformado", true);
			System.out.println("Ha finalizado la transformaci�n con �xito.");
		} catch (Exception ex) {
			System.out.println(
					"Ha finalizado la transformaci�n a tomos con error. \n" + ExceptionUtils.getExceptionMessage(ex));
		}
	}

}
