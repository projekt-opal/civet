package org.dice_research.opal.civet;

import org.dice_research.opal.civet.data.DataContainerTest;
import org.dice_research.opal.civet.data.DataObjectTests;
import org.dice_research.opal.civet.metrics.CategorizationMetricTest;
import org.dice_research.opal.civet.metrics.DescriptionMetricTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

		// Data

		DataObjectTests.class,

		DataContainerTest.class,

		// Access

// TODO: Too many requests overall. Endpoint does not react.		
//		OpalAccessorTest.class,

		// Metrics

		MetricImplementationTest.class,

		// Single metrics

		CategorizationMetricTest.class,

		DescriptionMetricTest.class,

		// Complete run

		OrchestrationTest.class })

public class AllTests {
}