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
public class DateFormatTest2 {

	TestData testdata;

	private static final String dateTimeYearFormat = "Test_data_for_date_time_year_format.ttl";

	private static final String dateTimeFormat = "Test_data_for_date_time_format.ttl";

	private static final String dateFormat = "Test_data_for_date_format.ttl";

	private static final String yearFormat = "Test_data_for_year_format.ttl";

	private static final String nullData = "Test_data_for_null_data.ttl";
	
	private static final String invalidData = "Test_data_for_invalid_date_format.ttl";

	private static final String checkModifiedProperty = "Test_data_for_modified_property.ttl";

	private static final String dataSetUri = "http://projekt-opal.de/dataset/http___europeandataportal_eu_set_data__3dff988d_59d2_415d_b2da_818e8ef3111701";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	@Test
	public void dateTimeYearFormat() throws Exception {

		DateFormat2 metric = new DateFormat2();
		Integer stars = metric.compute(testdata.getModel(dateTimeYearFormat), dataSetUri);
		Assert.assertEquals("Dateformat for date_time_year", 5, stars.intValue());
	}

	@Test
	public void dateTimeFormat() throws Exception {

		DateFormat2 metric = new DateFormat2();
		Integer stars = metric.compute(testdata.getModel(dateTimeFormat), dataSetUri);
		Assert.assertEquals("Dateformat for date_time", 4, stars.intValue());
	}

	@Test
	public void dateFormat() throws Exception {

		DateFormat2 metric = new DateFormat2();
		Integer stars = metric.compute(testdata.getModel(dateFormat), dataSetUri);
		Assert.assertEquals("Dateformat for date", 3, stars.intValue());
	}

	@Test
	public void yearFormat() throws Exception {

		DateFormat2 metric = new DateFormat2();
		Integer stars = metric.compute(testdata.getModel(yearFormat), dataSetUri);
		Assert.assertEquals("Dateformat for year", 2, stars.intValue());
	}

	@Test
	public void nullData() throws Exception {

		DateFormat2 metric = new DateFormat2();
		Integer stars = metric.compute(testdata.getModel(nullData), dataSetUri);
		Assert.assertEquals("Dateformat for null", 1, stars.intValue());
	}
	
	@Test
	public void invalidData() throws Exception {

		DateFormat2 metric = new DateFormat2();
		Integer stars = metric.compute(testdata.getModel(invalidData), dataSetUri);
		Assert.assertEquals("Invalid date format", 1, stars.intValue());
	}
	
	@Test
	public void checkModifiedProperty() throws Exception {

		DateFormat2 metric = new DateFormat2();
		Integer stars = metric.compute(testdata.getModel(checkModifiedProperty), dataSetUri);
		Assert.assertEquals("Dateformat in dct:modified", 4, stars.intValue());
	}

}