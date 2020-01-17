package org.dice_research.opal.civet.metrics;

import org.dice_research.opal.civet.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link DataFormatMetric}.
 * 
 * @author Gourab Sahu, Adrian Wilke
 */
public class DataFormatMetricTest {

	TestData testdata;

	// In TestCase1, dataset has 4 distributions and all the distributions have a
	// file format.
	private static final String TEST_CASE_1 = "TestCaseDataFormat5stars.ttl";

	// In TestCase2, 1 out of 2 distributions have file format information. This is
	// a 3 Star dataset.
	private static final String TEST_CASE_2 = "TestCaseDataFormat3stars.ttl";

	// In TestCase3, 1 distribution which does not have any file format.
	private static final String TEST_CASE_3 = "TestCaseDataFormat0stars.ttl";

	private static final String DATASET_URI_1 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_bilancio_previsione_2015_2017_entrate";
	private static final String DATASET_URI_2 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_birth_registrations_by_month_since_january_2009_to_june_2011";
	private static final String DATASET_URI_3 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_ac1218e9_2ae0_0002_ba5e_f88d969555ba";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	//@Test
	public void TestCase1() throws Exception {

		DataFormatMetric metric = new DataFormatMetric();

		// Compute stars : TestCase1 check for 5 stars --------> MUST PASS
		Integer stars_test = metric.compute(testdata.getModel(TEST_CASE_1), DATASET_URI_1);
		Assert.assertEquals("Data Format Test: All distributions have valid file format", 5, stars_test.intValue());
	}

	//@Test
	public void TestCase2() throws Exception {

		DataFormatMetric metric = new DataFormatMetric();

		// Compute stars : TestCase2 check for 5 stars, but it is actually a 2 star
		// dataset --------> MUST Pass
		Integer stars_test = metric.compute(testdata.getModel(TEST_CASE_2), DATASET_URI_2);
		Assert.assertEquals("Data Format Test: Out of 2 distributions, only 1 has valid file format", 3,
				stars_test.intValue());
	}

	@Test
	public void TestCase3() throws Exception {

		DataFormatMetric metric = new DataFormatMetric();
		Integer stars_test = metric.compute(testdata.getModel(TEST_CASE_3), DATASET_URI_3);
		Assert.assertEquals("Data Format Test: 1 distribution has no file format", 0, stars_test.intValue());
	}

}