package org.dice_research.opal.civet;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
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
			opalAccessor = new OpalAccessor(this).connectQueryEndpoint();
		}
		DataContainer dataContainer = createDataContainer(dataObjectIds);
		try {
			opalAccessor.getData(dataset, dataContainer);
		} catch (SparqlEndpointRuntimeException e) {
			LOGGER.error("Not connected to SPARQL endpoint.", e);
		}

		// Calculate
		dataContainer.calculateMetrics(metricIds);

		// Return
		Map<String, Float> metricResults = new HashMap<>();
		for (Entry<Metric, Float> metricResult : dataContainer.getMetricResults().entrySet()) {
			metricResults.put(metricResult.getKey().getId(), metricResult.getValue());
		}
		return metricResults;
	}

	/**
	 * Computes metrics for multiple datasets. Writes results back to graph.
	 * 
	 * Note: limit should be smaller than 1000, as
	 * {@link OpalAccessor#writeMetricResults(Map)} tends to throw
	 * {@link StackOverflowError}.
	 * 
	 * @param offset    Starting number (number of results, not datasets)
	 * @param endOffset Ending number (number of results, not datasets). Use -1 to
	 *                  process all results.
	 * @param limit     Number of items per request
	 * @param metricIds a collection of metrics to compute
	 */
	public void compute(int offset, int endOffset, int limit, Collection<String> metricIds) {

		// Get required data object IDs
		Set<String> dataObjectIds = getDataobjectIds(metricIds);

		// Prepare request
		if (opalAccessor == null) {
			opalAccessor = new OpalAccessor(this).connectQueryEndpoint();
		}
		DataContainer dataContainer = createDataContainer(dataObjectIds);

		// Process as long as end is not reached (or no end defined) AND
		// results have received in last iteration
		int numberOfResults = -1;
		int loopOffset = offset;
		while ((loopOffset < endOffset || endOffset == -1) && numberOfResults != 0) {

			// Get data
			OpalAccessorContainer resultsContainer = opalAccessor.getData(dataContainer, limit, loopOffset);

			// Calculate metrics
			for (DataContainer container : resultsContainer.dataContainers.values()) {
				container.calculateMetrics(metricIds);
			}

			// Write back
			opalAccessor.writeMetricResults(resultsContainer.dataContainers);

			// Prepare next iteration
			int repeat = limit - resultsContainer.refreshIndex;
			int newLoopOffset = loopOffset + limit - repeat;
			numberOfResults = resultsContainer.dataContainers.size();

			// Prevent repeated queries, if limit would produce requesting the same results
			if (newLoopOffset == loopOffset) {
				loopOffset = newLoopOffset + limit;
				LOGGER.error("Preventing infinitive loop at offset " + newLoopOffset);
			} else {
				loopOffset = newLoopOffset;
			}
		}
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