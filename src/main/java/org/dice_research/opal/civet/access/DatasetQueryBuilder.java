package org.dice_research.opal.civet.access;

import org.apache.jena.arq.querybuilder.SelectBuilder;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.core.Var;
import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.DataObject;
import org.dice_research.opal.civet.data.DataObjects;
import org.dice_research.opal.civet.vocabulary.Dcat;
import org.dice_research.opal.civet.vocabulary.DublinCore;
import org.dice_research.opal.civet.vocabulary.Foaf;
import org.dice_research.opal.civet.vocabulary.Skos;

public class DatasetQueryBuilder {

	public static final String VAR_DATASET = "DATASET";

	private boolean addInitialDataset = false;
	private String datasetUri = null;
	private Integer limit = null;
	private String namedGraph = null;
	private Integer offset = null;

	public DatasetQueryBuilder setLimit(Integer limit) {
		this.limit = limit;
		return this;
	}

	public DatasetQueryBuilder setOffset(Integer offset) {
		this.offset = offset;
		return this;
	}

	public DatasetQueryBuilder setNamedGraph(String namedGraph) {
		this.namedGraph = namedGraph;
		return this;
	}

	public DatasetQueryBuilder setAddInitialDataset(boolean addInitialDataset) {
		this.addInitialDataset = addInitialDataset;
		return this;
	}

	public DatasetQueryBuilder setDatasetUri(String datasetUri) {
		this.datasetUri = datasetUri;
		return this;
	}

	public Query getQuery(DataContainer dataContainer) {

		SelectBuilder selectBuilder = new SelectBuilder();
		selectBuilder.setDistinct(true);

		// Use named graph or default graph

		if (namedGraph != null) {
			selectBuilder.from(namedGraph);
		}

		// Required, if no dataset URI is provided

		if (addInitialDataset) {
			Triple initialWhereTriple = new Triple(NodeFactory.createVariable(VAR_DATASET),
					NodeFactory.createURI(org.apache.jena.vocabulary.RDF.type.getURI()),
					NodeFactory.createURI(Dcat.PROPERTY_DATASET));
			selectBuilder.addWhere(initialWhereTriple);
			selectBuilder.addVar(VAR_DATASET);
		}

		// Add dataset URI

		if (datasetUri != null) {
			selectBuilder.setVar(Var.alloc(VAR_DATASET), "<" + datasetUri.toString() + ">");
		}

		// Add query parts

		for (DataObject<?> dataObject : dataContainer.getDataObjects()) {

			// Add directly connected data

			if (addDatasetRelation(selectBuilder, dataObject.getId(), DataObjects.DESCRIPTION,
					DublinCore.PROPERTY_DESCRIPTION))
				continue;

			if (addDatasetRelation(selectBuilder, dataObject.getId(), DataObjects.ISSUED, DublinCore.PROPERTY_ISSUED))
				continue;

			if (addDatasetRelation(selectBuilder, dataObject.getId(), DataObjects.TITLE, DublinCore.PROPERTY_TITLE))
				continue;

			// Add indirectly connected data

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

		// Add pagination

		if (limit != null) {
			selectBuilder.setLimit(limit);
		}
		if (offset != null) {
			selectBuilder.setOffset(offset);
		}

		return selectBuilder.build();
	}

	/**
	 * Checks, if dataObjectIdActual equals dataObjectIdExpected. If true, the given
	 * predicate is used to add an optional variable.
	 */
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

}