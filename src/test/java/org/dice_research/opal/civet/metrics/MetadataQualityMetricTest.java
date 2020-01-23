package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.RDF;
import org.dice_research.opal.civet.Utils;
import org.dice_research.opal.common.vocabulary.Opal;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link MetadataQualityMetric}.
 * 
 * @author Adrian Wilke
 */
public class MetadataQualityMetricTest {

	/**
	 * Tests calculation of average.
	 */
	@Test
	public void test() throws Exception {

		String datasetUri = "https://example.org/dataset";
		Resource dataset = ResourceFactory.createResource(datasetUri);

		Model model = ModelFactory.createDefaultModel();
		model.add(dataset, RDF.type, DCAT.Dataset);

		// Included metrics
		model.add(Utils.createMetricStatements(dataset, Opal.OPAL_METRIC_CATEGORIZATION, 5));
		model.add(Utils.createMetricStatements(dataset, Opal.OPAL_METRIC_MULTIPLE_SERIALIZATIONS, 1));

		// Should be ignored
		model.add(Utils.createMetricStatements(dataset, Opal.OPAL_METRIC_METADATA_QUALITY, 999));

		MetadataQualityMetric metric = new MetadataQualityMetric();
		Integer score = metric.compute(model, datasetUri);

		Assert.assertEquals("Calculated average", 3, score.intValue());
	}

}