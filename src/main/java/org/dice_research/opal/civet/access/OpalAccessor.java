package org.dice_research.opal.civet.access;

import java.net.URI;
import java.util.Iterator;

import org.apache.jena.arq.querybuilder.SelectBuilder;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.core.Var;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.DataObject;
import org.dice_research.opal.civet.data.DataObjects;
import org.dice_research.opal.civet.exceptions.ParsingException;
import org.dice_research.opal.civet.vocabulary.Dcat;
import org.dice_research.opal.civet.vocabulary.DublinCore;
import org.dice_research.opal.civet.vocabulary.Foaf;
import org.dice_research.opal.civet.vocabulary.Skos;

public class OpalAccessor extends SparqlEndpointAccessor {

	protected static final Logger LOGGER = LogManager.getLogger();

	public OpalAccessor(String endpoint) {
		super(endpoint);
	}

	@Override
	public OpalAccessor connect() {
		super.connect();
		return this;
	}

	public void getData(URI datasetUri, DataContainer dataContainer) {

		// Ensure connection
		if (!isConnected()) {
			connect();
		}

		// Build query
		SelectBuilder selectBuilder = new SelectBuilder();
		for (DataObject<?> dataObject : dataContainer.getDataObjects()) {

			// Dataset properties

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
						.addOptional("?dataset", NodeFactory.createURI(DublinCore.PROPERTY_PUBLISHER), foafAgent)
						.addOptional(foafAgent, NodeFactory.createURI(Foaf.PROPERTY_NAME),
								NodeFactory.createVariable(DataObjects.PUBLISHER));
			}

			if (dataObject.getId().equals(DataObjects.THEME)) {
				Node skosConcept = NodeFactory.createVariable("skosConcept");
				selectBuilder.addVar(DataObjects.THEME)
						.addOptional("?dataset", NodeFactory.createURI(Dcat.PROPERTY_THEME), skosConcept)
						.addOptional(skosConcept, NodeFactory.createURI(Skos.PROPERTY_PREF_LABEL),
								NodeFactory.createVariable(DataObjects.THEME));
			}
		}

		selectBuilder.setVar(Var.alloc("dataset"), "<" + datasetUri.toString() + ">");

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

	private void getDistributionData(URI datasetUri, DataContainer dataContainer) {

		// Ensure connection
		if (!isConnected()) {
			connect();
		}

		// Build query
		SelectBuilder selectBuilder = new SelectBuilder();
		selectBuilder.addWhere("?dataset", NodeFactory.createURI(Dcat.PROPERTY_DISTRIBUTION), "?distribution");
		for (DataObject<?> dataObject : dataContainer.getDataObjects()) {

			// Dataset properties

			if (addDistributionRelation(selectBuilder, dataObject.getId(), DataObjects.LICENSE,
					DublinCore.PROPERTY_LICENSE))
				continue;

			if (addDistributionRelation(selectBuilder, dataObject.getId(), DataObjects.ACCESS_URL,
					Dcat.PROPERTY_ACCESS_URL))
				continue;

			if (addDistributionRelation(selectBuilder, dataObject.getId(), DataObjects.DOWNLOAD_URL,
					Dcat.PROPERTY_DOWNLOAD_URL))
				continue;
		}
		selectBuilder.setVar(Var.alloc("dataset"), "<" + datasetUri.toString() + ">");

		// Execute query
		Query query = selectBuilder.build();
		LOGGER.debug(query.toString());
		QueryExecution queryExecution = rdfConnection.query(query);
		ResultSet resultSet = queryExecution.execSelect();

		// Process results
		int categories = 0;
		while (resultSet.hasNext()) {
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
	}

	private boolean addDatasetRelation(SelectBuilder selectBuilder, String dataObjectIdActual,
			String dataObjectIdExpected, String predicate) {
		if (dataObjectIdActual.equals(dataObjectIdExpected)) {
			selectBuilder.addVar(dataObjectIdExpected).addOptional("?dataset", NodeFactory.createURI(predicate),
					NodeFactory.createVariable(dataObjectIdExpected));
			return true;
		} else {
			return false;
		}
	}

	private boolean addDistributionRelation(SelectBuilder selectBuilder, String dataObjectIdActual,
			String dataObjectIdExpected, String predicate) {
		if (dataObjectIdActual.equals(dataObjectIdExpected)) {
			selectBuilder.addVar(dataObjectIdExpected).addOptional("?distribution", NodeFactory.createURI(predicate),
					NodeFactory.createVariable(dataObjectIdExpected));
			return true;
		} else {
			return false;
		}
	}

}