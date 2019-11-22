package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.RDF;
import org.dice_research.opal.civet.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link VersionMetric}.
 */
public class VersioningMetricTest {

	TestData testdata;

	private static final String TEST_GOV_AMH = "Govdata-Allermoehe.ttl";
	private static final String TEST_GOV_AMH_DISTRIBUTION =
			"http://projekt-opal.de/distribution/https___ckan_govdata_de_aaacd662_f576_4f20_b05d_4076fe211375";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	/**
	 * Tests all 3 cases.
	 */
	@Test
	public void testCases() throws Exception {
		VersionMetric metric = new VersionMetric();

		Model model = ModelFactory.createDefaultModel();
		String datasetUri = "https://example.org/dataset-1";
		Resource distribution = ResourceFactory.createResource(datasetUri);
		model.add(distribution, RDF.type, DCAT.Distribution);

		Assert.assertEquals("No Version", null, metric.compute(model, datasetUri));

		model.addLiteral(distribution, DCAT.accessURL, ResourceFactory.createPlainLiteral("version"));
		Assert.assertEquals("Version Found", 5, metric.compute(model, datasetUri).intValue());

	}

	/**
	 * Tests all 3 cases.
	 */
	@Test
	public void testEdpIce() throws Exception {
		
		// Compute stars
		VersionMetric metric = new VersionMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_GOV_AMH), TEST_GOV_AMH_DISTRIBUTION);

		//Version is there
		//Expected 5
		Assert.assertEquals(TEST_GOV_AMH, 5, stars.intValue());
	}

}