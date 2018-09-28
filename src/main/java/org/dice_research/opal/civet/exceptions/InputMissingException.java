package org.dice_research.opal.civet.exceptions;

import org.dice_research.opal.civet.data.InputType;

/**
 * Thrown, if input data is missing.
 *
 * @author Adrian Wilke
 */
public class InputMissingException extends CivetException {

	private static final long serialVersionUID = 1L;

	InputType inputType;

	public InputMissingException(InputType inputType) {
		super("Missing input type " + inputType.toString());
		this.inputType = inputType;
	}

	public InputType getInputType() {
		return this.inputType;
	}

}