package org.dice_research.opal.civet;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.test_cases.OpalTestCases;
import org.dice_research.opal.test_cases.TestCase;
import org.junit.Test;

/**
 * Tests all metrics with all test cases.
 * 
 * Used to be aware of possible exceptions at usual datasets.
 *
 * @author Adrian Wilke
 */
public class TestCasesTest {

	private static final Logger LOGGER = LogManager.getLogger();

	@Test
	public void test() throws Exception {
		List<Metric> metrics = new Civet().getMetrics();
		List<TestCase> testCases = OpalTestCases.getAllTestCases();

		long time = System.currentTimeMillis();
		int counter = 0;
		StringBuilder stringBuilder = new StringBuilder();

		for (Metric metric : metrics) {
			for (TestCase testCase : testCases) {
				stringBuilder.append(metric.compute(testCase.getModel(), testCase.getDatasetUri()));
				stringBuilder.append("  ");
				stringBuilder.append(metric.getUri());
				stringBuilder.append("  ");
				stringBuilder.append(testCase.getDatasetUri());
				stringBuilder.append("\n");
				counter++;
			}
		}

		long runtime = System.currentTimeMillis() - time;

		LOGGER.info("Executed " + counter + " tests for " + metrics.size() + " metrics and " + testCases.size()
				+ " test cases in " + runtime + " milliseconds.");

		// Print results
		if (Boolean.FALSE) {
			System.out.println(stringBuilder.toString());
		}
	}
}