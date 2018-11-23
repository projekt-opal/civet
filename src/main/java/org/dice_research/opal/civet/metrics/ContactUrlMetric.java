package org.dice_research.opal.civet.metrics;

import java.net.MalformedURLException;
import java.net.URL;

import org.dice_research.opal.civet.data.DataContainer;
import org.dice_research.opal.civet.data.DataObjects;
import org.dice_research.opal.civet.data.StringDataObject;

/**
 * Single metric.
 *
 * @author Adrian Wilke
 */
public class ContactUrlMetric extends Metric {

	private static final String DESCRIPTION = "Calculates a score based on the contact URL.";
	private static final String ID = ContactUrlMetric.class.getSimpleName();
	private static final MetricType METRIC_TYPE = MetricType.FIVE_STAR;

	public ContactUrlMetric() {
		this.description = DESCRIPTION;
		this.id = ID;
		this.metricType = METRIC_TYPE;
	}

	@Override
	public float getScore(DataContainer dataContainer) {
		StringDataObject dataObject = dataContainer.getStringDataObject(DataObjects.CONTACT_URL);

		String url;
		if (dataObject.isEmpty()) {
			return 0f;
		} else {
			url = dataObject.getValues().get(0);
		}

		if (url.isEmpty()) {
			return 0f;
		}

		// Not empty
		else if (!url.contains(".")) {
			return 1f;
		}

		// Dot contained
		else if (!url.contains("//")) {
			return 2f;
		}

		try {
			new URL(url);
		} catch (MalformedURLException e) {
			// Double slash contained
			return 3f;
		}

		// Full URL
		return 5f;
	}

}