package org.dice_research.opal.civet;

import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.metrics.CategorizationMetric;
import org.dice_research.opal.civet.metrics.DataFormatMetric;
import org.dice_research.opal.civet.metrics.LicenseAvailabilityMetric;
import org.dice_research.opal.civet.metrics.MetadataQualityMetric;
import org.dice_research.opal.civet.metrics.MultipleSerializationsMetric;
import org.dice_research.opal.civet.metrics.UpdateRateMetric;
import org.dice_research.opal.common.interfaces.JenaModelProcessor;
import org.dice_research.opal.common.interfaces.ModelProcessor;

/**
 * Civet - OPAL quality metric component.
 * 
 * This component calculates scores (measurements) of metadata quality metrics.
 * 
 * The Data Quality Vocabulary (DQV) is used to describe the resulting data.
 * 
 * Existing measurements will be removed before computing new measurements by
 * default. To change that, use {@link #removeMeasurements}.
 *
 * @see https://www.w3.org/TR/vocab-dqv/
 *
 * @author Adrian Wilke
 */
@SuppressWarnings("deprecation")
public class Civet implements ModelProcessor, JenaModelProcessor {

	private static final Logger LOGGER = LogManager.getLogger();

	private boolean removeMeasurements = true;
	private boolean logNotComputed = true;

	/**
	 * Computes quality metric scores (measurements).
	 * 
	 * Existing measurements will be removed before computing new measurements by
	 * default. To change that, use {@link #removeMeasurements}.
	 */
	@Override
	public void processModel(Model model, String datasetUri) throws Exception {

		Resource dataset = ResourceFactory.createResource(datasetUri);

		LOGGER.info("Processing dataset " + datasetUri);

		// Remove existing measurements
		if (removeMeasurements) {
			Utils.removeAllMeasurements(model, dataset);
		}

		// Compute and add new measurements
		for (Metric metric : getMetrics()) {
			Integer score = null;

			try {
				score = metric.compute(model, datasetUri);
			} catch (Exception e) {
				LOGGER.error("Exception on computing " + metric.getUri() + " for " + datasetUri, e);
				continue;
			}

			if (score == null) {
				if (logNotComputed) {
					LOGGER.info("No result for metric " + metric.getUri() + " and dataset " + datasetUri);
				}
			} else {
				model.add(
						Utils.createMetricStatements(dataset, ResourceFactory.createResource(metric.getUri()), score));
			}

		}
	}

	/**
	 * @deprecated Replaced by {@link #processModel(Model, String)}.
	 */
	@Deprecated
	@Override
	public Model process(Model model, String datasetUri) throws Exception {
		processModel(model, datasetUri);
		return model;
	}

	/**
	 * Gets list of available metrics.
	 */
	public List<Metric> getMetrics() {
		List<Metric> metrics = new LinkedList<Metric>();

		metrics.add(new CategorizationMetric());
		metrics.add(new DataFormatMetric());
		metrics.add(new LicenseAvailabilityMetric());
		metrics.add(new MultipleSerializationsMetric());
		metrics.add(new UpdateRateMetric());

		// Has to be last metric as it aggregates
		metrics.add(new MetadataQualityMetric());

		return metrics;
	}

	/**
	 * Gets setting, if existing measurements will be removed.
	 */
	public boolean isRemovingMeasurements() {
		return removeMeasurements;
	}

	/**
	 * Sets, if existing measurements will be removed.
	 */
	public Civet setRemoveMeasurements(boolean removeMeasurements) {
		this.removeMeasurements = removeMeasurements;
		return this;
	}

	/**
	 * Gets setting, if it is logged, when a measurement could not be computed.
	 */
	public boolean isLogNotComputed() {
		return logNotComputed;
	}

	/**
	 * Sets, if it should be logged, when a measurement could not be computed.
	 */
	public void setLogNotComputed(boolean logNotComputed) {
		this.logNotComputed = logNotComputed;
	}
}