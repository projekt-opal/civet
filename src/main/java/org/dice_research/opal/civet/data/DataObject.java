package org.dice_research.opal.civet.data;

import java.util.List;

/**
 * Generic data object.
 * 
 * Defines data identificator and data type. Holds list of data values.
 * 
 * Data objects are defined in {@link DataObjects}.
 * 
 * Data objects are used in {@link DataContainer}.
 *
 * @author Adrian Wilke
 */
public interface DataObject<TYPE> {

	/**
	 * Gets identificator of data object.
	 */
	String getId();

	/**
	 * Gets type of data object.
	 */
	Class<TYPE> getType();

	/**
	 * Gets values of data object.
	 */
	List<TYPE> getValues();

	/**
	 * Sets value of data object.
	 * 
	 * @throws NullPointerException if the given value is null.
	 */
	DataObject<TYPE> setValue(TYPE value) throws NullPointerException;

	/**
	 * Sets values of data object.
	 * 
	 * @throws NullPointerException if the given list of values is null.
	 */
	DataObject<TYPE> setValues(List<TYPE> values) throws NullPointerException;

	/**
	 * Adds value of data object.
	 * 
	 * @throws NullPointerException if the given value is null.
	 */
	DataObject<TYPE> addValue(TYPE value) throws NullPointerException;

	/**
	 * Adds values of data object.
	 * 
	 * @throws NullPointerException if the given list of values is null.
	 */
	DataObject<TYPE> addValues(List<TYPE> values) throws NullPointerException;

}