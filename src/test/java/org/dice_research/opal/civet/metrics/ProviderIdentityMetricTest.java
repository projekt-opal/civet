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

	/*
	 *  Dataset no dct:publisher and no accessURL at distributions but 
	 *  the dataset has a landingPage which as per Data Catalog can be 
	 *  treated as publisher information ---> 5 stars
	 */
	private static final String TestCase_5stars = "TestCaseForPIs_LPatDataset_5star.ttl";
	
	
	/*
	 * The dataset has no dcat:landingPage, no dct:publisher and out of 
	 * 2 distributions only 1 has dcat:accessURL. As per Data Catalog, 
	 * accessURL can be used as landingPage is distributions are accessed 
	 * only through accessURL.
	 */
	private static final String TestCase_3stars = "TestCaseForPI_Access_URL_3Stars__synthetic.ttl";
	
	
	/*
	 * No publisher information at all
	 */
	private static final String TestCase_0stars = "TestCaseForPI_0Stars__synthetic.ttl";

	private static final String TEST_dataset_5stars = "http://projekt-opal.de/dataset/https___ckan_govdata_de_a642d7e5_bf1c_57f9_89df_cdad67c7c0fc";
	private static final String TEST_dataset_3stars = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_c0e483fd_9d29_328f_9b83_9eb950c9f022";
	private static final String TEST_dataset_0stars = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_c0e483fd_9d29_328f_9b83_9eb950c9f022";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	@Test
	public void TestCase1() throws Exception {

		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		Integer stars = metric.compute(testdata.getModel(TestCase_5stars), TEST_dataset_5stars);
		Assert.assertEquals("Provider Identity Test: Test Case 5 stars", 5, stars.intValue());
	}
	
	@Test
	public void TestCase2() throws Exception {

		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		Integer stars = metric.compute(testdata.getModel(TestCase_3stars), TEST_dataset_3stars);
		Assert.assertEquals("Provider Identity Test: Test Case 3 stars", 3, stars.intValue());
	}
	
	@Test
	public void TestCase3() throws Exception {

		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		Integer stars = metric.compute(testdata.getModel(TestCase_0stars), TEST_dataset_0stars);
		Assert.assertEquals("Provider Identity Test: Test Case 0 stars", 0, stars.intValue());
	}

}