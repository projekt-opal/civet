package org.dice_research.opal.civet.aggregation;

import static org.junit.Assert.assertEquals;

import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.IntegerDataObject;
import org.dice_research.opal.civet.metrics.ContactUrlMetric;
import org.dice_research.opal.civet.metrics.KnownLicenseMetric;
import org.dice_research.opal.civet.metrics.TimelinessMetric;
import org.junit.Test;

public class MetadataQualityAggregationTest {

	@Test
	public void test() {
		DataContainer data = new DataContainer();
		data.putDataObject(new IntegerDataObject(new ContactUrlMetric().getId(), 1));
		assertEquals(1, new MetadataQualityAggregation().getScore(data), 0);
		data.putDataObject(new IntegerDataObject(new TimelinessMetric().getId(), 3));
		assertEquals(2, new MetadataQualityAggregation().getScore(data), 0);
		data.putDataObject(new IntegerDataObject(new KnownLicenseMetric().getId(), 5));
		assertEquals(3, new MetadataQualityAggregation().getScore(data), 0);
	}

}
