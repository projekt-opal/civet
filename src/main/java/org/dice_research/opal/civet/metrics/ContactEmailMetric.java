package org.dice_research.opal.civet.metrics;

import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.InputType;
import org.dice_research.opal.civet.exceptions.InputMissingException;

/**
 * Single metric.
 *
 * @author Adrian Wilke
 */
public class ContactEmailMetric extends Metric {

	private static final String DESCRIPTION = "Calculates a score based on the contact email.";
	private static final String ID = ContactEmailMetric.class.getSimpleName();
	private static final MetricType METRIC_TYPE = MetricType.FIVE_STAR;

	public ContactEmailMetric() {
		this.description = DESCRIPTION;
		this.id = ID;
		this.metricType = METRIC_TYPE;
	}

	@Override
	public float getScore(DataContainer data) throws InputMissingException {
		String email = data.getInputAsString(InputType.CONTACT_EMAIL).trim();

		if (email.isEmpty()) {
			return 0f;
		}

		else if (!email.contains("@")) {
			return 1f;
		}

		else if (email.length() < 6) {
			return 1f;
		}

		else {
			return 5f;
		}

	}

}