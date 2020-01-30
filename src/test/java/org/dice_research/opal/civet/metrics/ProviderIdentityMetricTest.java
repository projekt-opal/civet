package org.dice_research.opal.civet.metrics;

import org.dice_research.opal.civet.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ProviderIdentityMetric}.
 * 
 * @author Gourab Sahu, Adrian Wilke
 */
public class ProviderIdentityMetricTest {

	TestData testdata;

	private static final String TEST0 = "TestCaseForPI_0Stars__synthetic.ttl";
	private static final String TEST2 = "TestCaseForPI_Access_URL_3Stars__synthetic.ttl";
	private static final String TEST3 = "TestCaseForPIs_LPatDataset_5star.ttl";

	private static final String URI0 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_c0e483fd_9d29_328f_9b83_9eb950c9f022";
	private static final String URI2 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_c0e483fd_9d29_328f_9b83_9eb950c9f022";
	private static final String URI3 = "http://projekt-opal.de/dataset/https___ckan_govdata_de_a642d7e5_bf1c_57f9_89df_cdad67c7c0fc";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	/*
	 * No publisher information at all
	 */
	@Test
	public void TestCase0() throws Exception {
		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		Integer stars = metric.compute(testdata.getModel(TEST0), URI0);
		Assert.assertEquals("Publisher Identity Test: Test Case 0 stars", 0, stars.intValue());
	}

	/*
	 * Dataset no dct:publisher and no accessURL at distributions but the dataset
	 * has a landingPage.
	 */
	@Test
	public void TestCase1() throws Exception {
		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		Integer stars = metric.compute(testdata.getModel(TEST3), URI3);
		Assert.assertEquals("Publisher Identity Test: Test Case 5 stars", 3, stars.intValue());
		if (Boolean.FALSE)
			testdata.getModel(TEST3).write(System.out, "TURTLE");
	}

	/*
	 * The dataset has no dcat:landingPage, no dct:publisher and out of 2
	 * distributions only 1 has dcat:accessURL. 3/2=1.5 -> 2
	 */
	@Test
	public void TestCase2() throws Exception {
		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		Integer stars = metric.compute(testdata.getModel(TEST2), URI2);
		Assert.assertEquals("Publisher Identity Test: Test Case 3 stars", 2, stars.intValue());
		if (Boolean.FALSE)
			testdata.getModel(TEST2).write(System.out, "TURTLE");
	}

}