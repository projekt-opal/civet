package org.dice_research.opal.civet;

import java.net.URI;
import java.util.Collection;
import java.util.Map;

import org.apache.jena.rdfconnection.RDFConnection;
import org.dice_research.opal.civet.metrics.Metrics;

/**
 * API of Civet - the quality framework of the Open Data Portal Germany (OPAL).
 * 
 * @author Adrian Wilke
 */
public class CivetApi {

	private Orchestration orchestration;
	private String sparqlQueryEndpoint;

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
		this.sparqlQueryEndpoint = endpoint;
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
	 * @param dataset the URI of the dataset
	 * @param metrics a collection of metrics to compute
	 * @return
	 * 
	 * @throws IllegalArgumentException if the metrics parameter is empty
	 * @throws NullPointerException     if one of the parameters is null or the
	 *                                  SPARQL query endpoint was not set
	 */
	public Map<String, Float> compute(URI dataset, Collection<String> metrics)
			throws NullPointerException, IllegalArgumentException {
		if (this.sparqlQueryEndpoint == null) {
			throw new NullPointerException("No SPARQL query endpoint specified.");
		} else if (dataset == null) {
			throw new NullPointerException("No dataset URI specified.");
		} else if (metrics == null) {
			throw new NullPointerException("No metrics specified.");
		} else if (metrics.isEmpty()) {
			throw new IllegalArgumentException("No metrics specified.");
		}

		if (this.orchestration == null) {
			this.orchestration = new Orchestration();
			this.orchestration.setSparqlQueryEndpoint(this.sparqlQueryEndpoint);
		}

		return this.orchestration.compute(dataset, metrics);
	}

	/**
	 * Gets RDF connection or null, if not set.
	 */
	public RDFConnection getRdfConnection() {
		if (orchestration == null) {
			return null;
		} else {
			return orchestration.getRdfConnection();
		}
	}

	/**
	 * Sets RDF connection.
	 */
	public CivetApi setRdfConnection(RDFConnection rdfConnection) {
		this.orchestration.setRdfConnection(rdfConnection);
		return this;
	}
}