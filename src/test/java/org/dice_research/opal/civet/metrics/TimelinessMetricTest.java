package org.dice_research.opal.civet.metrics;

import org.dice_research.opal.civet.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link TimelinessMetric}.
 */
public class TimelinessMetricTest {

	TestData testdata;

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	@Test
	public void test1Stars() throws Exception {
		//DCTerms.modified -> updated between 2 year and 4 year
		final String TEST_FILE = "TestData.ttl";
		final String TEST_DATASET = "http://projekt-opal.de/distribution/https___europeandataportal_eu_set_distribution_d57ee02f_fb9d_4c5e_9ec7_80cf495d19b7";

		TimelinessMetric metric = new TimelinessMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_FILE), TEST_DATASET);
		Assert.assertEquals(TEST_FILE, 1, stars.intValue());
	}

	@Test
	public void test2Stars() throws Exception {
		//DCTerms.modified -> updated between 1 year and 2 year
		TimelinessMetric metric = new TimelinessMetric();
		final String TEST_FILE = "TestData.ttl";
		final String TEST_DATASET = "http://projekt-opal.de/distribution/https___europeandataportal_eu_set_distribution_18d846ba_45c2_483f_9a59_a776d15b8dad";

		Integer stars = metric.compute(testdata.getModel(TEST_FILE), TEST_DATASET);
		Assert.assertEquals(TEST_FILE, 2, stars.intValue());
	}

	@Test
	public void test3Stars() throws Exception {
		//DCTerms.modified -> updated between 2 months and an year
		final String TEST_FILE = "TestData.ttl";
		final String TEST_DATASET = "http://projekt-opal.de/distribution/https___europeandataportal_eu_set_distribution_ab5cbaa0_7a95_471f_94a1_502c8fe55170";

		TimelinessMetric metric = new TimelinessMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_FILE), TEST_DATASET);
		Assert.assertEquals(TEST_FILE, 3, stars.intValue());
	}


	@Test
	public void test5Stars() throws Exception {
		//DCTerms.modified -> updated between now and 7 days
		final String TEST_FILE = "TestData.ttl";
		final String TEST_DATASET = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_de6f2062_c0f9_4bb3_858d_d722250b322b";

		TimelinessMetric metric = new TimelinessMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_FILE), TEST_DATASET);
		Assert.assertEquals(TEST_FILE, 5, stars.intValue());

	}
}