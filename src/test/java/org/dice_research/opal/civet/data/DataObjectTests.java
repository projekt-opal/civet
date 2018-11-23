package org.dice_research.opal.civet.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

/**
 * Tests {@link DataObjects} and {@link DataObject}.
 *
 * @author Adrian Wilke
 */
public class DataObjectTests {

	/**
	 * Tests {@link DataObjects}.
	 */
	@Test
	public void testDataObjects() {

		// At least one id should be returned
		List<String> ids = DataObjects.getDataObjectIds();
		assertFalse(ids.isEmpty());

		// Create every known {@link DataObject}
		for (String id : ids) {
			DataObjects.createDataObject(id);
		}

		// Create unknown {@link DataObject}
		String unknownId = "UNKNOWN_ID";
		assumeFalse(ids.contains(unknownId));
		boolean thrown = false;
		try {
			DataObjects.createDataObject(unknownId);
		} catch (Exception e) {
			thrown = true;
		}
		assertTrue(thrown);

		// Create null {@link DataObject}
		thrown = false;
		try {
			DataObjects.createDataObject(null);
		} catch (Exception e) {
			thrown = true;
		}
		assertTrue(thrown);
	}

	/**
	 * Tests {@link DataObject} using {@link StringDataObject} as an implementation
	 * of {@link AbstractDataObject}.
	 */
	@Test
	public void testDataObject() {

		StringDataObject stringDataObject;

		String id = "Test-Data-Object-ID";
		String valueA = "Test-Data-Object-Value-A";
		String valueB = "Test-Data-Object-Value-B";

		List<String> values = new LinkedList<String>();
		values.add(valueA);
		values.add(valueB);

		// Constructors

		stringDataObject = new StringDataObject(id);
		assertEquals(0, stringDataObject.getValues().size());

		stringDataObject = new StringDataObject(id, valueA);
		assertEquals(1, stringDataObject.getValues().size());

		stringDataObject = new StringDataObject(id, values);
		assertEquals(2, stringDataObject.getValues().size());

		// Constructors and null values

		boolean thrown = false;
		try {
			stringDataObject = new StringDataObject(null);
		} catch (Exception e) {
			thrown = true;
		}
		assertTrue(thrown);

		thrown = false;
		try {
			String nullString = null;
			stringDataObject = new StringDataObject(id, nullString);
		} catch (Exception e) {
			thrown = true;
		}
		assertTrue(thrown);

		thrown = false;
		try {
			List<String> nullList = null;
			stringDataObject = new StringDataObject(id, nullList);
		} catch (Exception e) {
			thrown = true;
		}
		assertTrue(thrown);

		// General methods

		stringDataObject = new StringDataObject(id);

		assertEquals(id, stringDataObject.getId());
		assertEquals(String.class, stringDataObject.getType());

		// Values

		stringDataObject = new StringDataObject(id);

		stringDataObject.addValue(valueA);
		stringDataObject.addValue(valueB);
		assertEquals(2, stringDataObject.getValues().size());

		stringDataObject.setValue(valueA);
		assertEquals(1, stringDataObject.getValues().size());

		stringDataObject.setValues(values);
		assertEquals(2, stringDataObject.getValues().size());

		stringDataObject.addValues(values);
		assertEquals(4, stringDataObject.getValues().size());
	}

	/**
	 * Tests implementations of {@link AbstractDataObject}.
	 */
	@Test
	public void testDataObjectTypes() {

		DataObject<?> dataObject;
		String id = "Test-Data-Object-ID";

		String stringValue = "Test-Data-Object-Value";
		dataObject = new StringDataObject(id, stringValue);
		assertEquals(String.class, dataObject.getType());
		assertEquals(String.class, dataObject.getValues().get(0).getClass());
		assertEquals(stringValue, dataObject.getValues().get(0));

		Integer integerValue = 12345;
		dataObject = new IntegerDataObject(id, integerValue);
		assertEquals(Integer.class, dataObject.getType());
		assertEquals(Integer.class, dataObject.getValues().get(0).getClass());
		assertEquals(integerValue, dataObject.getValues().get(0));
	}

}