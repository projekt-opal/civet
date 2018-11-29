package org.dice_research.opal.civet;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.access.OpalAccessor;
import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.DataObjects;
import org.dice_research.opal.civet.exceptions.SparqlEndpointRuntimeException;
import org.dice_research.opal.civet.metrics.Metric;
import org.dice_research.opal.civet.metrics.Metrics;

/**
 * Civet management
 */
public class Orchestration {

	protected static final Logger LOGGER = LogManager.getLogger();
	protected OpalAccessor opalAccessor;
	protected String sparqlQueryEndpoint;

	/**
	 * Sets the endpoint for SPARQL queries.
	 * 
	 * @param endpoint a URL, e.g. http://example.com:8890/sparql
	 * 
	 * @throws NullPointerException if endpoint is null
	 */
	public Orchestration setSparqlQueryEndpoint(String endpoint) throws NullPointerException {
		if (endpoint == null) {
			throw new NullPointerException("No SPARQL query endpoint specified.");
		}
		this.sparqlQueryEndpoint = endpoint;
		return this;
	}

	/**
	 * Computes metrics for a dataset.
	 * 
	 * @param dataset   the URI of the dataset
	 * @param metricIds a collection of metrics to compute
	 * @return
	 */
	public Map<String, Float> compute(URI dataset, Collection<String> metricIds) {

		// Create metrics and set of required data object IDs
		Set<String> dataObjectIds = new HashSet<String>();
		Set<Metric> metrics = new HashSet<Metric>();
		Map<String, Metric> availableMetrics = Metrics.getMetrics();
		for (String metricId : metricIds) {
			Metric metric = availableMetrics.get(metricId);
			metrics.add(metric);
			dataObjectIds.addAll(metric.getRequiredProperties());
		}

		// Get data
		if (opalAccessor == null) {
			opalAccessor = new OpalAccessor(this.sparqlQueryEndpoint).connect();
		}
		DataContainer dataContainer = createDataContainer(dataObjectIds);
		try {
			opalAccessor.getData(dataset, dataContainer);
		} catch (SparqlEndpointRuntimeException e) {
			LOGGER.error("Not connected to SPARQL endpoint.", e);
		}

		// Compute metrics
		Map<String, Float> scores = new HashMap<String, Float>();
		for (Metric metric : metrics) {
			scores.put(metric.getId(), metric.getScore(dataContainer));
		}

		return scores;
	}

	/**
	 * Closes OPAL accessor endpoint connection.
	 */
	public void close() {
		opalAccessor.close();
	}

	/**
	 * Gets RDF connection or null, if not set.
	 */
	public RDFConnection getRdfConnection() {
		if (opalAccessor == null) {
			return null;
		} else {
			return opalAccessor.getRdfConnection();
		}
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

	/**
	 * Sets RDF connection.
	 */
	public Orchestration setRdfConnection(RDFConnection rdfConnection) {
		this.opalAccessor.setRdfConnection(rdfConnection);
		return this;
	}
}