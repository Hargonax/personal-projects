package es.bifacia.manga.downloader.utils;

import java.lang.reflect.InvocationTargetException;

public class ExceptionUtils {

	/**
	 * Obtiene el mensaje de una excepción.
	 * 
	 * @param exception Excepción de la que obtener el mensaje.
	 * @return
	 */
	public static String getExceptionMessage(final Exception exception) {
		String message = null;
		if (exception instanceof InvocationTargetException) {
			final InvocationTargetException targetException = (InvocationTargetException) exception;
			if (targetException.getTargetException() != null) {
				message = targetException.getTargetException().getMessage();
			} else {
				message = exception.getMessage();
			}
		} else {
			message = exception.getMessage();
		}
		return message;
	}

}
