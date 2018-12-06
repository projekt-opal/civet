package org.dice_research.opal.civet.metrics;

import java.util.Arrays;
import java.util.Collection;

import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.DataObjects;
import org.dice_research.opal.civet.data.StringDataObject;

/**
 * Single metric.
 * 
 * TODO: Currently it is assumed that every distribution represents an update.
 *
 * @author Adrian Wilke
 */
public class UpdateRateMetric extends Metric {

	private static final String DESCRIPTION = "Calculates a score based on the number of distributions.";
	private static final String ID = UpdateRateMetric.class.getSimpleName();
	private static final MetricType METRIC_TYPE = MetricType.FIVE_STAR;
	private static final Collection<String> REQUIRED_PROPERTIES = Arrays.asList(DataObjects.DOWNLOAD_URL);

	public UpdateRateMetric() {
		this.description = DESCRIPTION;
		this.id = ID;
		this.metricType = METRIC_TYPE;
		this.requiredProperties = REQUIRED_PROPERTIES;
	}

	@Override
	public float getScore(DataContainer dataContainer) {

		StringDataObject dataObject = dataContainer.getStringDataObject(DataObjects.DOWNLOAD_URL);
		int downloadUrls;
		if (dataObject.isEmpty()) {
			return 0f;
		} else {
			downloadUrls = dataObject.getValues().size();
		}

		if (downloadUrls <= 1) {
			// No Updates
			return 0f;

		} else {
			// Updated
			return 5f;
		}

	}
}