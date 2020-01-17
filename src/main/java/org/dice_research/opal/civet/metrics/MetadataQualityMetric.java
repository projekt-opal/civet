package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Dqv;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The MetadataQualityMetric calculates the average of all available metric
 * results.
 * 
 * @author Adrian Wilke
 */
public class MetadataQualityMetric implements Metric {

	private static final String DESCRIPTION = "Calculates the average of all available metric results.";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		Resource dataset = ResourceFactory.createResource(datasetUri);

		int numberOfMeasurements = 0;
		int sumOfMeasurements = 0;

		// Get measurements
		NodeIterator measurementIterator = model.listObjectsOfProperty(dataset, Dqv.HAS_QUALITY_MEASUREMENT);
		while (measurementIterator.hasNext()) {
			RDFNode measurementNode = measurementIterator.next();
			if (measurementNode.isResource()) {
				Resource measurement = measurementNode.asResource();

				// Check related metric
				NodeIterator metricIterator = model.listObjectsOfProperty(measurement, Dqv.IS_MEASUREMENT_OF);
				if (metricIterator.hasNext()) {
					RDFNode metric = metricIterator.next();

					// Do not aggregate existing aggregation
					if (metric.isURIResource()) {
						if (metric.asResource().getURI().equals(Opal.OPAL_METRIC_METADATA_QUALITY.getURI())) {
							continue;
						}
					}
				}

				// Get value
				NodeIterator valueIterator = model.listObjectsOfProperty(measurement, Dqv.HAS_VALUE);
				if (valueIterator.hasNext()) {
					RDFNode valueNode = valueIterator.next();
					if (valueNode.isLiteral()) {
						try {
							sumOfMeasurements += valueNode.asLiteral().getInt();
							numberOfMeasurements++;
						} catch (Exception e) {
							// Ignore value of this measurement
						}
					}
				}
			}
		}

		return Math.round(1f * sumOfMeasurements / numberOfMeasurements);
	}

	@Override
	public String getDescription() throws Exception {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_METADATA_QUALITY.getURI();
	}

}