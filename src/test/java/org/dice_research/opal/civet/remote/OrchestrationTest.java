package org.dice_research.opal.civet.remote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dice_research.opal.civet.Config;
import org.dice_research.opal.civet.Orchestration;
import org.dice_research.opal.civet.access.IoUtils;
import org.dice_research.opal.civet.aggregation.MetadataQualityAggregation;
import org.dice_research.opal.civet.metrics.DescriptionMetric;
import org.dice_research.opal.civet.metrics.KnownLicenseMetric;

public class OrchestrationTest {

	/**
	 * Tests calculation of description metric.
	 */
	@Deprecated
	public void test() throws URISyntaxException {

		// Configure endpoint
		Orchestration orchestration = new Orchestration();
		orchestration.getConfiguration().setSparqlQueryEndpoint(Config.sparqlQueryEndpoint);
		orchestration.getConfiguration().setNamedGraph(Config.namedGraph);
		pingEndpoint();

		// Set metrics
		Set<String> metrics = new HashSet<String>();
		String metricDescription = new DescriptionMetric().toString();
		metrics.add(metricDescription);
		// License metric needs distribution
		String licenseSpecifiedMetric = new KnownLicenseMetric().toString();
		metrics.add(licenseSpecifiedMetric);

		// Compute scores
		Map<String, Float> scores = orchestration.compute(new URI(Config.datasetUriBerlin), metrics);

		assertEquals(2, scores.size());
		assertTrue(scores.get(metricDescription) > 0);
		assertTrue(scores.get(licenseSpecifiedMetric) > 0);
	}

	/**
	 * Tests aggregation of metric scores.
	 * 
	 * @throws URISyntaxException
	 */
	public void testAggregation() throws URISyntaxException {

		// Configure endpoint
		Orchestration orchestration = new Orchestration();
		orchestration.getConfiguration().setSparqlQueryEndpoint(Config.sparqlQueryEndpoint);
		orchestration.getConfiguration().setSparqlUpdateEndpoint(Config.sparqlUpdateEndpoint);
		orchestration.getConfiguration().setNamedGraph(Config.namedGraph);
		pingEndpoint();

		// Set metrics
		Set<String> metrics = new HashSet<String>();
		String metricDescription = new DescriptionMetric().toString();
		metrics.add(metricDescription);
		// License metric needs distribution
		String licenseSpecifiedMetric = new KnownLicenseMetric().toString();
		metrics.add(licenseSpecifiedMetric);
		String metadataQualityAggregation = new MetadataQualityAggregation().toString();
		metrics.add(metadataQualityAggregation);

		// Process
		Map<String, Float> scores = orchestration.compute(new URI(Config.datasetUriBerlin), metrics);

		assertEquals(3, scores.size());

		assertTrue(scores.get(metricDescription) > 0);
		assertTrue(scores.get(licenseSpecifiedMetric) > 0);
		assertTrue(scores.get(metadataQualityAggregation) > 0);

	}

	/**
	 * Tests calculation using multiple datasets.
	 */
	public void testMultipleDatasets() {

		// Configure endpoint
		Orchestration orchestration = new Orchestration();
		orchestration.getConfiguration().setSparqlQueryEndpoint(Config.sparqlQueryEndpoint);
		orchestration.getConfiguration().setSparqlUpdateEndpoint(Config.sparqlUpdateEndpoint);
		orchestration.getConfiguration().setNamedGraph(Config.namedGraph);
		pingEndpoint();

		// Set metrics
		Set<String> metrics = new HashSet<String>();
		String metricDescription = new DescriptionMetric().toString();
		metrics.add(metricDescription);
		// License metric needs distribution
		String licenseSpecifiedMetric = new KnownLicenseMetric().toString();
		metrics.add(licenseSpecifiedMetric);

		// Process
		int offset = 0;
		int endOffset = 100;
		int limit = 40;
		int continuationOffset = orchestration.compute(offset, endOffset, limit, metrics);
		assumeTrue(endOffset < continuationOffset);
	}

	/**
	 * Checks, if host of endpoint answers.
	 */
	private void pingEndpoint() {
		boolean reachable = IoUtils.pingHost(Config.sparqlQueryEndpointHost, Config.sparqlQueryEndpointPort, 100);
		if (!reachable) {
			System.out.println(OrchestrationTest.class.getSimpleName() + ": could not access endpoint "
					+ Config.sparqlQueryEndpoint);
		}
		assumeTrue(reachable);
	}
}
