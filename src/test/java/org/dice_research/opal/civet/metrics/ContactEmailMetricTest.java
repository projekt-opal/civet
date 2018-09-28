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
public class ContactEmailMetricTest {

	@Test
	public void test() throws InputMissingException {

		DataContainer data = new DataContainer();

		data.setInput(InputType.CONTACT_EMAIL, "");
		assertEquals(0, new ContactEmailMetric().getScore(data), 0);

		data.setInput(InputType.CONTACT_EMAIL, "john.doe-at-upb.de");
		assertEquals(1, new ContactEmailMetric().getScore(data), 0);

		data.setInput(InputType.CONTACT_EMAIL, "x@x.x");
		assertEquals(1, new ContactEmailMetric().getScore(data), 0);

		data.setInput(InputType.CONTACT_EMAIL, "john.doe@upb.de");
		assertEquals(5, new ContactEmailMetric().getScore(data), 0);
	}

	@Test
	public void exceptionTest() {

		DataContainer data;

		data = new DataContainer();
		try {
			new ContactEmailMetric().getScore(data);
			fail("No exception thrown");
		} catch (InputMissingException e) {
			// Okay, mail missing
		}
	}
}