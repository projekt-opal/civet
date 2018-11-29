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
public class ContactUrlMetricTest {

	@Test
	public void test() {

		DataContainer data = new DataContainer();

		data.putDataObject(new StringDataObject(DataObjects.CONTACT_URL, ""));
		assertEquals(0, new ContactUrlMetric().getScore(data), 0);

		data.putDataObject(new StringDataObject(DataObjects.CONTACT_URL, "upb-de"));
		assertEquals(1, new ContactUrlMetric().getScore(data), 0);

		data.putDataObject(new StringDataObject(DataObjects.CONTACT_URL, "upb.de"));
		assertEquals(2, new ContactUrlMetric().getScore(data), 0);

		data.putDataObject(new StringDataObject(DataObjects.CONTACT_URL, "//upb.de"));
		assertEquals(3, new ContactUrlMetric().getScore(data), 0);

		data.putDataObject(new StringDataObject(DataObjects.CONTACT_URL, "http://dice.cs.upb.de"));
		assertEquals(5, new ContactUrlMetric().getScore(data), 0);
	}
}