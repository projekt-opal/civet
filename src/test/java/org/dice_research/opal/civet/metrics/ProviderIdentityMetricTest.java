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
	private static final String TestCase1 = "TestCaseforProviderIdentityEmptyBlanknodeButValidAccessURLs5star.ttl";

	// Publisher with URI of non Foaf:org or Foaf:Person  ----> 4 stars
	private static final String TestCase2 = "TestCaseforProviderIdentityNoFoafRecommendations4Stars.ttl";

	// Inconsistent Publishers 3 stars
	private static final String TestCase3 = "TestCaseforProviderIdentityInconsistentProviders3Stars.ttl";

	// Publishers with non-empty blanknodes with FoaF:Org ----> 5 stars
	private static final String TestCase4 = "TestCaseforProviderIdentityNonemptyBlanknode5stars.ttl";

	// Publishers with inconsistent providers -----> 3 stars
	private static final String TestCase5 = "TestCaseforProviderIdentityNonemptyBlanknodeInconsistentProviders3stars.ttl";


	private static final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/http___europeandataportal_eu_set_data__3dff988d_59d2_415d_b2da_818e8ef3111701";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	@Test
	public void TestCase1() throws Exception {

		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		Integer stars_test1 = metric.compute(testdata.getModel(TestCase1), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Provider Identity Test: Test Case 1", 5, stars_test1.intValue());
	}

	@Test
	public void TestCase2() throws Exception {
		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		Integer stars_test2 = metric.compute(testdata.getModel(TestCase2), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Provider Identity Test: Test Case 1", 4, stars_test2.intValue());
	}

	@Test
	public void TestCase3() throws Exception {
		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		Integer stars_test3 = metric.compute(testdata.getModel(TestCase3), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Provider Identity Test: Test Case 3", 3,
				stars_test3.intValue());
	}

	@Test
	public void TestCase4() throws Exception {
		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		Integer stars_test4 = metric.compute(testdata.getModel(TestCase4), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Provider Identity Test: Test Case 4", 5,
				stars_test4.intValue());
	}

	@Test
	public void TestCase5() throws Exception {
		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		Integer stars_test5 = metric.compute(testdata.getModel(TestCase5), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Provider Identity Test: Test Case 5", 3, stars_test5.intValue());
	}


}