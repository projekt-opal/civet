package org.dice_research.opal.civet.data;

import java.util.List;

/**
 * {@link DataObject} of type {@link String}.
 *
 * @author Adrian Wilke
 */
public class StringDataObject extends AbstractDataObject<String> {

	/**
	 * Sets id. Creates empty list of values.
	 * 
	 * @throws NullPointerException if the given ID is null.
	 */
	public StringDataObject(String id) throws NullPointerException {
		super(id);
	}

	/**
	 * Sets id and sets value.
	 * 
	 * @throws NullPointerException if the given ID or the given value is null.
	 */
	public StringDataObject(String id, String value) throws NullPointerException {
		super(id, value);
	}

	/**
	 * Sets id and sets list of values.
	 * 
	 * @throws NullPointerException if the given ID or the given list of values is
	 *                              null.
	 */
	public StringDataObject(String id, List<String> values) throws NullPointerException {
		super(id, values);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<String> getType() {
		return String.class;
	}

}