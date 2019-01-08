package org.dice_research.opal.civet;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

		// Access

// TODO: Too many requests overall. Endpoint does not react.		
//		OpalAccessorTest.class,

		// Complete run

		OrchestrationTest.class })

public class RemoteTests {
}