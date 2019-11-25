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
 * Tests {@link TimelinessMetric}.
 */
public class TimelinessMetricTest {

	TestData testdata;

	private static final String TEST_EDP_ICE = "Europeandataportal-Iceland.ttl";
	private static final String TEST_EDP_ICE_DATASET1 =
			"http://projekt-opal.de/dataset/http___europeandataportal_eu_set_data__3dff988d_59d2_415d_b2da_818e8ef3111701";
	private static final String TEST_EDP_ICE_DATASET2 =
			"http://projekt-opal.de/dataset/https___ckan_govdata_de_001c9703_3556_4f62_a376_85804f18ab52";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	@Test
	public void testCases() throws Exception {
		TimelinessMetric metric = new TimelinessMetric();

		Model model = ModelFactory.createDefaultModel();
		String datasetUri = "https://example.org/dataset-1";
		Resource dataset = ResourceFactory.createResource(datasetUri);
		model.add(dataset, RDF.type, DCAT.Dataset);

		Assert.assertEquals(" Data not given", null, metric.compute(model, datasetUri));

//		model.addLiteral(dataset, DCTerms.modified, ResourceFactory.createPlainLiteral("UpdatedWeek"));
//		Assert.assertEquals("Updated Week ago", 5, metric.compute(model, datasetUri).intValue());
//
//		model.addLiteral(dataset, DCTerms.modified, ResourceFactory.createPlainLiteral("UpdatedHalfYearly"));
//		Assert.assertEquals("Updated between week and 6 months", 4, metric.compute(model, datasetUri).intValue());
//
//		model.addLiteral(dataset, DCTerms.modified, ResourceFactory.createPlainLiteral("UpdatedYearly"));
//		Assert.assertEquals("Updated between 6months and year", 3, metric.compute(model, datasetUri).intValue());
//
//		model.addLiteral(dataset, DCTerms.modified, ResourceFactory.createPlainLiteral("UpdatedBiYearly"));
//		Assert.assertEquals("Updated between 1 and 2 years", 2, metric.compute(model, datasetUri).intValue());
//
//		model.addLiteral(dataset, DCTerms.modified, ResourceFactory.createPlainLiteral("UpdatedQuadYearly"));
//		Assert.assertEquals("Updated between 2 and 4 years", 1, metric.compute(model, datasetUri).intValue());

//		model.addLiteral(dataset, DCTerms.modified, ResourceFactory.createPlainLiteral("UpdatedEarlier"));
//		Assert.assertEquals("Updated before 4 years", 0, metric.compute(model, datasetUri).intValue());
	}

	@Test
	public void testEdpIce() throws Exception {
		
		// Compute stars
		TimelinessMetric metric = new TimelinessMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_EDP_ICE), TEST_EDP_ICE_DATASET1);
		Assert.assertEquals(TEST_EDP_ICE, 0, stars.intValue());

		//To do
		// for 5,4,3,2,1 stars
		// need TTL files

	}
}