package org.dice_research.opal.civet.metrics;

import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.DataObjects;
import org.dice_research.opal.civet.data.IntegerDataObject;

/**
 * Single metric.
 *
 * @author Adrian Wilke
 */
public class CategorizationMetric extends Metric {

	private static final String DESCRIPTION = "Calculates a score based on the number of categories.";
	private static final String ID = CategorizationMetric.class.getSimpleName();
	private static final MetricType METRIC_TYPE = MetricType.FIVE_STAR;

	public CategorizationMetric() {
		this.description = DESCRIPTION;
		this.id = ID;
		this.metricType = METRIC_TYPE;
	}

	@Override
	public float getScore(DataContainer dataContainer) {
		IntegerDataObject dataObject = dataContainer.getIntegerDataObject(DataObjects.NUMBER_OF_CATEGORIES);

		int numberOfCategories = 0;
		if (dataObject.isEmpty()) {
			return 0f;
		} else {
			numberOfCategories = dataObject.getValues().get(0);
		}

		if (numberOfCategories <= 0) {
			return 0f;
		} else if (numberOfCategories <= 1) {
			return 1f;
		} else if (numberOfCategories <= 2) {
			return 2f;
		} else {
			return 5f;
		}
	}

}