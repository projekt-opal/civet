package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.RDF;
import org.dice_research.opal.civet.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link CategorizationMetric}.
 * 
 * @author Adrian Wilke
 */
public class CategorizationMetricTest {

	TestData testdata;

	private static final String TEST_EDP_ICE = "Europeandataportal-Iceland.ttl";
	private static final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/http___europeandataportal_eu_set_data__3dff988d_59d2_415d_b2da_818e8ef3111701";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	/**
	 * Tests all 3 cases.
	 */
	@Test
	public void testCases() throws Exception {
		CategorizationMetric metric = new CategorizationMetric();

		Model model = ModelFactory.createDefaultModel();
		String datasetUri = "https://example.org/dataset-1";
		Resource dataset = ResourceFactory.createResource(datasetUri);
		model.add(dataset, RDF.type, DCAT.Dataset);

		Assert.assertEquals("No keywords", 0, metric.compute(model, datasetUri).intValue());

		model.addLiteral(dataset, DCAT.keyword, ResourceFactory.createPlainLiteral("keyword-1"));
		Assert.assertEquals("One keyword", 4, metric.compute(model, datasetUri).intValue());

		model.addLiteral(dataset, DCAT.keyword, ResourceFactory.createPlainLiteral("keyword-2"));
		Assert.assertEquals("Two keywords", 5, metric.compute(model, datasetUri).intValue());
	}

	/**
	 * Tests all 3 cases.
	 */
	@Test
	public void testEdpIce() throws Exception {
		
		// Compute stars
		CategorizationMetric metric = new CategorizationMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_EDP_ICE), TEST_EDP_ICE_DATASET);

		// Keywords in test dataset:
		// "Iceland" , "Downloadable Data" , "land use" , "land cover"
		// Should result in 5 stars
		Assert.assertEquals(TEST_EDP_ICE, 5, stars.intValue());
	}

}