package org.dice_research.opal.civet.data;

import java.util.HashMap;
import java.util.Map;

import org.dice_research.opal.civet.exceptions.InputMissingException;

/**
 * TODO:
 * 
 * add or rethink output
 * 
 * rethink to devide dataset inputs and configuration inputs
 */
public class DataContainer {

	private Map<InputType, Object> input = new HashMap<InputType, Object>();

	public Object getInput(InputType type) throws InputMissingException {
		if (!input.containsKey(type)) {
			throw new InputMissingException(type.toString());
		}
		return input.get(type);
	}

	public float getInputAsFloat(InputType type) throws InputMissingException {
		if (!input.containsKey(type)) {
			throw new InputMissingException(type.toString());
		}
		return Float.valueOf(getInputAsString(type));
	}

	public String getInputAsString(InputType type) throws InputMissingException {
		if (!input.containsKey(type)) {
			throw new InputMissingException(type.toString());
		}
		return String.valueOf(input.get(type));
	}

	public DataContainer setInput(InputType inputType, Object object) {
		input.put(inputType, object);
		return this;
	}
}