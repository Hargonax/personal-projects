package es.bifacia.manga.downloader.manager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import es.bifacia.manga.downloader.Exception.LogException;
import es.bifacia.manga.downloader.bean.ChapterFolder;
import es.bifacia.manga.downloader.utils.FileUtils;

public class ReorderManager {

	public ReorderManager() {
		super();
	}

	/**
	 * Transfiere las imágenes descargadas de diferentes capítulos de un manga a
	 * carpetas de volúmenes.
	 * 
	 * @param originPath      Path de origen donde se encuentran las carpetas de los
	 *                        capítulos.
	 * @param destinationPath Directorio de destino donde se van a crear las
	 *                        carpetas de volúmenes.
	 * @throws LogException
	 */
	public void transformChaptersToVolumes(final String originPath, final String destinationPath) throws LogException {
		final File originDirectory = new File(originPath);
		if (!originDirectory.exists()) {
			throw new LogException("No existe el directorio de origen " + originPath
					+ " donde están los capítulos a ordenar en tomos.");
		}
		final File destinationDirectory = new File(destinationPath);
		if (!destinationDirectory.exists()) {
			destinationDirectory.mkdirs();
		}
		final List<ChapterFolder> chaptersFolders = this.getChaptersFoldersInformation(originDirectory);
		int i = 2;
		int imagesCopied = 0;
		String volumePath = FileUtils.addElementsToPath(destinationPath, "Vol 001");
		FileUtils.createDirectoryIfNeccessary(volumePath);
		for (final ChapterFolder chapterFolder : chaptersFolders) {
			if (imagesCopied >= 180) {
				String volumeName = "Vol ";
				if (i < 100) {
					volumeName += "0";
				}
				if (i < 10) {
					volumeName += "0";
				}
				volumeName += i;
				volumePath = FileUtils.addElementsToPath(destinationPath, volumeName);
				FileUtils.createDirectoryIfNeccessary(volumePath);
				i++;
				imagesCopied = 0;
			}
			final File chapterFolderFile = new File(chapterFolder.getPath());
			for (final File pageFile : chapterFolderFile.listFiles()) {
				try {
					final Path originFilePath = Paths.get(pageFile.getAbsolutePath());
					final String destinationFilePath = FileUtils.addElementsToPath(volumePath, pageFile.getName());
					final Path targetFilePath = Paths.get(destinationFilePath);
					Files.copy(originFilePath, targetFilePath);
					imagesCopied++;
				} catch (Exception ex) {
					throw new LogException("Se ha producido un error al intentar copiar la imagen.", ex);
				}
			}
		}
	}

	/**
	 * Obtiene la información de las carpetas de los capítulos.
	 * 
	 * @param originDirectory Directorio donde están las carpetas de los capítulos.
	 * @return Información de las carpetas de los capítulos.
	 */
	private List<ChapterFolder> getChaptersFoldersInformation(final File originDirectory) {
		final List<ChapterFolder> chaptersFolders = new ArrayList<>();
		for (final File chapterFolderFile : originDirectory.listFiles()) {
			if (chapterFolderFile.list().length > 0) {
				final ChapterFolder chapterFolder = new ChapterFolder(chapterFolderFile.getName(),
						chapterFolderFile.getAbsolutePath(), chapterFolderFile.list().length);
				chaptersFolders.add(chapterFolder);
			}
		}
		return chaptersFolders;
	}

}
