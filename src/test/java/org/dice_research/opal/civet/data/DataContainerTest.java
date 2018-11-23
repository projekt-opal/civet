package org.dice_research.opal.civet.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.dice_research.opal.civet.exceptions.UnknownIdRuntimeException;
import org.junit.Test;

public class DataContainerTest {

	@Test
	public void test() {
		DataContainer dataContainer = new DataContainer();

		// Test null

		boolean thrown = false;
		try {
			dataContainer.setDataObject(null);
		} catch (NullPointerException e) {
			thrown = true;
		}
		assertTrue(thrown);

		// Object storage

		String id = "Test-Data-Object-ID";
		String unknownId = "Test-Data-Object-Unknown-ID";
		String stringValue = "Test-Data-Object-Value";
		dataContainer.setDataObject(new StringDataObject(id));
		assertTrue(dataContainer.getDataObject(id).getValues().isEmpty());

		dataContainer.setDataObject(new StringDataObject(id, stringValue));
		assertFalse(dataContainer.getDataObject(id).getValues().isEmpty());

		thrown = false;
		try {
			dataContainer.getIntegerDataObject(unknownId);
		} catch (UnknownIdRuntimeException e) {
			thrown = true;
		}
		assertTrue(thrown);

		// Object casting

		dataContainer.getStringDataObject(id);

		thrown = false;
		try {
			dataContainer.getIntegerDataObject(id);
		} catch (ClassCastException e) {
			thrown = true;
		}
		assertTrue(thrown);
	}
}