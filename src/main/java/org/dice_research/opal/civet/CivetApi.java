package org.dice_research.opal.civet;

import java.net.URI;
import java.util.Collection;
import java.util.Map;

import org.dice_research.opal.civet.metrics.Metrics;

/**
 * API of Civet - the quality framework of the Open Data Portal Germany (OPAL).
 * 
 * @author Adrian Wilke
 */
public class CivetApi {

	private Orchestration orchestration = new Orchestration();

	/**
	 * Sets the endpoint for SPARQL queries.
	 * 
	 * @param endpoint a URL, e.g. http://example.com:8890/sparql
	 * 
	 * @throws NullPointerException if endpoint is null
	 */
	public CivetApi setSparqlQueryEndpoint(String endpoint) throws NullPointerException {
		if (endpoint == null) {
			throw new NullPointerException("No SPARQL query endpoint specified.");
		}
		this.orchestration.getConfiguration().setSparqlQueryEndpoint(endpoint);
		return this;
	}

	/**
	 * Sets a named graph for data access.
	 * 
	 * @param namedGraph Name of the graph or null, if default graph has to be used
	 */
	public CivetApi setNamedGraph(String namedGraph) {
		this.orchestration.getConfiguration().setNamedGraph(namedGraph);
		return this;
	}

	/**
	 * Gets IDs of all quality metrics.
	 */
	public Collection<String> getAllMetricIds() {
		return Metrics.getMetrics().keySet();
	}

	/**
	 * Computes metrics for a dataset.
	 * 
	 * Available metric IDs can be accessed by {@link #getAllMetricIds()}.
	 * 
	 * @param dataset the URI of the dataset
	 * @param metrics the metric IDs to compute
	 * @return metric IDs mapped to the resulting scores
	 * 
	 * @throws IllegalArgumentException if the metrics parameter is empty
	 * @throws NullPointerException     if one of the parameters is null or the
	 *                                  SPARQL query endpoint was not set
	 */
	public Map<String, Float> compute(URI dataset, Collection<String> metrics)
			throws NullPointerException, IllegalArgumentException {
		if (this.orchestration.getConfiguration().getSparqlQueryEndpoint() == null) {
			throw new NullPointerException("No SPARQL query endpoint specified.");
		} else if (dataset == null) {
			throw new NullPointerException("No dataset URI specified.");
		} else if (metrics == null) {
			throw new NullPointerException("No metrics specified.");
		} else if (metrics.isEmpty()) {
			throw new IllegalArgumentException("No metrics specified.");
		}

		return this.orchestration.compute(dataset, metrics);
	}
}