package org.dice_research.opal.civet.metrics;

import java.util.Arrays;
import java.util.Collection;

import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.DataObjects;
import org.dice_research.opal.civet.data.StringDataObject;

/**
 * Single metric.
 *
 * @author Adrian Wilke
 */
public class ContactEmailMetric extends Metric {

	private static final String DESCRIPTION = "Calculates a score based on the contact email.";
	private static final String ID = ContactEmailMetric.class.getSimpleName();
	private static final MetricType METRIC_TYPE = MetricType.FIVE_STAR;
	private static final Collection<String> REQUIRED_PROPERTIES = Arrays.asList(DataObjects.CONTACT_EMAIL);

	public ContactEmailMetric() {
		this.description = DESCRIPTION;
		this.id = ID;
		this.metricType = METRIC_TYPE;
		this.requiredProperties = REQUIRED_PROPERTIES;
	}

	@Override
	public float getScore(DataContainer dataContainer) {
		StringDataObject dataObject = dataContainer.getStringDataObject(DataObjects.CONTACT_EMAIL);

		String email;
		if (dataObject.isEmpty()) {
			return 0f;
		} else {
			email = dataObject.getValues().get(0);
		}

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