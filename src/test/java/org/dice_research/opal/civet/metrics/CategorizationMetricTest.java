package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.RDF;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link CategorizationMetric}.
 * 
 * @author Adrian Wilke
 */
public class CategorizationMetricTest {

	/**
	 * Tests all 3 cases.
	 */
	@Test
	public void test() throws Exception {
		CategorizationMetric metric = new CategorizationMetric();

		Model model = ModelFactory.createDefaultModel();
		String datasetUri = "https://example.org/dataset-1";
		Resource dataset = ResourceFactory.createResource(datasetUri);
		model.add(dataset, RDF.type, DCAT.Dataset);

		Assert.assertEquals("No keywords", 0, metric.compute(model, datasetUri).intValue());

		model.addLiteral(dataset, DCAT.keyword, ResourceFactory.createPlainLiteral("keyword-1"));
		Assert.assertEquals("One keyword", 4, metric.compute(model, datasetUri).intValue());

		model.addLiteral(dataset, DCAT.keyword, ResourceFactory.createPlainLiteral("keyword-2"));
		Assert.assertEquals("Two keywords", 5, metric.compute(model, datasetUri).intValue());
	}

}