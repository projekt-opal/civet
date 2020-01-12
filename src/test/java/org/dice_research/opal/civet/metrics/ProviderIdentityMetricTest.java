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
 * Tests {@link AvailabilityOfLicensesMetric}.
 * 
 * @author Gourab Sahu
 */
public class ProviderIdentityMetricTest {

	TestData testdata;

	// Publisher with empty blank node but all distributions have valid accessURLS ---> 5 stars
	private static final String TestCase1 = "TestCaseforProviderIdentityNonemptyBlanknodeInconsistentProviders3stars.ttl";

	private static final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/http___europeandataportal_eu_set_data__3dff988d_59d2_415d_b2da_818e8ef3111701";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	@Test
	public void TestCase1() throws Exception {

		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		Integer stars_test1 = metric.compute(testdata.getModel(TestCase1), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Provider Identity Test: Test Case 1", 3, stars_test1.intValue());
	}

}