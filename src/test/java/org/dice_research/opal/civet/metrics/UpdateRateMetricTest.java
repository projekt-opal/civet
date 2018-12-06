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
public class UpdateRateMetricTest {

	@Test
	public void test() throws NullPointerException, UnknownIdRuntimeException, ParsingException {

		DataContainer data = new DataContainer();

		data.putDataObject(new StringDataObject(DataObjects.DOWNLOAD_URL, "https://example.com/data1"));
		assertEquals(1, data.getDataObject(DataObjects.DOWNLOAD_URL).getValues().size(), 0);
		assertEquals(0, new UpdateRateMetric().getScore(data), 0);

		data.getDataObject(DataObjects.DOWNLOAD_URL).addValue("https://example.com/data2");
		assertEquals(2, data.getDataObject(DataObjects.DOWNLOAD_URL).getValues().size(), 0);
		assertEquals(5, new UpdateRateMetric().getScore(data), 0);

	}

}