package org.dice_research.opal.civet.exceptions;

/**
 * Thrown, if input data is missing.
 *
 * @author Adrian Wilke
 */
public class InputMissingException extends CivetException {

	private static final long serialVersionUID = 1L;

	public InputMissingException() {
		super();
	}

	public InputMissingException(String message) {
		super(message);
	}

	public InputMissingException(String message, Throwable cause) {
		super(message, cause);
	}

	public InputMissingException(Throwable cause) {
		super(cause);
	}

}