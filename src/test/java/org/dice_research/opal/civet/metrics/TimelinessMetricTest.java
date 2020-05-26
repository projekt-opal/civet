package org.dice_research.opal.civet.metrics;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCTerms;
import org.dice_research.opal.test_cases.OpalTestCases;
import org.dice_research.opal.test_cases.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link TimelinessMetric}.
 *
 * @author Adrian Wilke
 */
public class TimelinessMetricTest {

	TestCase testCase;
	String testCaseDatasetUri;

	@Before
	public void setUp() throws Exception {
		testCase = OpalTestCases.getTestCase("edp-2019-12-17", "mittenwalde");
		testCaseDatasetUri = testCase.getDatasetUri();
	}

	@Test
	public void testTimes() throws Exception {
		long now = System.currentTimeMillis();

		long day = 1000l * 60 * 60 * 24;
		long month = day * 31;
		long year = day * 365;

		testFormat("yyyy-MM-dd", 5, now - 15 * day);
		testFormat("yyyy-MM-dd", 4, now - 3 * month);
		testFormat("yyyy-MM-dd", 3, now - 9 * month);
		testFormat("yyyy-MM-dd", 2, now - (1 * year + 3 * month));
		testFormat("yyyy-MM-dd", 1, now - (2 * year + 3 * month));
		testFormat("yyyy-MM-dd", 0, now - (3 * year + 3 * month));
	}

	@Test
	public void testFormats() throws Exception {

		// YYYY (eg 1997)
		testFormatThreeMonthAgo("yyyy");

		// YYYY-MM (eg 1997-07)
		testFormatThreeMonthAgo("yyyy-MM");

		// YYYY-MM-DD (eg 1997-07-16)
		testFormatThreeMonthAgo("yyyy-MM-dd");

		// YYYY-MM-DDThh:mmTZD (eg 1997-07-16T19:20+01:00)
		testFormatThreeMonthAgo("YYYY-MM-dd'T'hh:mm'+01:00'");

		// YYYY-MM-DDThh:mm:ssTZD (eg 1997-07-16T19:20:30+01:00)
		testFormatThreeMonthAgo("YYYY-MM-dd'T'hh:mm:ss'+01:00'");

		// YYYY-MM-DDThh:mm:ss.sTZD (eg 1997-07-16T19:20:30.45+01:00)
		testFormatThreeMonthAgo("YYYY-MM-dd'T'hh:mm:ss'.45+01:00'");
	}

	/**
	 * Creates date which is 3 month before now.
	 * 
	 * Tests if the date combined with the given formatPattern results in 4 stars.
	 */
	protected void testFormatThreeMonthAgo(String formatPattern) throws Exception {
		long threeMonthAgo = System.currentTimeMillis() - 1000l * 60 * 60 * 24 * 31 * 3;
		testFormat(formatPattern, 4, threeMonthAgo);
	}

	/**
	 * Creates date string based on formatPattern and modelMillis.
	 * 
	 * Tests if the created date results in expectedStars.
	 */
	protected void testFormat(String formatPattern, int expectedStars, long modelMillis) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(formatPattern);
		String date = format.format(new Date(modelMillis));

		Integer stars = new TimelinessMetric().compute(getModel(date), testCaseDatasetUri);
		Assert.assertEquals(formatPattern, expectedStars, stars.intValue());
	}

	/**
	 * Returns model with triple: (dataset, DCTerms.modified, dateString).
	 */
	protected Model getModel(String dateString) throws IOException {
		Model model = ModelFactory.createDefaultModel().add(testCase.getModel());
		Resource dataset = model.getResource(testCase.getDatasetUri());
		return model.add(dataset, DCTerms.modified, ResourceFactory.createPlainLiteral(dateString));
	}
}