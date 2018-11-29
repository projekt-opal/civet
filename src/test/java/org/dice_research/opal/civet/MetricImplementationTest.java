package org.dice_research.opal.civet;

import static org.junit.Assert.assertNotNull;

import org.dice_research.opal.civet.metrics.Metric;
import org.dice_research.opal.civet.metrics.Metrics;
import org.junit.Test;

/**
 * Tests, if all mandatory values are set.
 *
 * @author Adrian Wilke
 */
public class MetricImplementationTest {

	@Test
	public void test() {

		for (Metric metric : Metrics.getMetrics().values()) {
			assertNotNull(metric.getDescription());
			assertNotNull(metric.getId());
			assertNotNull(metric.getType());
			assertNotNull(metric.getRequiredProperties());
		}
	}

}