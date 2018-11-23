package org.dice_research.opal.civet.data;

import java.util.LinkedList;
import java.util.List;

import org.dice_research.opal.civet.exceptions.UnknownIdRuntimeException;

/**
 * Defines IDs and types of {@link DataObject}s.
 * 
 * Data object IDs are defined via {@link DataObjects} class constants.
 * 
 * Data object types are defined in static initializer of {@link DataObjects}.
 * 
 * Data objects are used in {@link DataContainer}.
 *
 * @author Adrian Wilke
 */
public abstract class DataObjects {

	// Definition of data object IDs

	public final static String CONTACT_EMAIL = "CONTACT_EMAIL";
	public final static String CONTACT_URL = "CONTACT_URL";
	public final static String DESCRIPTION = "DESCRIPTION";
	public final static String NUMBER_OF_CATEGORIES = "NUMBER_OF_CATEGORIES";
	public final static String TITLE = "TITLE";

	private static List<String> integerDataObjects = new LinkedList<String>();
	private static List<String> stringDataObjects = new LinkedList<String>();

	/**
	 * Definition of data object types.
	 */
	static {
		stringDataObjects.add(CONTACT_EMAIL);
		stringDataObjects.add(CONTACT_URL);
		stringDataObjects.add(DESCRIPTION);
		integerDataObjects.add(NUMBER_OF_CATEGORIES);
		stringDataObjects.add(TITLE);
	}

	/**
	 * Creates data object for given ID.
	 * 
	 * @throws UnknownIdRuntimeException if the given ID has not been defined.
	 * @throws NullPointerException      if the given ID is null.
	 */
	public static DataObject<?> createDataObject(String id) throws UnknownIdRuntimeException {
		if (id == null) {
			throw new NullPointerException("Data object ID is null");
		} else if (integerDataObjects.contains(id)) {
			return new IntegerDataObject(id);
		} else if (stringDataObjects.contains(id)) {
			return new StringDataObject(id);
		} else {
			throw new UnknownIdRuntimeException("Unknown data object ID: " + id);
		}
	}

	/**
	 * Gets list of all defined data object IDs.
	 */
	public static List<String> getDataObjectIds() {
		List<String> dataObjectIds = new LinkedList<String>();
		dataObjectIds.addAll(integerDataObjects);
		dataObjectIds.addAll(stringDataObjects);
		return dataObjectIds;
	}

}