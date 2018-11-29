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
public class DescriptionMetricTest {

	@Test
	public void test() {

		DataContainer data;

		data = new DataContainer();
		data.putDataObject(new StringDataObject(DataObjects.TITLE, "test"));
		data.putDataObject(new StringDataObject(DataObjects.DESCRIPTION, ""));
		assertEquals(1, new DescriptionMetric().getScore(data), 0);

		data = new DataContainer();
		data.putDataObject(new StringDataObject(DataObjects.TITLE, ""));
		data.putDataObject(new StringDataObject(DataObjects.DESCRIPTION, "test"));
		assertEquals(1, new DescriptionMetric().getScore(data), 0);

		data = new DataContainer();
		data.putDataObject(new StringDataObject(DataObjects.TITLE, "test"));
		data.putDataObject(new StringDataObject(DataObjects.DESCRIPTION, "test"));
		assertEquals(1, new DescriptionMetric().getScore(data), 0);

		data = new DataContainer();
		data.putDataObject(new StringDataObject(DataObjects.TITLE, "test"));
		data.putDataObject(new StringDataObject(DataObjects.DESCRIPTION, "this is a description: 25"));
		assertEquals(2, new DescriptionMetric().getScore(data), 0);

		data = new DataContainer();
		data.putDataObject(new StringDataObject(DataObjects.TITLE, "test"));
		data.putDataObject(
				new StringDataObject(DataObjects.DESCRIPTION, "this is a description with some useful informat50"));
		assertEquals(3, new DescriptionMetric().getScore(data), 0);

		data = new DataContainer();
		data.putDataObject(new StringDataObject(DataObjects.TITLE, "test"));
		data.putDataObject(new StringDataObject(DataObjects.DESCRIPTION,
				"this is a description with some text which could contain useful informati75"));
		assertEquals(4, new DescriptionMetric().getScore(data), 0);

		data = new DataContainer();
		data.putDataObject(new StringDataObject(DataObjects.TITLE, "test"));
		data.putDataObject(new StringDataObject(DataObjects.DESCRIPTION,
				"this is a description with some text which could contain useful information and more"));
		assertEquals(5, new DescriptionMetric().getScore(data), 0);
	}
}