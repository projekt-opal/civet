package org.dice_research.opal.civet;

import static org.junit.Assume.assumeTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.dice_research.opal.civet.metrics.ContactEmailMetric;
import org.dice_research.opal.civet.metrics.DescriptionMetric;
import org.junit.Test;

public class OrchestrationTest {

	// TODO: Put in properties file or so
	private String sparqlQueryEndpoint = "http://opalpro.cs.upb.de:8890";
	private String datasetUri = "http://europeandataportal.projekt-opal.de/dataset/0021";

	@Test
	public void test() throws MalformedURLException, IOException, URISyntaxException {
		Orchestration orchestration = new Orchestration();
		assumeTrue(new URL(sparqlQueryEndpoint).openConnection().getContentLength() > 0);

		Set<String> metrics = new HashSet<String>();
		metrics.add(new ContactEmailMetric().toString());
		metrics.add(new DescriptionMetric().toString());

		orchestration.setSparqlQueryEndpoint(sparqlQueryEndpoint);
		orchestration.compute(new URI(datasetUri), metrics);

		// TODO: go on.

	}

}
