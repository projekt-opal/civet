package org.dice_research.opal.civet;

import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;

import org.dice_research.opal.civet.metrics.CategorizationMetirc;
import org.dice_research.opal.civet.metrics.Metric;
import org.junit.Test;

public class MetricImplementationTest {

	@Test
	public void test() {

		// TODO: Automatically add all available metrics

		List<Metric> metrics = new LinkedList<Metric>();
		metrics.add(new CategorizationMetirc());

		for (Metric metric : metrics) {
			assertNotNull(metric.getDescription());
			assertNotNull(metric.getId());
			assertNotNull(metric.getType());
		}
	}

}
