package org.dice_research.opal.civet.metrics;

import java.util.HashMap;
import java.util.Map;

/**
 * Metrics catalog.
 *
 * @author Adrian Wilke
 */
public abstract class Metrics {

	/**
	 * Gets all metrics of the dimension expressiveness.
	 */
	public static Map<String, Metric> getMetricsExpressiveness() {
		Map<String, Metric> metrics = new HashMap<String, Metric>();
		putMetricIntoMap(new CategorizationMetric(), metrics);
		putMetricIntoMap(new DescriptionMetric(), metrics);
		return metrics;
	}

	/**
	 * Gets all metrics of the dimension temporal.
	 */
	public static Map<String, Metric> getMetricsTemporal() {
		Map<String, Metric> metrics = new HashMap<String, Metric>();
		putMetricIntoMap(new UpdateRateMetric(), metrics);
		return metrics;
	}

	/**
	 * Gets all metrics of the dimension rights.
	 */
	public static Map<String, Metric> getMetricsRights() {
		Map<String, Metric> metrics = new HashMap<String, Metric>();
		putMetricIntoMap(new LicenseSpecifiedMetric(), metrics);
		return metrics;
	}

	/**
	 * Gets all metrics.
	 */
	public static Map<String, Metric> getMetrics() {
		Map<String, Metric> metrics = new HashMap<String, Metric>();
		metrics.putAll(getMetricsExpressiveness());
		metrics.putAll(getMetricsTemporal());
		metrics.putAll(getMetricsRights());
		return metrics;
	}

	/**
	 * Puts ID of metric and the metric itself in the map.
	 */
	private static void putMetricIntoMap(Metric metric, Map<String, Metric> map) {
		map.put(metric.toString(), metric);
	}
}
