package org.dice_research.opal.civet.metrics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.InputType;
import org.dice_research.opal.civet.exceptions.InputMissingException;
import org.junit.Test;

/**
 * Single metric test.
 *
 * @author Adrian Wilke
 */
public class ContactUrlMetricTest {

	@Test
	public void test() throws InputMissingException {

		DataContainer data = new DataContainer();

		data.setInput(InputType.CONTACT_URL, "");
		assertEquals(0, new ContactUrlMetric().getScore(data), 0);

		data.setInput(InputType.CONTACT_URL, "upb-de");
		assertEquals(1, new ContactUrlMetric().getScore(data), 0);

		data.setInput(InputType.CONTACT_URL, "upb.de");
		assertEquals(2, new ContactUrlMetric().getScore(data), 0);

		data.setInput(InputType.CONTACT_URL, "//upb.de");
		assertEquals(3, new ContactUrlMetric().getScore(data), 0);

		data.setInput(InputType.CONTACT_URL, "http://dice.cs.upb.de");
		assertEquals(5, new ContactUrlMetric().getScore(data), 0);
	}

	@Test
	public void exceptionTest() {

		DataContainer data;

		data = new DataContainer();
		try {
			new ContactUrlMetric().getScore(data);
			fail("No exception thrown");
		} catch (InputMissingException e) {
			// Okay, url missing
		}
	}
}