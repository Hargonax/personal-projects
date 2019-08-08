package es.bifacia.manga.downloader;

import es.bifacia.manga.downloader.manager.ReorderManager;
import es.bifacia.manga.downloader.utils.ExceptionUtils;

public class ReorderChaptersInVolumes {

	public static void main(String[] args) {
		try {
			System.out.println("Comienza la transformaci�n de cap�tulos a tomos.");
			final ReorderManager reorderManager = new ReorderManager();
			reorderManager.transformChaptersToVolumes("C:\\Varios\\Manga\\Watashitachi no Shiawase na Jikan",
					"C:\\Varios\\Manga\\Transformado");
			System.out.println("Ha finalizado la transformaci�n con �xito.");
		} catch (Exception ex) {
			System.out.println(
					"Ha finalizado la transformaci�n a tomos con error. \n" + ExceptionUtils.getExceptionMessage(ex));
		}
	}

}
