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

	// Dataset has 1 distribution which has license info  ---> 5 Stars
	private static final String TestCase1 = "TestCaseForAL5Stars.ttl";
	
    //Dataset has 5 distributions and 3 distributions have license info ---> 3 Stars
	private static final String TestCase2 = "TestCaseForAL3Stars.ttl";

	// Dataset has 1 distribution which has no rights/license info ---> 0 Stars
	private static final String TestCase3 = "TestCaseForAL0stars.ttl";

	
	private static final String DATASET_URI_1 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_brownfield_land_register_blackburn_with_darwen";
	
	private static final String DATASET_URI_2 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_conservation_areas1";
	
	private static final String DATASET_URI_3 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_31303833_3231_4031_3130_323032344210";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}
	
	
	@Test
	public void TestCase1() throws Exception {

		AvailabilityOfLicensesMetric metric = new AvailabilityOfLicensesMetric();
		Integer stars = metric.compute(testdata.getModel(TestCase1), DATASET_URI_1);
		Assert.assertEquals("Dataset has in total 1 distribution which has license ", 5, stars.intValue());
	}
	
	@Test
	public void TestCase2() throws Exception {

		AvailabilityOfLicensesMetric metric = new AvailabilityOfLicensesMetric();
		Integer stars = metric.compute(testdata.getModel(TestCase2), DATASET_URI_2);
		Assert.assertEquals("Dataset has in total 5 distributions, only 3 have license ", 3, stars.intValue());
	}
	
	@Test
	public void TestCase3() throws Exception {

		AvailabilityOfLicensesMetric metric = new AvailabilityOfLicensesMetric();
		Integer stars = metric.compute(testdata.getModel(TestCase3), DATASET_URI_3);
		Assert.assertEquals("Dataset has in total 1 distribution which has no license ", 0, stars.intValue());
	}

	
}