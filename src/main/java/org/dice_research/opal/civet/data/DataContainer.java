package org.dice_research.opal.civet.data;

import java.util.HashMap;
import java.util.Map;

import org.dice_research.opal.civet.exceptions.InputMissingException;
import org.dice_research.opal.civet.metrics.Metric;

/**
 * Data container for calculations in {@link Metric} implementations.
 *
 * @author Adrian Wilke
 */
public class DataContainer {

	private Map<InputType, Object> input = new HashMap<InputType, Object>();

	public Object getInput(InputType type) throws InputMissingException {
		if (!input.containsKey(type)) {
			throw new InputMissingException(type);
		}
		return input.get(type);
	}

	public float getInputAsFloat(InputType type) throws InputMissingException {
		if (!input.containsKey(type)) {
			throw new InputMissingException(type);
		}
		return Float.valueOf(String.valueOf(input.get(type)));
	}

	public String getInputAsString(InputType type) throws InputMissingException {
		if (!input.containsKey(type)) {
			throw new InputMissingException(type);
		}
		return String.valueOf(input.get(type));
	}

	public DataContainer setInput(InputType inputType, Object object) {
		input.put(inputType, object);
		return this;
	}
}