package org.dice_research.opal.civet.access;

import java.net.URI;
import java.util.Iterator;

import org.apache.jena.arq.querybuilder.SelectBuilder;
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

			if (addDatasetRelation(selectBuilder, dataObject.getId(), DataObjects.PUBLISHER,
					DublinCore.PROPERTY_PUBLISHER))
				continue;

			if (addDatasetRelation(selectBuilder, dataObject.getId(), DataObjects.THEME, Dcat.PROPERTY_THEME))
				continue;

			if (addDatasetRelation(selectBuilder, dataObject.getId(), DataObjects.TITLE, DublinCore.PROPERTY_TITLE))
				continue;

			LOGGER.warn("Unknown data object ID: " + dataObject.getId());
		}
		selectBuilder.setVar(Var.alloc("dataset"), "<" + datasetUri.toString() + ">");

		// Execute query
		Query query = selectBuilder.build();
		LOGGER.debug(query.toString());
		QueryExecution queryExecution = rdfConnection.query(query);
		ResultSet resultSet = queryExecution.execSelect();

		// Process results
		if (resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.next();

			// Iterator only returns variables with values (optional properties are skipped)
			Iterator<String> iterator = querySolution.varNames();
			while (iterator.hasNext()) {
				String id = iterator.next();
				try {
					dataContainer.getDataObject(id).addValue(querySolution.getLiteral(id).getString());
				} catch (ParsingException e) {
					LOGGER.error(e);
				}

			}
		}
		if (resultSet.hasNext()) {
			LOGGER.debug("More than one result returned.");
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
}