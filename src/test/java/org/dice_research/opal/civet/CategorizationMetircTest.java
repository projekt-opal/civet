package org.dice_research.opal.civet;

import static org.junit.Assert.*;

import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.InputType;
import org.dice_research.opal.civet.exceptions.InputMissingException;
import org.dice_research.opal.civet.metrics.CategorizationMetirc;
import org.dice_research.opal.civet.metrics.Metric;
import org.junit.Test;

public class CategorizationMetircTest {

	@Test
	public void test() throws InputMissingException {

		DataContainer data = new DataContainer();
		data.setInput(InputType.NUMBER_OF_CATEGORIES, 3);
		data.setInput(InputType.AVERAGE_NUMBER_OF_CATEGORIES, 3);

		Metric metric = new CategorizationMetirc();
		assertEquals(0.5, metric.getScore(data), 0);

	}
}