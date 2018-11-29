package org.dice_research.opal.civet;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dice_research.opal.civet.access.OpalAccessor;
import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.DataObjects;
import org.dice_research.opal.civet.metrics.Metric;
import org.dice_research.opal.civet.metrics.Metrics;

/**
 * Civet management
 */
public class Orchestration {

	protected String sparqlQueryEndpoint;

	/**
	 * Sets the endpoint for SPARQL queries.
	 * 
	 * @param endpoint a URL, e.g. http://example.com:8890/sparql
	 */
	public Orchestration setSparqlQueryEndpoint(String endpoint) {
		this.sparqlQueryEndpoint = endpoint;
		return this;
	}

	/**
	 * Computes metrics for a dataset.
	 * 
	 * @param dataset   the URI of the dataset
	 * @param metricIds a collection of metrics to compute
	 */
	public void compute(URI dataset, Collection<String> metricIds) {

		// Create metrics and set of required data object IDs
		Map<String, Metric> availableMetrics = new Metrics().getMetrics();
		Set<Metric> metrics = new HashSet<Metric>();
		Set<String> dataObjectIds = new HashSet<String>();
		for (String metricId : metricIds) {
			Metric metric = availableMetrics.get(metricId);
			metrics.add(metric);
			dataObjectIds.addAll(metric.getRequiredProperties());

		}

		// TODO get data.

		OpalAccessor opalAccessor = new OpalAccessor(this.sparqlQueryEndpoint).connect();
		DataContainer dataContainer = createDataContainer(dataObjectIds);
		opalAccessor.getData(dataset, dataContainer);

		// TODO compute metrics

		opalAccessor.close();
	}

	/**
	 * Creates new data container with data objects.
	 */
	protected DataContainer createDataContainer(Collection<String> dataObjectIds) {
		DataContainer dataContainer = new DataContainer();
		for (String id : dataObjectIds) {
			dataContainer.putDataObject(DataObjects.createDataObject(id));
		}
		return dataContainer;
	}
}