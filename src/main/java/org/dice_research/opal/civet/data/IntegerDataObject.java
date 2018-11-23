package org.dice_research.opal.civet.data;

import java.util.List;

/**
 * {@link DataObject} of type {@link Integer}.
 *
 * @author Adrian Wilke
 */
public class IntegerDataObject extends AbstractDataObject<Integer> {

	/**
	 * Sets id. Creates empty list of values.
	 * 
	 * @throws NullPointerException if the given ID is null.
	 */
	public IntegerDataObject(String id) throws NullPointerException {
		super(id);
	}

	/**
	 * Sets id and sets value.
	 * 
	 * @throws NullPointerException if the given ID or the given value is null.
	 */
	public IntegerDataObject(String id, Integer value) throws NullPointerException {
		super(id, value);
	}

	/**
	 * Sets id and sets list of values.
	 * 
	 * @throws NullPointerException if the given ID or the given list of values is
	 *                              null.
	 */
	public IntegerDataObject(String id, List<Integer> values) throws NullPointerException {
		super(id, values);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<Integer> getType() {
		return Integer.class;
	}

}