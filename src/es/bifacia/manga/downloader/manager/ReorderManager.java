package es.bifacia.manga.downloader.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import es.bifacia.manga.downloader.Exception.LogException;
import es.bifacia.manga.downloader.bean.ChapterFolder;
import es.bifacia.manga.downloader.utils.FileUtils;

public class ReorderManager {

	public ReorderManager() {
		super();
	}

	/**
	 * Transfiere las im�genes descargadas de diferentes cap�tulos de un manga a
	 * carpetas de vol�menes.
	 * 
	 * @param originPath      Path de origen donde se encuentran las carpetas de los
	 *                        cap�tulos.
	 * @param destinationPath Directorio de destino donde se van a crear las
	 *                        carpetas de vol�menes.
	 * @throws LogException
	 */
	public void transformChaptersToVolumes(final String originPath, final String destinationPath,
			final boolean compress) throws LogException {
		final File originDirectory = new File(originPath);
		if (!originDirectory.exists()) {
			throw new LogException("No existe el directorio de origen " + originPath
					+ " donde est�n los cap�tulos a ordenar en tomos.");
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
		if (compress) {
			this.compressVolumes(destinationPath);
		}
	}

	/**
	 * Obtiene la informaci�n de las carpetas de los cap�tulos.
	 * 
	 * @param originDirectory Directorio donde est�n las carpetas de los cap�tulos.
	 * @return Informaci�n de las carpetas de los cap�tulos.
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

	/**
	 * Comprime los vol�menes de un manga.
	 * 
	 * @param mangaPath Path del manga del que queremos comprimir los vol�menes.
	 * @throws LogException
	 */
	public void compressVolumes(final String mangaPath) throws LogException {
		final File mangaFile = new File(mangaPath);
		if (!mangaFile.exists()) {
			throw new LogException(
					"No existe la ubicaci�n en la que se encuentran los tomos a comprimir (" + mangaPath + ").");
		}
		for (final File volumeFile : mangaFile.listFiles()) {
			this.compressVolume(mangaPath, volumeFile);
		}
	}

	/**
	 * Comprime el contenido de la carpeta del volumen indicado.
	 * 
	 * @param mangaPath  Path del manga al que pertenece el volumen.
	 * @param volumeFile Fichero con la informaci�n del path del volumen.
	 * @throws LogException
	 */
	private void compressVolume(final String mangaPath, final File volumeFile) throws LogException {
		try {
			final String volumePath = mangaPath + File.separator + volumeFile.getName();
			final String zipFilePath = volumePath + ".zip";
			final String cbzFilePath = volumePath + ".cbz";
			byte[] buffer = new byte[1024];
			final FileOutputStream fileOutputStream = new FileOutputStream(zipFilePath);
			try (final ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {
				final File[] files = volumeFile.listFiles();
				for (int i = 0; i < files.length; i++) {
					try (final FileInputStream fileInputStream = new FileInputStream(files[i])) {
						zipOutputStream.putNextEntry(new ZipEntry(files[i].getName()));
						int length;
						while ((length = fileInputStream.read(buffer)) > 0) {
							zipOutputStream.write(buffer, 0, length);
						}
						zipOutputStream.closeEntry();
					}
				}
			}
			FileUtils.renameFile(zipFilePath, cbzFilePath);
		} catch (Exception ex) {
			throw new LogException("Se ha producido un error al generer el fichero cbz del volumen.", ex);
		}
	}

}
