package org.dice_research.opal.civet;

import org.dice_research.opal.civet.example.ExampleTest;
import org.dice_research.opal.civet.metrics.CategorizationMetricTest;
import org.dice_research.opal.civet.metrics.DataFormatMetricTest;
import org.dice_research.opal.civet.metrics.DateFormatMetricTest;
import org.dice_research.opal.civet.metrics.LicenseAvailabilityMetricTest;
import org.dice_research.opal.civet.metrics.MetadataQualityMetricTest;
import org.dice_research.opal.civet.metrics.MultipleSerializationsMetricTest;
import org.dice_research.opal.civet.metrics.ProviderIdentityMetricTest;
import org.dice_research.opal.civet.metrics.ReadabilityMetricTest;
import org.dice_research.opal.civet.metrics.RetrievabilityMetricTest;
import org.dice_research.opal.civet.metrics.TimelinessMetricTest;
import org.dice_research.opal.civet.metrics.UpdateRateMetricTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

		// Civet

		MetricComputationTest.class,

		// Metrics

		CategorizationMetricTest.class,

		DataFormatMetricTest.class,

		DateFormatMetricTest.class,

		LicenseAvailabilityMetricTest.class,

		MultipleSerializationsMetricTest.class,

		ProviderIdentityMetricTest.class,

		ReadabilityMetricTest.class,

		RetrievabilityMetricTest.class,

		TimelinessMetricTest.class,

		UpdateRateMetricTest.class,

		// Metric aggregation

		MetadataQualityMetricTest.class,

		// Test cases on all metrics

		TestCasesTest.class,

		// Minimal working example

		ExampleTest.class

})

public class AllTests {

}