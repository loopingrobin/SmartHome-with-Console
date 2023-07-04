package de.loopingrobin.frontend;

/**
 * <pre>
 * Wird geworfen, wenn ein falsches Zeichen in der KonsolenUi benutzt wird.
 * </pre>
 * @author Robin Wagner
 * 
 */
public class AbbruchException extends Exception {

	/** Enthält die serialVersionUID. */
	private static final long serialVersionUID = -114076344645485469L;

	/**
	 * Erstellt eine leere Exception.
	 */
	public AbbruchException() {
		super();
	}

	/**
	 * Erstellt eine Exception mit message, cause, enableSuppression und writableStackTrace.
	 * @param message Die Nachricht der Exception
	 * @param cause Der Grund der Exception
	 * @param enableSuppression Suppression
	 * @param writableStackTrace StackTrace
	 */
	public AbbruchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Erstellt eine Exception mit message und cause.
	 * @param message Die Nachricht der Exception
	 * @param cause Der Grund der Exception
	 */
	public AbbruchException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Erstellt eine Exception mit message.
	 * @param message Die Nachricht der Exception
	 */
	public AbbruchException(String message) {
		super(message);
	}

	/**
	 * Erstellt eine Exception mit cause.
	 * @param cause Der Grund der Exception
	 */
	public AbbruchException(Throwable cause) {
		super(cause);
	}

}
