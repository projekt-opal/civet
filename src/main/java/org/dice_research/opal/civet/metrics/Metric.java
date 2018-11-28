package org.dice_research.opal.civet.metrics;

import java.util.Collection;

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
	 * The respective required properties for a metric are set in the concrete
	 * implementation.
	 */
	protected Collection<String> requiredProperties;

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
	 * Returns list of required properties
	 */
	public Collection<String> getRequiredProperties() {
		return this.requiredProperties;
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

	/**
	 * Gets simple class name of metric.
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}