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
	public void testFormats() throws Exception {
		// YYYY (eg 1997)
		testFormat("yyyy", 5, System.currentTimeMillis());
		long lastYear = System.currentTimeMillis() - 1000l * 60 * 60 * 24 * 365;
		testFormat("yyyy", 5, lastYear);
		long twoYearsAgo = System.currentTimeMillis() - 1000l * 60 * 60 * 24 * 365 * 2;
		testFormat("yyyy", 2, twoYearsAgo);

		// YYYY-MM (eg 1997-07)
		testFormatThreeMonthAgo("yyyy-MM");
		testFormat("yyyy-MM", 5, System.currentTimeMillis());

		// YYYY-MM-DD (eg 1997-07-16)
		testFormatThreeMonthAgo("yyyy-MM-dd");

		// YYYY-MM-DDThh:mmTZD (eg 1997-07-16T19:20+01:00)
		testFormatThreeMonthAgo("YYYY-MM-dd'T'hh:mm'+01:00'");

		// YYYY-MM-DDThh:mm:ssTZD (eg 1997-07-16T19:20:30+01:00)
		testFormatThreeMonthAgo("YYYY-MM-dd'T'hh:mm:ss'+01:00'");

		// YYYY-MM-DDThh:mm:ss.sTZD (eg 1997-07-16T19:20:30.45+01:00)
		testFormatThreeMonthAgo("YYYY-MM-dd'T'hh:mm:ss'.45+01:00'");
	}

	protected Model getModelWithModifiedDate(String modifiedDate) throws IOException {
		Model model = ModelFactory.createDefaultModel().add(testCase.getModel());
		Resource dataset = model.getResource(testCase.getDatasetUri());
		return model.add(dataset, DCTerms.modified, ResourceFactory.createPlainLiteral(modifiedDate));
	}

	protected void testFormatThreeMonthAgo(String pattern) throws Exception {
		long threeMonthAgo = System.currentTimeMillis() - 1000l * 60 * 60 * 24 * 31 * 3;
		testFormat(pattern, 4, threeMonthAgo);
	}

	protected void testFormat(String pattern, int expectedStars, long timeToCheck) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String date = format.format(new Date(timeToCheck));
		Integer stars = new TimelinessMetric().compute(getModelWithModifiedDate(date), testCaseDatasetUri);
		Assert.assertEquals(pattern, expectedStars, stars.intValue());
	}
}