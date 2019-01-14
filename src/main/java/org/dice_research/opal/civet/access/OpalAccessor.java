package org.dice_research.opal.civet.access;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.jena.arq.querybuilder.SelectBuilder;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.core.Var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Orchestration;
import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.DataObject;
import org.dice_research.opal.civet.data.DataObjects;
import org.dice_research.opal.civet.exceptions.ParsingException;
import org.dice_research.opal.civet.vocabulary.Dcat;
import org.dice_research.opal.civet.vocabulary.DublinCore;
import org.dice_research.opal.civet.vocabulary.Foaf;
import org.dice_research.opal.civet.vocabulary.Skos;

/**
 * Data accessor for OPAL SPARQL endpoint.
 * 
 * RDF graph data is accessed and written into data container.
 *
 * @author Adrian Wilke
 */
public class OpalAccessor extends SparqlEndpointAccessor {

	protected static final Logger LOGGER = LogManager.getLogger();
	protected static final String VAR_DATASET = "DATASET";
	protected static final String VAR_DISTRIBUTION = "DISTRIBUTION";

	protected Orchestration orchestration;

	public OpalAccessor(Orchestration orchestration) {
		super(orchestration.getConfiguration().getSparqlQueryEndpoint());
		this.orchestration = orchestration;
	}

	@Override
	public OpalAccessor connect() {
		super.connect();
		return this;
	}

	// TODO store data
	// TODO Dataset uri needed
	public void writeMetricResults(List<DataContainer> dataContainers) {

		// Ensure connection
//		if (!isConnected()) {
//			connect();
//		}
//
//		Map<String, Metric> availableMetrics = Metrics.getMetrics();
//
//		rdfConnection.begin(ReadWrite.WRITE);
//
//		for (DataContainer dataContainer : dataContainers) {
//
//			for (Entry<String, Float> metricResult : dataContainer.getMetricResults().entrySet()) {
//
//				Metric metric = availableMetrics.get(metricResult.getKey());
//				metric.getResultsUri();
//
//			}
//
//		}

// https://www.w3.org/TR/sparql11-update/#insertData	
//		PREFIX dc: <http://purl.org/dc/elements/1.1/>
//			INSERT DATA
//			{ 
//			  <http://example/book1> dc:title "A new book" ;
//			                         dc:creator "A.N.Other" .
//			}

//		rdfConnection.update("");
//		rdfConnection.commit();
//		rdfConnection.end();

		// Option 2
//		https://jena.apache.org/documentation/query/update.html
//		UpdateRequest updateRequest = new UpdateRequest();
//		UpdateAction.execute(updateRequest, rdfConnection);
//		UpdateFactory.

	}

	/**
	 * Gets data for several datasets.
	 * 
	 * @param dataContainer A data-container with pre-defined IDs
	 * @param limit         Number of items per request
	 * @param offset        Starting number
	 * @throws NullPointerException
	 */
	public OpalAccessorContainer getData(DataContainer dataContainer, int limit, int offset)
			throws NullPointerException {
		Map<String, DataContainer> dataContainers = new HashMap<>();

		// Ensure connection
		if (!isConnected()) {
			connect();
		}

		// Build query
		Triple initialWhereTriple = new Triple(NodeFactory.createVariable(VAR_DATASET),
				NodeFactory.createURI(org.apache.jena.vocabulary.RDF.type.getURI()),
				NodeFactory.createURI(Dcat.PROPERTY_DATASET));
		SelectBuilder selectBuilder = buildQuery(dataContainer, initialWhereTriple);
		selectBuilder.addVar(VAR_DATASET);
		selectBuilder.setLimit(limit);
		selectBuilder.setOffset(offset);

		// Execute query
		Query query = selectBuilder.build();
		LOGGER.debug(query.toString());
		QueryExecution queryExecution = rdfConnection.query(query);
		ResultSet resultSet = queryExecution.execSelect();

		// Process results (datasets may be splitted into multiple results)
		int resultIndex = -1;
		int refreshIndex = 0;
		String datasetUri = null;
		while (resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.next();
			resultIndex++;

			// Extract IDs and values of the current result
			// Iterator only returns variables with values (optional properties are skipped)
			Map<String, String> idToValue = new HashMap<>();
			Iterator<String> iterator = querySolution.varNames();
			while (iterator.hasNext()) {
				String id = iterator.next();
				idToValue.put(id, querySolution.get(id).toString().trim());
			}

			// Create or get data-container
			DataContainer resultsDataContainer;
			if (!idToValue.containsKey(VAR_DATASET)) {
				// Every result has to provide the URI of the current dataset
				LOGGER.error("Could not find dataset URI at result index: " + resultIndex);
				continue;
			} else {

				// Check change of URI
				if (!idToValue.get(VAR_DATASET).equals(datasetUri)) {
					// Remember the index of the last dataset URI in results.
					// Used to re-request that dataset in next iteration and to ensure completeness
					// of requested data. This could be solved with other types of SPARQL queries.
					// However, the current request type was fastest in tests.
					refreshIndex = resultIndex;
				}

				datasetUri = idToValue.get(VAR_DATASET);
				if (dataContainers.containsKey(datasetUri)) {
					resultsDataContainer = dataContainers.get(datasetUri);
				} else {
					try {
						resultsDataContainer = DataContainer.create(dataContainer);
					} catch (IOException e) {
						LOGGER.error("Could not create new data container.", e);
						continue;
					}
				}
			}

			// Update data-objects in data-container
			for (String id : idToValue.keySet()) {
				if (id.equals(VAR_DATASET)) {
					continue;
				} else {
					try {
						resultsDataContainer.getDataObject(id).addValueUnique(idToValue.get(id));
					} catch (ParsingException e) {
						LOGGER.error(e);
					}
				}
			}

			dataContainers.put(datasetUri, resultsDataContainer);
		}

		// Update number of categories
		for (DataContainer dc : dataContainers.values()) {
			if (dc.getIds().contains(DataObjects.NUMBER_OF_CATEGORIES)) {
				try {
					dc.getDataObject(DataObjects.NUMBER_OF_CATEGORIES)
							.setValue("" + dc.getDataObject(DataObjects.THEME).getValues().size());
				} catch (ParsingException e) {
					LOGGER.error(e);
				}
			}
		}

		// Check completeness
		if (refreshIndex == 0) {
			// The complete results contain data for exactly one resource. Therefore,
			// requests with the given limit are not able to fetch this dataset completely.
			LOGGER.warn("Completeness of SPARQL request is maybe not given. Offset: " + offset);
		}

		// Put results into container
		OpalAccessorContainer resultsContainer = new OpalAccessorContainer();
		resultsContainer.dataContainers = dataContainers;
		resultsContainer.refreshIndex = refreshIndex;

		// Get related distribution data
		getDistributionData(dataContainer, resultsContainer);

		return resultsContainer;
	}

	public void getData(URI datasetUri, DataContainer dataContainer) {

		// TODO: Check dataset with multiple categories

		// Ensure connection
		if (!isConnected()) {
			connect();
		}

		// Add query parts for single data-objects
		SelectBuilder selectBuilder = buildQuery(dataContainer, null);

		// Only one dataset has to be returned
		selectBuilder.setVar(Var.alloc(VAR_DATASET), "<" + datasetUri.toString() + ">");

		// Execute query
		Query query = selectBuilder.build();
		LOGGER.debug(query.toString());
		QueryExecution queryExecution = rdfConnection.query(query);
		ResultSet resultSet = queryExecution.execSelect();

		// Process results
		int categories = 0;
		if (resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.next();

			// Iterator only returns variables with values (optional properties are skipped)
			Iterator<String> iterator = querySolution.varNames();
			while (iterator.hasNext()) {
				String id = iterator.next();
				try {
					if (id.equals(DataObjects.THEME)) {
						categories++;
					}
					dataContainer.getDataObject(id).addValue(querySolution.get(id).toString().trim());
				} catch (ParsingException e) {
					LOGGER.error(e);
				}
			}
		}
		try {
			if (dataContainer.getIds().contains(DataObjects.NUMBER_OF_CATEGORIES))
				dataContainer.getDataObject(DataObjects.NUMBER_OF_CATEGORIES).addValue("" + categories);
		} catch (ParsingException e) {
			LOGGER.error(e);
		}
		if (resultSet.hasNext()) {
			LOGGER.debug("More than one result returned.");
		}

		getDistributionData(datasetUri, dataContainer);
	}

	/**
	 * Adds query parts for single data-objects.
	 */
	private SelectBuilder buildQuery(DataContainer dataContainer, Triple initialWhereTriple) {

		// Build query
		SelectBuilder selectBuilder = new SelectBuilder();
		selectBuilder.setDistinct(true);

		// Use named graph or default graph
		if (orchestration.getConfiguration().getNamedGraph() != null) {
			selectBuilder.from(orchestration.getConfiguration().getNamedGraph());
		}

		if (initialWhereTriple != null) {
			selectBuilder.addWhere(initialWhereTriple);
		}

		// Add query parts
		for (DataObject<?> dataObject : dataContainer.getDataObjects()) {

			if (addDatasetRelation(selectBuilder, dataObject.getId(), DataObjects.DESCRIPTION,
					DublinCore.PROPERTY_DESCRIPTION))
				continue;

			if (addDatasetRelation(selectBuilder, dataObject.getId(), DataObjects.ISSUED, DublinCore.PROPERTY_ISSUED))
				continue;

			if (addDatasetRelation(selectBuilder, dataObject.getId(), DataObjects.TITLE, DublinCore.PROPERTY_TITLE))
				continue;

			if (dataObject.getId().equals(DataObjects.PUBLISHER)) {
				Node foafAgent = NodeFactory.createVariable("foafAgent");
				selectBuilder.addVar(DataObjects.PUBLISHER)
						.addOptional("?" + VAR_DATASET, NodeFactory.createURI(DublinCore.PROPERTY_PUBLISHER), foafAgent)
						.addOptional(foafAgent, NodeFactory.createURI(Foaf.PROPERTY_NAME),
								NodeFactory.createVariable(DataObjects.PUBLISHER));
			}

			if (dataObject.getId().equals(DataObjects.THEME)) {
				Node skosConcept = NodeFactory.createVariable("skosConcept");
				selectBuilder.addVar(DataObjects.THEME)
						.addOptional("?" + VAR_DATASET, NodeFactory.createURI(Dcat.PROPERTY_THEME), skosConcept)
						.addOptional(skosConcept, NodeFactory.createURI(Skos.PROPERTY_PREF_LABEL),
								NodeFactory.createVariable(DataObjects.THEME));
			}
		}

		return selectBuilder;
	}

	private void getDistributionData(DataContainer dataContainer, OpalAccessorContainer resultsContainer) {

		// Build query
		SelectBuilder selectBuilder = new SelectBuilder();
		selectBuilder.setDistinct(true);
		selectBuilder.addVar(VAR_DATASET);

		// Use named graph or default graph
		if (orchestration.getConfiguration().getNamedGraph() != null) {
			selectBuilder.from(orchestration.getConfiguration().getNamedGraph());
		}

		selectBuilder.addWhere("?" + VAR_DATASET, NodeFactory.createURI(Dcat.PROPERTY_DISTRIBUTION),
				"?" + VAR_DISTRIBUTION);
		for (DataObject<?> dataObject : dataContainer.getDataObjects()) {

			if (addDistributionRelation(selectBuilder, dataObject.getId(), DataObjects.ACCESS_URL,
					Dcat.PROPERTY_ACCESS_URL))
				continue;

			if (addDistributionRelation(selectBuilder, dataObject.getId(), DataObjects.DOWNLOAD_URL,
					Dcat.PROPERTY_DOWNLOAD_URL))
				continue;

			if (addDistributionRelation(selectBuilder, dataObject.getId(), DataObjects.LICENSE,
					DublinCore.PROPERTY_LICENSE))
				continue;
		}

		for (String datasetUri : resultsContainer.dataContainers.keySet()) {
			selectBuilder.addValueVar(Var.alloc(VAR_DATASET), "<" + datasetUri + ">");
		}

		// Execute query
		Query query = selectBuilder.build();

		LOGGER.debug(query.toString());
		QueryExecution queryExecution = rdfConnection.query(query);
		ResultSet resultSet = queryExecution.execSelect();

		// Process results
		Map<String, Set<String>> data = new HashMap<>();
		while (resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.next();
			String dataset = null;

			// Collect data in result
			Iterator<String> iterator = querySolution.varNames();
			while (iterator.hasNext()) {
				String id = iterator.next();
				if (id.equals(VAR_DISTRIBUTION)) {
					// variable just used to access data
					continue;
				} else if (id.equals(VAR_DATASET)) {
					dataset = querySolution.get(id).toString().trim();
					continue;
				}
				if (!data.containsKey(id)) {
					data.put(id, new HashSet<String>());
				}
				data.get(id).add(querySolution.get(id).toString().trim());
			}

			// Put data in container
			if (dataset != null) {
				DataContainer container = resultsContainer.dataContainers.get(dataset);
				for (String id : data.keySet()) {
					for (String value : data.get(id)) {
						try {
							container.getDataObject(id).addValueUnique(value);
						} catch (ParsingException e) {
							LOGGER.error(e);
						}
					}
				}
			}
		}
	}

	private void getDistributionData(URI datasetUri, DataContainer dataContainer) {

		// Build query
		SelectBuilder selectBuilder = new SelectBuilder();

		// Use named graph or default graph
		if (orchestration.getConfiguration().getNamedGraph() != null) {
			selectBuilder.from(orchestration.getConfiguration().getNamedGraph());
		}

		selectBuilder.addWhere("?" + VAR_DATASET, NodeFactory.createURI(Dcat.PROPERTY_DISTRIBUTION),
				"?" + VAR_DISTRIBUTION);
		for (DataObject<?> dataObject : dataContainer.getDataObjects()) {

			if (addDistributionRelation(selectBuilder, dataObject.getId(), DataObjects.ACCESS_URL,
					Dcat.PROPERTY_ACCESS_URL))
				continue;

			if (addDistributionRelation(selectBuilder, dataObject.getId(), DataObjects.DOWNLOAD_URL,
					Dcat.PROPERTY_DOWNLOAD_URL))
				continue;

			if (addDistributionRelation(selectBuilder, dataObject.getId(), DataObjects.LICENSE,
					DublinCore.PROPERTY_LICENSE))
				continue;
		}
		selectBuilder.setVar(Var.alloc(VAR_DATASET), "<" + datasetUri.toString() + ">");

		// Execute query
		Query query = selectBuilder.build();
		LOGGER.debug(query.toString());
		QueryExecution queryExecution = rdfConnection.query(query);
		ResultSet resultSet = queryExecution.execSelect();

		// Deduplication
		Map<String, Set<String>> data = new HashMap<>();

		// Process results
		while (resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.next();

			// Iterator only returns variables with values (optional properties are skipped)
			Iterator<String> iterator = querySolution.varNames();
			while (iterator.hasNext()) {
				String id = iterator.next();
				if (id.equals(VAR_DISTRIBUTION)) {
					// variable just used to access data
					continue;
				}
				if (!data.containsKey(id)) {
					data.put(id, new HashSet<String>());
				}
				data.get(id).add(querySolution.get(id).toString().trim());
			}
		}

		// Put data in container
		for (Entry<String, Set<String>> entry : data.entrySet()) {
			for (String value : entry.getValue()) {
				try {
					dataContainer.getDataObject(entry.getKey()).addValue(value);
				} catch (ParsingException e) {
					LOGGER.error(e);
				}
			}
		}
	}

	private boolean addDatasetRelation(SelectBuilder selectBuilder, String dataObjectIdActual,
			String dataObjectIdExpected, String predicate) {
		if (dataObjectIdActual.equals(dataObjectIdExpected)) {
			selectBuilder.addVar(dataObjectIdExpected).addOptional("?" + VAR_DATASET, NodeFactory.createURI(predicate),
					NodeFactory.createVariable(dataObjectIdExpected));
			return true;
		} else {
			return false;
		}
	}

	private boolean addDistributionRelation(SelectBuilder selectBuilder, String dataObjectIdActual,
			String dataObjectIdExpected, String predicate) {
		if (dataObjectIdActual.equals(dataObjectIdExpected)) {
			selectBuilder.addVar(dataObjectIdExpected).addOptional("?" + VAR_DISTRIBUTION,
					NodeFactory.createURI(predicate), NodeFactory.createVariable(dataObjectIdExpected));
			return true;
		} else {
			return false;
		}
	}

}