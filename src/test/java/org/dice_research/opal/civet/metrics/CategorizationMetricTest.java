package org.dice_research.opal.civet.metrics;

import static org.junit.Assert.assertEquals;

import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.DataObjects;
import org.dice_research.opal.civet.data.IntegerDataObject;
import org.junit.Test;

/**
 * Single metric test.
 *
 * @author Adrian Wilke
 */
public class CategorizationMetricTest {

	@Test
	public void test() {

		DataContainer data = new DataContainer();

		data.putDataObject(new IntegerDataObject(DataObjects.NUMBER_OF_CATEGORIES, 0));
		assertEquals(0, new CategorizationMetric().getScore(data), 0);

		data.putDataObject(new IntegerDataObject(DataObjects.NUMBER_OF_CATEGORIES, 1));
		assertEquals(1, new CategorizationMetric().getScore(data), 0);

		data.putDataObject(new IntegerDataObject(DataObjects.NUMBER_OF_CATEGORIES, 2));
		assertEquals(2, new CategorizationMetric().getScore(data), 0);

		data.putDataObject(new IntegerDataObject(DataObjects.NUMBER_OF_CATEGORIES, 3));
		assertEquals(5, new CategorizationMetric().getScore(data), 0);

		data.putDataObject(new IntegerDataObject(DataObjects.NUMBER_OF_CATEGORIES, 4));
		assertEquals(5, new CategorizationMetric().getScore(data), 0);

	}

}