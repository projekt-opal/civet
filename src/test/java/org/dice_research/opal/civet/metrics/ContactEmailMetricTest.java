package org.dice_research.opal.civet.metrics;

import static org.junit.Assert.assertEquals;

import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.DataObjects;
import org.dice_research.opal.civet.data.StringDataObject;
import org.junit.Test;

/**
 * Single metric test.
 *
 * @author Adrian Wilke
 */
public class ContactEmailMetricTest {

	@Test
	public void test() {

		DataContainer data = new DataContainer();

		data.putDataObject(new StringDataObject(DataObjects.CONTACT_EMAIL, ""));
		assertEquals(0, new ContactEmailMetric().getScore(data), 0);

		data.putDataObject(new StringDataObject(DataObjects.CONTACT_EMAIL, "john.doe-at-upb.de"));
		assertEquals(1, new ContactEmailMetric().getScore(data), 0);

		data.putDataObject(new StringDataObject(DataObjects.CONTACT_EMAIL, "x@x.x"));
		assertEquals(1, new ContactEmailMetric().getScore(data), 0);

		data.putDataObject(new StringDataObject(DataObjects.CONTACT_EMAIL, "john.doe@upb.de"));
		assertEquals(5, new ContactEmailMetric().getScore(data), 0);
	}
}