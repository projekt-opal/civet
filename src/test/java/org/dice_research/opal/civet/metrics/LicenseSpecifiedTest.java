package org.dice_research.opal.civet.metrics;

import static org.junit.Assert.assertEquals;

import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.DataObjects;
import org.dice_research.opal.civet.data.StringDataObject;
import org.dice_research.opal.civet.exceptions.ParsingException;
import org.dice_research.opal.civet.exceptions.UnknownIdRuntimeException;
import org.junit.Test;

/**
 * Single metric test.
 *
 * @author Adrian Wilke
 */
public class LicenseSpecifiedTest {

	@Test
	public void test() throws NullPointerException, UnknownIdRuntimeException, ParsingException {

		DataContainer data = new DataContainer();

		data.putDataObject(new StringDataObject(DataObjects.LICENSE, "https://example.com/licence"));
		assertEquals(1, data.getDataObject(DataObjects.LICENSE).getValues().size(), 0);
		assertEquals(5, new LicenseSpecifiedMetric().getScore(data), 0);

	}

}