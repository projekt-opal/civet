package org.dice_research.opal.civet.data;

import java.util.HashMap;
import java.util.Map;

import org.dice_research.opal.civet.exceptions.UnknownIdRuntimeException;

/**
 * Data container for {@link DataObject}s.
 * 
 * Data object IDs and types are defined in {@link DataObjects}.
 *
 * @author Adrian Wilke
 */
public class DataContainer {

	private Map<String, DataObject<?>> dataObjects = new HashMap<String, DataObject<?>>();

	/**
	 * Gets data object.
	 * 
	 * @param id as specified in class constants of {@link DataObjects}
	 * 
	 * @throws NullPointerException      if the given ID is null.
	 * @throws UnknownIdRuntimeException if the given ID has not been defined.
	 */
	public DataObject<?> getDataObject(String id) throws NullPointerException, UnknownIdRuntimeException {
		if (id == null) {
			throw new NullPointerException("Data object ID is null");
		} else if (dataObjects.containsKey(id)) {
			return dataObjects.get(id);
		} else {
			throw new UnknownIdRuntimeException("Unknown data object ID: " + id);
		}
	}

	/**
	 * Gets casted data object.
	 * 
	 * @throws NullPointerException      if the given ID is null.
	 * @throws UnknownIdRuntimeException if the given ID has not been defined.
	 * @throws ClassCastException        if type of data object does not fit.
	 */
	public IntegerDataObject getIntegerDataObject(String id)
			throws NullPointerException, UnknownIdRuntimeException, ClassCastException {
		return IntegerDataObject.cast(getDataObject(id));
	}

	/**
	 * Gets casted data object.
	 * 
	 * @throws NullPointerException      if the given ID is null.
	 * @throws UnknownIdRuntimeException if the given ID has not been defined.
	 * @throws ClassCastException        if type of data object does not fit.
	 */
	public StringDataObject getStringDataObject(String id)
			throws NullPointerException, UnknownIdRuntimeException, ClassCastException {
		return StringDataObject.cast(getDataObject(id));
	}

	/**
	 * Sets data object.
	 * 
	 * @param dataObject implementation of {@link AbstractDataObject}
	 * 
	 * @throws NullPointerException if the given data object is null.
	 */
	public DataContainer setDataObject(DataObject<?> dataObject) throws NullPointerException {
		if (dataObject == null) {
			throw new NullPointerException("Data object is null");
		} else {
			dataObjects.put(dataObject.getId(), dataObject);
			return this;
		}
	}
}