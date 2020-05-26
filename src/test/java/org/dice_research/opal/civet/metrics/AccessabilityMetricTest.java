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
 * Tests {@link Accessibility}.
 * 
 * This files contain testcases to perform Unit testing on Accessibility.java 
 * 
 * @author Amit Kumar
 */
public class AccessabilityMetricTest {

	TestData testdata;
	private static final String TEST_EDP_ICE = "model4881.ttl";

	private static final String TEST_DATASET_RATING5_POSITIVE1 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_bsii3_zdos";
	
//	It should return a status code 200
	private static final String TEST_DATASET_RATING5_STATUS200 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_boundary_dataset_guidance_2001_to_20115";

//	It should return status code 301: “The requested resource has been moved permanently.”
	private static final String TEST_DATASET_STATUS301 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_bristol_city_council_contracts_tenders";
	
//	It should return status code 500: “There was an error on the server and the request could not be completed.”	
	private static final String TEST3_DATASET_STATUS500 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_bowling_greens";
	
//	It should return status code 400: Bad Request
	private static final String TEST4_DATASET_STATUS400 = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_brownfield_land_register_polygons";

	private static final String TEST4_DATASET_DOWNLOADURL = "http://projekt-opal.de/dataset/https___europeandataportal_eu_set_data_broad_rental_market_areas_brma1";
	
	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}
	
	@Test
	public void testRating4() throws Exception {
		// Compute stars
		AccessibilityMetric metric = new AccessibilityMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_EDP_ICE), TEST_DATASET_RATING5_POSITIVE1);
		Assert.assertEquals(TEST_EDP_ICE, 4, stars.intValue());
	}


	@Test
	public void testRatingDownloadURL() throws Exception {
		// Compute stars
		AccessibilityMetric metric = new AccessibilityMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_EDP_ICE), TEST4_DATASET_DOWNLOADURL);
		Assert.assertEquals(TEST_EDP_ICE, 5, stars.intValue());
	}
	@Test
	public void testRating5_1() throws Exception {
		// Compute stars
		AccessibilityMetric metric = new AccessibilityMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_EDP_ICE), TEST_DATASET_RATING5_STATUS200);
		Assert.assertEquals(TEST_EDP_ICE, 5, stars.intValue());
	}
	
	@Test
	public void testRating5_2() throws Exception {
		// Compute stars
		AccessibilityMetric metric = new AccessibilityMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_EDP_ICE), TEST_DATASET_STATUS301);
		Assert.assertEquals(TEST_EDP_ICE, 5, stars.intValue());
	}
	
	@Test
	public void testRating0() throws Exception {
		// Compute stars
		AccessibilityMetric metric = new AccessibilityMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_EDP_ICE), TEST3_DATASET_STATUS500);
		Assert.assertEquals(TEST_EDP_ICE, 0, stars.intValue());
	}
	
	@Test
	public void testRating2() throws Exception {
		AccessibilityMetric metric = new AccessibilityMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_EDP_ICE), TEST4_DATASET_STATUS400);
		Assert.assertEquals(TEST_EDP_ICE, 2, stars.intValue());
	}
}