package org.dice_research.opal.civet.metrics;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.fail;

import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.InputType;
import org.dice_research.opal.civet.exceptions.InputMissingException;
import org.dice_research.opal.civet.metrics.DescriptionMetric;
import org.junit.Test;

/**
 * Single metric test.
 *
 * @author Adrian Wilke
 */
public class DescriptionMetricTest {

	@Test
	public void test() throws InputMissingException {

		DataContainer data;

		data = new DataContainer();
		data.setInput(InputType.TITLE, "test");
		data.setInput(InputType.DESCRIPTION, "");
		assertEquals(1, new DescriptionMetric().getScore(data), 0);

		data = new DataContainer();
		data.setInput(InputType.TITLE, "");
		data.setInput(InputType.DESCRIPTION, "test");
		assertEquals(1, new DescriptionMetric().getScore(data), 0);

		data = new DataContainer();
		data.setInput(InputType.TITLE, "test");
		data.setInput(InputType.DESCRIPTION, "test");
		assertEquals(1, new DescriptionMetric().getScore(data), 0);

		data = new DataContainer();
		data.setInput(InputType.TITLE, "test");
		data.setInput(InputType.DESCRIPTION, "this is a description: 25");
		assertEquals(2, new DescriptionMetric().getScore(data), 0);

		data = new DataContainer();
		data.setInput(InputType.TITLE, "test");
		data.setInput(InputType.DESCRIPTION, "this is a description with some useful informat50");
		assertEquals(3, new DescriptionMetric().getScore(data), 0);

		data = new DataContainer();
		data.setInput(InputType.TITLE, "test");
		data.setInput(InputType.DESCRIPTION,
				"this is a description with some text which could contain useful informati75");
		assertEquals(4, new DescriptionMetric().getScore(data), 0);

		data = new DataContainer();
		data.setInput(InputType.TITLE, "test");
		data.setInput(InputType.DESCRIPTION,
				"this is a description with some text which could contain useful information and more");
		assertEquals(5, new DescriptionMetric().getScore(data), 0);
	}

	@Test
	public void exceptionTest() {

		DataContainer data;

		data = new DataContainer();
		data.setInput(InputType.TITLE, "test");
		try {
			new DescriptionMetric().getScore(data);
			fail("No exception thrown");
		} catch (InputMissingException e) {
			// Okay, description missing
		}

		data = new DataContainer();
		data.setInput(InputType.DESCRIPTION, "test");
		try {
			new DescriptionMetric().getScore(data);
			fail("No exception thrown");
		} catch (InputMissingException e) {
			// Okay, title missing
		}
	}
}