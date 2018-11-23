package org.dice_research.opal.civet.metrics;

import org.dice_research.opal.civet.data.DataContainer;

/**
 * Computes metadata quality metric.
 *
 * @author Adrian Wilke
 */
public abstract class Metric {

	/**
	 * The respective description of a metric is set in the concrete implementation.
	 */
	protected String description;

	/**
	 * The respective ID of a metric is set in the concrete implementation.
	 */
	protected String id;

	/**
	 * The respective type of a metric is set in the concrete implementation.
	 */
	protected MetricType metricType;

	/**
	 * Returns description of the metric.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Returns the ID of the metric
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Computes and returns score of metric.
	 */
	public abstract float getScore(DataContainer dataContainer);

	/**
	 * Returns type of metric.
	 */
	public MetricType getType() {
		return this.metricType;
	}
}