package org.dice_research.opal.civet.metrics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.InputType;
import org.dice_research.opal.civet.exceptions.InputMissingException;
import org.dice_research.opal.civet.metrics.CategorizationMetric;
import org.junit.Test;

/**
 * Single metric test.
 *
 * @author Adrian Wilke
 */
public class CategorizationMetricTest {

	@Test
	public void test() throws InputMissingException {

		DataContainer data = new DataContainer();

		data.setInput(InputType.NUMBER_OF_CATEGORIES, 0);
		assertEquals(0, new CategorizationMetric().getScore(data), 0);

		data.setInput(InputType.NUMBER_OF_CATEGORIES, 1);
		assertEquals(1, new CategorizationMetric().getScore(data), 0);

		data.setInput(InputType.NUMBER_OF_CATEGORIES, 2);
		assertEquals(2, new CategorizationMetric().getScore(data), 0);

		data.setInput(InputType.NUMBER_OF_CATEGORIES, 3);
		assertEquals(5, new CategorizationMetric().getScore(data), 0);

		data.setInput(InputType.NUMBER_OF_CATEGORIES, 4);
		assertEquals(5, new CategorizationMetric().getScore(data), 0);

	}

	@Test
	public void exceptionTest() {
		
		DataContainer data;

		data = new DataContainer();
		try {
			new CategorizationMetric().getScore(data);
			fail("No exception thrown");
		} catch (InputMissingException e) {
			// Okay, cats missing
		}
	}
}