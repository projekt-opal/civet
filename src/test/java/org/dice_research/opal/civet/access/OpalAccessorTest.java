package org.dice_research.opal.civet.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import org.dice_research.opal.civet.Config;
import org.dice_research.opal.civet.Orchestration;
import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.DataObjects;
import org.junit.Test;

public class OpalAccessorTest {

	@Test
	public void testAllProperties() throws URISyntaxException {

		// Get all properties
		AllPropertiesMetric allPropertiesMetric = new AllPropertiesMetric();
		Collection<String> allPropertiesIds = allPropertiesMetric.getRequiredProperties();

		// Create data container and put data objects
		DataContainer dataContainer = new DataContainer();
		for (String propertyId : allPropertiesIds) {
			dataContainer.putDataObject(DataObjects.createDataObject(propertyId));
		}

		// Get data
		Orchestration orchestration = new Orchestration();
		orchestration.getConfiguration().setSparqlQueryEndpoint(Config.sparqlQueryEndpoint);
		orchestration.getConfiguration().setNamedGraph(Config.namedGraph);
		OpalAccessor opalAccessor = new OpalAccessor(orchestration);
		opalAccessor.getData(new URI(Config.datasetUriBerlin), dataContainer);

		// ACCESS_URL data is currently not used in RDF graph
		int unusedProperties = 1;

		// Check, if all properties are non-empty
		assertEquals(allPropertiesMetric.getRequiredProperties().size() - unusedProperties,
				allPropertiesMetric.getScore(dataContainer), 0);

		// Human test of score
		// System.out.println(allPropertiesMetric.getScore(dataContainer));

		// Human investigation of data
		// System.out.println(dataContainer.toExtendedString());
	}

	@Test
	public void testMultipleDatasets() {

		int limit = 10;
		int offset = 0;

		// Get all properties
		AllPropertiesMetric allPropertiesMetric = new AllPropertiesMetric();
		Collection<String> allPropertiesIds = allPropertiesMetric.getRequiredProperties();

		// Create data container and put data objects
		DataContainer dataContainer = new DataContainer();
		for (String propertyId : allPropertiesIds) {
			dataContainer.putDataObject(DataObjects.createDataObject(propertyId));
		}

		// Get data
		Orchestration orchestration = new Orchestration();
		orchestration.getConfiguration().setSparqlQueryEndpoint(Config.sparqlQueryEndpoint);
		orchestration.getConfiguration().setNamedGraph(Config.namedGraph);
		OpalAccessor opalAccessor = new OpalAccessor(orchestration);
		OpalAccessorContainer resultsContainer = opalAccessor.getData(dataContainer, limit, offset);

		// Returned number of datasets shout be at least 1. Multiple results specified
		// by limit may return the same dataset with additional information.
		assertNotEquals(0, resultsContainer.dataContainers.size());

		// Checks for all datasets, if at least one non-empty data-object exists.
		for (DataContainer container : resultsContainer.dataContainers.values()) {
			assertNotEquals(0, allPropertiesMetric.getScore(container));
		}

		// If this appears, the data of a dataset is maybe not completely returned.
		// Devs have to increase the value of limit.
		assertNotEquals(0, resultsContainer.refreshIndex);
	}
}