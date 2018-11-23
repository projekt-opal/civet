package org.dice_research.opal.civet.metrics;

import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.DataObjects;
import org.dice_research.opal.civet.data.StringDataObject;

/**
 * Single metric.
 *
 * @author Adrian Wilke
 */
public class DescriptionMetric extends Metric {

	private static final String DESCRIPTION = "Calculates a score based on the extend of the description.";
	private static final String ID = DescriptionMetric.class.getSimpleName();
	private static final MetricType METRIC_TYPE = MetricType.FIVE_STAR;

	public DescriptionMetric() {
		this.description = DESCRIPTION;
		this.id = ID;
		this.metricType = METRIC_TYPE;
	}

	@Override
	public float getScore(DataContainer dataContainer) {

		StringDataObject dataObject = dataContainer.getStringDataObject(DataObjects.TITLE);
		String title;
		if (dataObject.isEmpty()) {
			return 0f;
		} else {
			title = dataObject.getValues().get(0);
		}

		dataObject = dataContainer.getStringDataObject(DataObjects.DESCRIPTION);
		String description;
		if (dataObject.isEmpty()) {
			return 0f;
		} else {
			description = dataObject.getValues().get(0);
		}

		if (title.isEmpty() && description.isEmpty()) {
			return 0f;

		} else if (title.isEmpty()) {
			return 1f;
		} else if (description.isEmpty()) {
			return 1f;

		} else if (description.equals(title)) {
			return 1f;

		} else if (description.length() <= 25) {
			return 2f;

		} else if (description.length() <= 50) {
			return 3f;

		} else if (description.length() <= 75) {
			return 4f;

		} else {
			return 5f;
		}

	}

}