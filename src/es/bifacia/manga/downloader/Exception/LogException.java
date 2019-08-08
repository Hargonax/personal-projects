package es.bifacia.manga.downloader.Exception;

import es.bifacia.manga.downloader.utils.ExceptionUtils;

@SuppressWarnings("serial")
public class LogException extends Exception {

	public LogException() {
		super();
	}

	public LogException(final String message) {
		super(message);
	}

	public LogException(final String message, final Exception exception) {
		super(message + " \n" + ExceptionUtils.getExceptionMessage(exception));
	}

}
