package org.dice_research.opal.civet.metrics;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.DataObjects;
import org.dice_research.opal.civet.data.StringDataObject;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * Single metric.
 *
 * @author Adrian Wilke
 */
public class LinkedDataMetric extends Metric {

	private static final String DESCRIPTION = "Checks, if data is offered via RDF/SPARQL.";
	private static final String ID = LinkedDataMetric.class.getSimpleName();
	private static final MetricType METRIC_TYPE = MetricType.FIVE_STAR;
	private static final Collection<String> REQUIRED_PROPERTIES = Arrays.asList(DataObjects.CATALOG);
	private static final String RESULTS_URI = Opal.OPAL_METRIC_LINKED_DATA.toString();

	private static final List<String> CATALOGS_RDF;

	static {
		CATALOGS_RDF = new LinkedList<>();

		// Configuration of metric and portals

		// https://www.europeandataportal.eu/sparql
		CATALOGS_RDF.add(Opal.OPAL_CATALOG_EUROPEANDATAPORTAL.toString());
	}

	public LinkedDataMetric() {
		this.description = DESCRIPTION;
		this.id = ID;
		this.metricType = METRIC_TYPE;
		this.requiredProperties = REQUIRED_PROPERTIES;
		this.resultsUri = RESULTS_URI;
	}

	@Override
	public float getScore(DataContainer dataContainer) {
		StringDataObject dataObject = dataContainer.getStringDataObject(DataObjects.CATALOG);

		if (dataObject.isEmpty()) {
			// Every dataset should be related to a catalog
			LOGGER.error("No catalog provided");
			return 0;

		} else {

			// Check, if catalog provides RDF / linked data
			for (String value : dataObject.getValues()) {
				if (CATALOGS_RDF.contains(value)) {
					return 5f;
				}
			}

			// No RDF / linked data provided
			return 0f;
		}

	}

}