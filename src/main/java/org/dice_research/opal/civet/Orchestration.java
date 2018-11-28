package org.dice_research.opal.civet;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dice_research.opal.civet.access.SparqlEndpointAccessor;
import org.dice_research.opal.civet.metrics.Metric;
import org.dice_research.opal.civet.metrics.Metrics;

/**
 * TODO: implement
 */
public class Orchestration {

	protected String sparqlQueryEndpoint;
	protected SparqlEndpointAccessor sparqlEndpointAccessor;

	/**
	 * Sets the endpoint for SPARQL queries.
	 * 
	 * @param endpoint
	 *            a URL, e.g. http://example.com:8890/sparql
	 */
	public Orchestration setSparqlQueryEndpoint(String endpoint) {
		this.sparqlQueryEndpoint = endpoint;
		return this;
	}

	/**
	 * Computes metrics for a dataset.
	 * 
	 * @param dataset
	 *            the URI of the dataset
	 * @param metricIds
	 *            a collection of metrics to compute
	 */
	public void compute(URI dataset, Collection<String> metricIds) {

		// Get all required properties
		Map<String, Metric> availableMetrics = new Metrics().getMetrics();
		Set<Metric> metrics = new HashSet<Metric>();
		Set<String> requiredProperties = new HashSet<String>();
		for (String metricId : metricIds) {
			Metric metric = availableMetrics.get(metricId);
			metrics.add(metric);
			requiredProperties.addAll(metric.getRequiredProperties());
		}

		this.sparqlEndpointAccessor = new SparqlEndpointAccessor(this.sparqlQueryEndpoint);

		// TODO get data. use sparqlEndpointAccessor

		// TODO compute metrics
	}

}