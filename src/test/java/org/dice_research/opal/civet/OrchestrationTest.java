package org.dice_research.opal.civet;

import static org.junit.Assume.assumeTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.dice_research.opal.civet.access.IoUtils;
import org.dice_research.opal.civet.metrics.ContactEmailMetric;
import org.dice_research.opal.civet.metrics.DescriptionMetric;
import org.junit.Test;

public class OrchestrationTest {

	// TODO: Put in properties file or so
	private String sparqlQueryEndpointHost = "opalpro.cs.upb.de";
	private int sparqlQueryEndpointPort = 8890;
	private String sparqlQueryEndpointPath = "/sparql";
	private String sparqlQueryEndpoint = "http://" + sparqlQueryEndpointHost + ":" + sparqlQueryEndpointPort
			+ sparqlQueryEndpointPath;
	private String datasetUri = "http://europeandataportal.projekt-opal.de/dataset/0021";

	@Test
	public void test() throws MalformedURLException, IOException, URISyntaxException {
		Orchestration orchestration = new Orchestration();
		boolean reachable = IoUtils.pingHost(sparqlQueryEndpointHost, sparqlQueryEndpointPort, 100);
		if (!reachable) {
			System.out.println(
					OrchestrationTest.class.getSimpleName() + ": could not access endpoint " + sparqlQueryEndpoint);
		}
		assumeTrue(reachable);

		Set<String> metrics = new HashSet<String>();
		metrics.add(new ContactEmailMetric().toString());
		metrics.add(new DescriptionMetric().toString());
		orchestration.setSparqlQueryEndpoint(sparqlQueryEndpoint);
		orchestration.compute(new URI(datasetUri), metrics);

		// TODO: go on.

	}
}
