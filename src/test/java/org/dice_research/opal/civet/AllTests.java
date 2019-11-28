package org.dice_research.opal.civet;

import org.dice_research.opal.civet.metrics.CategorizationMetricTest;
import org.dice_research.opal.civet.metrics.MultipleSerializationsMetricTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ MetricComputationTest.class,

		CategorizationMetricTest.class,

		MultipleSerializationsMetricTest.class

})
public class AllTests {

}