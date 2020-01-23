package org.dice_research.opal.civet;

import org.apache.jena.rdf.model.Model;

/**
 * Interface for metric implementations.
 * 
 * @author Adrian Wilke
 */
public interface Metric {

	/**
	 * Computes a score betweed 0 (bad) and 5 (good) for the given dataset.
	 * 
	 * @param model      Jena input model
	 * @param datasetUri datasetUri URI of DCAT dataset to process
	 * @return A score in range [0, 5] or null if no score could be calculated
	 * @throws Exception On errors
	 */
	public Integer compute(Model model, String datasetUri) throws Exception;

	/**
	 * The metric description is presented to users. It should contain a short text
	 * about how scores are computed.
	 * 
	 * @return A description on how the metric is computed.
	 * @throws Exception On errors
	 */
	public String getDescription() throws Exception;

	/**
	 * Returns the URI of the metric. This is used to identify the metric type in
	 * RDF.
	 * 
	 * @return URI of metric
	 * @throws Exception On errors
	 */
	public String getUri() throws Exception;
}