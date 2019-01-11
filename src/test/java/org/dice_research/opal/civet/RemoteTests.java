package org.dice_research.opal.civet;

import org.dice_research.opal.civet.access.OpalAccessorTest;
import org.dice_research.opal.civet.access.OrchestrationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

		// Access

		OpalAccessorTest.class,

		// Complete run

		OrchestrationTest.class })

public class RemoteTests {
}