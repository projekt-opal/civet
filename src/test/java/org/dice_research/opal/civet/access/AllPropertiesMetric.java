package org.dice_research.opal.civet.access;

import java.util.Arrays;
import java.util.Collection;

import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.DataObject;
import org.dice_research.opal.civet.data.DataObjects;
import org.dice_research.opal.civet.metrics.Metric;
import org.dice_research.opal.civet.metrics.MetricType;
import org.dice_research.opal.civet.remote.OpalAccessorTest;

/**
 * Uses all known properties. Calculated score is number of non-empty data
 * objects.
 * 
 * Used in {@link OpalAccessorTest}.
 *
 * @author Adrian Wilke
 */
public class AllPropertiesMetric extends Metric {

	private static final String DESCRIPTION = "Requires all properties for tests.";
	private static final String ID = AllPropertiesMetric.class.getSimpleName();
	private static final MetricType METRIC_TYPE = MetricType.COUNTER;
	private static final Collection<String> REQUIRED_PROPERTIES = Arrays.asList(

			DataObjects.NUMBER_OF_CATEGORIES,

			DataObjects.DESCRIPTION, DataObjects.ISSUED, DataObjects.PUBLISHER, DataObjects.THEME, DataObjects.TITLE,

			DataObjects.ACCESS_URL, DataObjects.DOWNLOAD_URL, DataObjects.LICENSE);

	public AllPropertiesMetric() {
		this.description = DESCRIPTION;
		this.id = ID;
		this.metricType = METRIC_TYPE;
		this.requiredProperties = REQUIRED_PROPERTIES;
	}

	/**
	 * Counts number of non-empty data objects.
	 */
	@Override
	public float getScore(DataContainer dataContainer) {
		int nonEmpty = 0;
		for (DataObject<?> dataObject : dataContainer.getDataObjects()) {
			if (!dataObject.getValues().isEmpty()) {
				nonEmpty++;
			}
		}
		return nonEmpty;
	}

}