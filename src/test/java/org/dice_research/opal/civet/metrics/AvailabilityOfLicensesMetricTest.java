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
public class AvailabilityOfLicensesMetricTest {

	TestData testdata;

	// Dataset has 5 distributions and all 5 distribution have rights/license info ---> 5 Stars
	private static final String TestCase1 = "TestCaseForKnownLicense5stars_5dist_5rights.ttl";

	// Dataset has 5 distributions and all 4 distribution have rights/license info ----> 4 stars
	private static final String TestCase2 = "TestCaseForKnownLicense4stars_5dist_4rights.ttl";

	// Dataset has direct rghts/license info in Dataset only -----> 5 stars
	private static final String TestCase3 = "TestCaseForKnownLicense5stars_dataset_has_licenseInfo.ttl";
	
	// Out of 5 distributions 2 dist has licenses and 2 dist has rights -----> 4 stars
	private static final String TestCase4 = "TestCaseForKnownLicense4stars_5dist_2license_2rights.ttl";
	
	// Out of 5 distributions 1 dist has license  -----> 2 stars
    private static final String TestCase5 = "TestCaseForKnownLicense2stars_5dist_1license.ttl";
    
 // Out of 5 distributions 2 dist has licenses  -----> 3 stars
    private static final String TestCase6 = "TestCaseForKnownLicense3stars_5dist_2rights.ttl";
	
	
	private static final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/http___europeandataportal_eu_set_data__3dff988d_59d2_415d_b2da_818e8ef3111701";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}
	
	
	@Test
	public void TestCase1() throws Exception {

		AvailabilityOfLicensesMetric metric = new AvailabilityOfLicensesMetric();
		Integer stars = metric.compute(testdata.getModel(TestCase1), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("5 Distributions and all have license ", 5, stars.intValue());
	}

	@Test
	public void TestCase2() throws Exception {
		AvailabilityOfLicensesMetric metric = new AvailabilityOfLicensesMetric();
		Integer stars = metric.compute(testdata.getModel(TestCase2), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Out of 5 distributions, 4 distributions have rights info", 4, stars.intValue());
	}

	@Test
	public void TestCase3() throws Exception {
		AvailabilityOfLicensesMetric metric = new AvailabilityOfLicensesMetric();
		Integer stars = metric.compute(testdata.getModel(TestCase3), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Dataset has license info directly", 5, stars.intValue());
	}
	
	@Test
	public void TestCase4() throws Exception {
		AvailabilityOfLicensesMetric metric = new AvailabilityOfLicensesMetric();
		Integer stars = metric.compute(testdata.getModel(TestCase4), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Dataset 5 distributions, 2 dis has dct:license and 2 dis has dct:rights", 4, stars.intValue());
	}
	
	@Test
	public void TestCase5() throws Exception {
		AvailabilityOfLicensesMetric metric = new AvailabilityOfLicensesMetric();
		Integer stars = metric.compute(testdata.getModel(TestCase5), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Dataset 5 distributions, only 1 distribution has license", 1, stars.intValue());
	}
	
	@Test
	public void TestCase6() throws Exception {
		AvailabilityOfLicensesMetric metric = new AvailabilityOfLicensesMetric();
		Integer stars = metric.compute(testdata.getModel(TestCase6), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Dataset 5 distributions, only 2 distributions have rights", 2, stars.intValue());
	}

}