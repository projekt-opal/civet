package org.dice_research.opal.civet;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.access.OpalAccessor;
import org.dice_research.opal.civet.access.OpalAccessorContainer;
import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.DataObjects;
import org.dice_research.opal.civet.exceptions.SparqlEndpointRuntimeException;
import org.dice_research.opal.civet.metrics.Metric;
import org.dice_research.opal.civet.metrics.Metrics;

/**
 * Civet management
 * 
 * @author Adrian Wilke
 */
public class Orchestration {

	protected static final Logger LOGGER = LogManager.getLogger();
	protected Configuration configuration = new Configuration();
	protected OpalAccessor opalAccessor;

	/**
	 * Computes metrics for a dataset.
	 * 
	 * @param dataset   the URI of the dataset
	 * @param metricIds a collection of metrics to compute
	 * @return
	 */
	public Map<String, Float> compute(URI dataset, Collection<String> metricIds) {

		// Get required data object IDs
		Set<String> dataObjectIds = getDataobjectIds(metricIds);

		// Get data
		if (opalAccessor == null) {
			opalAccessor = new OpalAccessor(this).connect();
		}
		DataContainer dataContainer = createDataContainer(dataObjectIds);
		try {
			opalAccessor.getData(dataset, dataContainer);
		} catch (SparqlEndpointRuntimeException e) {
			LOGGER.error("Not connected to SPARQL endpoint.", e);
		}

		dataContainer.calculateMetrics(metricIds);
		return dataContainer.getMetricResults();
	}

	/**
	 * Computes metrics for multiple datasets. Writes results back to graph.
	 * 
	 * @param offset    Starting number
	 * @param limit     Number of items per request
	 * @param min       Minimum number of datasets to receive
	 * @param metricIds a collection of metrics to compute
	 * 
	 * @return Number of processed datasets
	 */
	public int compute(int offset, int limit, int min, Collection<String> metricIds) {

		// Get required data object IDs
		Set<String> dataObjectIds = getDataobjectIds(metricIds);

		// Prepare request
		if (opalAccessor == null) {
			opalAccessor = new OpalAccessor(this).connect();
		}
		DataContainer dataContainer = createDataContainer(dataObjectIds);

		// Process
		while (offset < min) {

			// Get data
			OpalAccessorContainer resultsContainer = opalAccessor.getData(dataContainer, limit, offset);

			// Calculate metrics
			for (Entry<String, DataContainer> entry : resultsContainer.dataContainers.entrySet()) {
				entry.getValue().calculateMetrics(metricIds);
			}

			// Write back
			// TODO

			// Prepare next iteration
			int repeat = limit - resultsContainer.refreshIndex;
			offset = offset + limit - repeat;
		}

		return offset - 1;
	}

	/**
	 * Closes OPAL accessor endpoint connection.
	 */
	public void close() {
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

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Gets required data-object IDs for given metrics.
	 */
	private Set<String> getDataobjectIds(Collection<String> metricIds) {
		Set<String> dataObjectIds = new HashSet<String>();
		Map<String, Metric> availableMetrics = Metrics.getMetrics();
		for (String metricId : metricIds) {
			dataObjectIds.addAll(availableMetrics.get(metricId).getRequiredProperties());
		}
		return dataObjectIds;
	}
}