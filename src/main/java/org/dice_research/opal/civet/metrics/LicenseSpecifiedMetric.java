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
public class LicenseSpecifiedMetric extends Metric {

	private static final String DESCRIPTION = "Checks, if a license is given.";
	private static final String ID = LicenseSpecifiedMetric.class.getSimpleName();
	private static final MetricType METRIC_TYPE = MetricType.FIVE_STAR;
	private static final Collection<String> REQUIRED_PROPERTIES = Arrays.asList(DataObjects.LICENSE);

	public LicenseSpecifiedMetric() {
		this.description = DESCRIPTION;
		this.id = ID;
		this.metricType = METRIC_TYPE;
		this.requiredProperties = REQUIRED_PROPERTIES;
	}

	@Override
	public float getScore(DataContainer dataContainer) {

		StringDataObject dataObject = dataContainer.getStringDataObject(DataObjects.LICENSE);
		boolean licenceSpecified = false;
		if (dataObject.isEmpty()) {
			return 0f;
		} else {
			for (String value : dataObject.getValues()) {
				if (value != null && !value.isEmpty()) {
					licenceSpecified = true;
					break;
				}
			}
		}

		if (licenceSpecified) {
			// At least one specified license
			return 5f;

		} else {
			// No license specified
			return 0f;
		}

	}
}