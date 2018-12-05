package org.dice_research.opal.civet.access;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import org.dice_research.opal.civet.Config;
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
		OpalAccessor opalAccessor = new OpalAccessor(Config.sparqlQueryEndpoint);
		opalAccessor.getData(new URI(Config.datasetUriMcloud), dataContainer);
		opalAccessor.getData(new URI(Config.datasetUriEuroPortal), dataContainer);

		// Use data
		// TODO: Test minimal score
		System.out.println(allPropertiesMetric.getScore(dataContainer));

		// TODO: get all literals
		StringBuilder stringBuilder = new StringBuilder();
		for (String propertyId : allPropertiesIds) {
			stringBuilder.append(dataContainer.getDataObject(propertyId));
			stringBuilder.append(System.lineSeparator());
			for (Object value : dataContainer.getDataObject(propertyId).getValues()) {
				stringBuilder.append(" ");
				stringBuilder.append(value.toString());
				stringBuilder.append(System.lineSeparator());
			}
		}
		System.out.println(stringBuilder.toString());
	}
}
