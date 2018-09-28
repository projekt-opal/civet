package org.dice_research.opal.civet.metrics;

import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.InputType;
import org.dice_research.opal.civet.exceptions.InputMissingException;

public class CategorizationMetirc extends Metric {

	private static final String DESCRIPTION = "Calculates a score based on the"
			+ "number of categories of this dataset and the average number of categories.";
	private static final String ID = CategorizationMetirc.class.getSimpleName();
	private static final MetricType METRIC_TYPE = MetricType.SCALE;

	public CategorizationMetirc() {
		this.description = DESCRIPTION;
		this.id = ID;
		this.metricType = METRIC_TYPE;
	}

	@Override
	public float getScore(DataContainer data) throws InputMissingException {
		return data.getInputAsFloat(InputType.NUMBER_OF_CATEGORIES)
				/ (data.getInputAsFloat(InputType.AVERAGE_NUMBER_OF_CATEGORIES) * 2);
	}

}