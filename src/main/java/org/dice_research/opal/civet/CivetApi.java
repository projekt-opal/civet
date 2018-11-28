package org.dice_research.opal.civet;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

import org.dice_research.opal.civet.metrics.Metrics;

/**
 * API of Civet - the quality framework of the Open Data Portal Germany (OPAL).
 * 
 * @author Adrian Wilke
 */
public class CivetApi {

	protected Orchestration orchestration;
	protected String sparqlQueryEndpoint;

	/**
	 * Sets the endpoint for SPARQL queries.
	 * 
	 * @param endpoint
	 *            a URL, e.g. http://example.com:8890/sparql
	 */
	public CivetApi setSparqlQueryEndpoint(String endpoint) {
		this.sparqlQueryEndpoint = endpoint;
		return this;
	}

	/**
	 * Gets IDs of all quality metrics.
	 */
	public Collection<String> getAllMetrics() {
		return new Metrics().getMetrics().keySet();
	}

	/**
	 * Computes metrics for a dataset.
	 * 
	 * @param dataset
	 *            the URI of the dataset
	 * @param metrics
	 *            a collection of metrics to compute
	 * 
	 * @throws IOException
	 *             if the metrics parameter is empty
	 * @throws NullPointerException
	 *             if one of the parameters is null or the SPARQL query endpoint was
	 *             not set
	 */
	public void compute(URI dataset, Collection<String> metrics) throws RuntimeException, IOException {
		if (this.sparqlQueryEndpoint == null) {
			throw new NullPointerException("No SPARQL query endpoint specified.");
		} else if (dataset == null) {
			throw new NullPointerException("No dataset URI specified.");
		} else if (metrics == null) {
			throw new NullPointerException("No metrics specified.");
		} else if (metrics.isEmpty()) {
			throw new IOException("No metrics specified.");
		}

		if (this.orchestration == null) {
			this.orchestration = new Orchestration();
			this.orchestration.setSparqlQueryEndpoint(this.sparqlQueryEndpoint);
		}

		// TODO: Compute and return appropriate values.
		this.orchestration.compute(dataset, metrics);
	}
}