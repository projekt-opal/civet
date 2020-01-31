package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.RDF;
import org.dice_research.opal.civet.Civet;
import org.dice_research.opal.civet.Utils;
import org.dice_research.opal.common.vocabulary.Dqv;
import org.dice_research.opal.common.vocabulary.Opal;
import org.dice_research.opal.test_cases.OpalTestCases;
import org.dice_research.opal.test_cases.TestCase;
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
	public void testAverage() throws Exception {

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

	/**
	 * Tests number of metrics.
	 */
	@Test
	public void testIncludedMetrics() throws Exception {

		TestCase testCase = OpalTestCases.getTestCase("opal-2019-06-24", "edp-corine-iceland");

		Model model = ModelFactory.createDefaultModel();
		model.add(testCase.getModel());

		Civet civet = new Civet();
		civet.processModel(model, testCase.getDatasetUri());

		Resource dataset = model.getResource(testCase.getDatasetUri());

		StmtIterator stmtIterator = dataset.listProperties(Dqv.HAS_QUALITY_MEASUREMENT);
		int counter = 0;
		while (stmtIterator.hasNext()) {
			stmtIterator.next().getObject();
			counter++;
		}

		model.write(System.out, "TURTLE");

		// Note: Metrics integrated in the future may return null, if it is not possible
		// to calculate a score for the test model.
		Assert.assertEquals("Number of computed metrics", civet.getMetrics().size(), counter);
	}

}