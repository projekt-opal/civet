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
public class DataFormatMetricTest {

	TestData testdata;

	// In TestCase1, dataset has 4 distributions and all the distributions have a file format.
	private static final String TestCase1 = "TestCaseDataFormat5stars.ttl";

	// In TestCase2, 1 out of 2 distributions have file format information. This is a 3 Star dataset.
	private static final String TestCase2 = "TestCaseDataFormat3stars.ttl";
	
	// In TestCase3, 1 distribution which does not have any file format.
	private static final String TestCase3 = "TestCaseDataFormat0stars.ttl";

	private static final String dataset_URI_1 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_bilancio_previsione_2015_2017_entrate";
	private static final String dataset_URI_2 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_birth_registrations_by_month_since_january_2009_to_june_2011";
	private static final String dataset_URI_3 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_ac1218e9_2ae0_0002_ba5e_f88d969555ba";
	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	@Test
	public void TestCase1() throws Exception {

		DataFormatMetric metric = new DataFormatMetric();

		// Compute stars : TestCase1 check for 5 stars --------> MUST PASS
		Integer stars_test = metric.compute(testdata.getModel(TestCase1), dataset_URI_1);
		Assert.assertEquals("Data Format Test: All distributions have valid file format", 5, stars_test.intValue());
	}
	
	@Test
	public void TestCase2() throws Exception {

		DataFormatMetric metric = new DataFormatMetric();

		// Compute stars : TestCase2 check for 5 stars, but it is actually a 2 star dataset --------> MUST Pass
		Integer stars_test = metric.compute(testdata.getModel(TestCase2), dataset_URI_2);
		Assert.assertEquals("Data Format Test: Out of 2 distributions, only 1 has valid file format", 3, stars_test.intValue());
	}
	
	@Test
	public void TestCase3() throws Exception {

		DataFormatMetric metric = new DataFormatMetric();
		Integer stars_test = metric.compute(testdata.getModel(TestCase3), dataset_URI_3);
		Assert.assertEquals("Data Format Test: 1 distribution has no file format", 0, stars_test.intValue());
	}

}