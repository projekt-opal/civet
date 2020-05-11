package org.dice_research.opal.civet.metrics;

import org.dice_research.opal.civet.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link Dateformat}.
 * 
 * @author Aamir Mohammed
 */
public class DateformatTest2 {

	TestData testdata;

	// Date format one
	private static final String TestCase1 = "Test_data_for_dateformat_one.ttl";

	// Date format two
	private static final String TestCase2 = "Test_data_for_dateformat_two.ttl";

	// Date format three
	private static final String TestCase3 = "Test_data_for_dateformat_three.ttl";

	// Date format four
	private static final String TestCase4 = "Test_data_for_dateformat_four.ttl";

	// Date format five
	private static final String TestCase5 = "Test_data_for_dateformat_five.ttl";

	private static final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/http___europeandataportal_eu_set_data__3dff988d_59d2_415d_b2da_818e8ef3111701";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	@Test
	public void TestCase1() throws Exception {

		Dateformat2 metric = new Dateformat2();
		Integer stars = metric.compute(testdata.getModel(TestCase1), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Dateformat one", 5, stars.intValue());
	}

	@Test
	public void TestCase2() throws Exception {

		Dateformat2 metric = new Dateformat2();
		Integer stars = metric.compute(testdata.getModel(TestCase2), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Dateformat two", 4, stars.intValue());
	}

	@Test
	public void TestCase3() throws Exception {
		
		Dateformat2 metric = new Dateformat2();
		Integer stars = metric.compute(testdata.getModel(TestCase3), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Dateformat three", 3, stars.intValue());
	}

	@Test
	public void TestCase4() throws Exception {
		
		Dateformat2 metric = new Dateformat2();
		Integer stars = metric.compute(testdata.getModel(TestCase4), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Dateformat four", 2, stars.intValue());
	}

	@Test
	public void TestCase5() throws Exception {

		Dateformat2 metric = new Dateformat2();
		Integer stars = metric.compute(testdata.getModel(TestCase5), TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Dateformat five", 1, stars.intValue());
	}

}