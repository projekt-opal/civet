package org.dice_research.opal.civet.exceptions;

/**
 * General OPAL Civet exception.
 *
 * @author Adrian Wilke
 */
public class CivetException extends Exception {

	private static final long serialVersionUID = 1L;

	public CivetException() {
		super();
	}

	public CivetException(String message) {
		super(message);
	}

	public CivetException(String message, Throwable cause) {
		super(message, cause);
	}

	public CivetException(Throwable cause) {
		super(cause);
	}

}