package org.dice_research.opal.civet;

import org.dice_research.opal.civet.access.CivetApiTest;
import org.dice_research.opal.civet.data.DataContainerTest;
import org.dice_research.opal.civet.data.DataObjectTests;
import org.dice_research.opal.civet.metrics.CategorizationMetricTest;
import org.dice_research.opal.civet.metrics.DescriptionMetricTest;
import org.dice_research.opal.civet.metrics.LicenseSpecifiedTest;
import org.dice_research.opal.civet.metrics.MetricImplementationTest;
import org.dice_research.opal.civet.metrics.UpdateRateMetricTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

		// Orchestration

		CivetApiTest.class,

		// Data

		DataObjectTests.class,

		DataContainerTest.class,

		// Metrics

		MetricImplementationTest.class,

		// Single metrics

		CategorizationMetricTest.class,

		DescriptionMetricTest.class,

		UpdateRateMetricTest.class,

		LicenseSpecifiedTest.class

})

public class LocalTests {
}