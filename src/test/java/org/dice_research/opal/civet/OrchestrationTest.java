package org.dice_research.opal.civet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dice_research.opal.civet.access.IoUtils;
import org.dice_research.opal.civet.metrics.DescriptionMetric;
import org.junit.Test;

public class OrchestrationTest {

	@Test
	public void test() throws URISyntaxException {

		// Check if endpoint is reachable
		Orchestration orchestration = new Orchestration();
		boolean reachable = IoUtils.pingHost(Config.sparqlQueryEndpointHost, Config.sparqlQueryEndpointPort, 100);
		if (!reachable) {
			System.out.println(OrchestrationTest.class.getSimpleName() + ": could not access endpoint "
					+ Config.sparqlQueryEndpoint);
		}
		assumeTrue(reachable);

		// Metrics to use
		String metricDescription = new DescriptionMetric().toString();

		// Compute scores
		Set<String> metrics = new HashSet<String>();
		metrics.add(metricDescription);
		orchestration.getConfiguration().setSparqlQueryEndpoint(Config.sparqlQueryEndpoint);
		Map<String, Float> scores = orchestration.compute(new URI(Config.datasetUriEuroPortal), metrics);

		assertEquals(1, scores.size());
		assertTrue(scores.get(metricDescription) > 0);
	}
}
