package org.dice_research.opal.civet.metrics;

import java.util.LinkedList;
import java.util.List;

/**
 * Metrics catalogue.
 *
 * @author Adrian Wilke
 */
public class Metrics {

	public List<Metric> getMetricsExpressiveness() {
		List<Metric> metrics = new LinkedList<Metric>();
		metrics.add(new CategorizationMetric());
		metrics.add(new DescriptionMetric());
		return metrics;
	}

	public List<Metric> getMetricsContact() {
		List<Metric> metrics = new LinkedList<Metric>();
		metrics.add(new ContactEmailMetric());
		metrics.add(new ContactUrlMetric());
		return metrics;
	}

	public List<Metric> getMetrics() {
		List<Metric> metrics = new LinkedList<Metric>();
		metrics.addAll(getMetricsExpressiveness());
		metrics.addAll(getMetricsContact());
		return metrics;
	}
}
