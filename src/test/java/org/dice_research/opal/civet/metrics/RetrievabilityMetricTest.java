package org.dice_research.opal.civet.metrics;

import java.net.URL;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCAT;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link RetrievabilityMetric}.
 * 
 * @author Adrian Wilke
 */
public class RetrievabilityMetricTest {

	private class RetrievabilityTestMetric extends RetrievabilityMetric {
		@Override
		protected int getResponseCode(URL url) {
			if (url.toString().endsWith("100")) {
				return 100;
			} else if (url.toString().endsWith("200")) {
				return 200;
			} else if (url.toString().endsWith("300")) {
				return 300;
			} else if (url.toString().endsWith("400")) {
				return 400;
			} else {
				return 500;
			}
		}
	}

	public static final String PREFIX = "http://example.org/";
	public static final String URL_DATASET = PREFIX + "dataset";
	public static final String URL_DISTRIBUTION = PREFIX + "distribution";
	public static final String URL_WP_LOGO = "https://en.wikipedia.org/static/images/project-logos/enwiki.png";
	public static final String URL_NOT_EXISTS = "https://example.org/dataset.zip";

	private Model getTestModel(String downloadUrl, String accessUrl) {
		Model model = ModelFactory.createDefaultModel();
		Resource dataset = ResourceFactory.createResource(URL_DATASET);
		Resource distribution = ResourceFactory.createResource(URL_DISTRIBUTION);
		model.add(dataset, DCAT.distribution, distribution);

		if (downloadUrl != null) {
			model.add(distribution, DCAT.downloadURL, downloadUrl);
		}

		if (accessUrl != null) {
			model.add(distribution, DCAT.accessURL, accessUrl);
		}

		return model;
	}

	@Test
	public void testAccessUrl() throws Exception {
		RetrievabilityMetric metric = new RetrievabilityTestMetric();
		int stars = metric.compute(getTestModel(null, PREFIX + "access200"), URL_DATASET);
		Assert.assertEquals("no downloadUrl", 5, stars);
	}

	@Test
	public void testRealUrl() throws Exception {
		RetrievabilityMetric metric = new RetrievabilityMetric();
		int stars = metric.compute(getTestModel(URL_WP_LOGO, null), URL_DATASET);
		Assert.assertEquals("Wikipedia URL test", 5, stars);
	}

	@Test
	public void testRealNotExist() throws Exception {
		RetrievabilityMetric metric = new RetrievabilityMetric();
		int stars = metric.compute(getTestModel(URL_NOT_EXISTS, null), URL_DATASET);
		Assert.assertEquals("Wikipedia URL test", 1, stars);
	}

	@Test
	public void testAverage() throws Exception {
		Model model = ModelFactory.createDefaultModel();
		Resource dataset = ResourceFactory.createResource(URL_DATASET);
		Resource distribution = ResourceFactory.createResource(URL_DISTRIBUTION);
		Resource distributionB = ResourceFactory.createResource(URL_DISTRIBUTION + "B");
		model.add(dataset, DCAT.distribution, distribution);
		model.add(dataset, DCAT.distribution, distributionB);
		model.add(distribution, DCAT.downloadURL, PREFIX + "download200");
		model.add(distributionB, DCAT.accessURL, PREFIX + "access500");

		RetrievabilityMetric metric = new RetrievabilityTestMetric();
		int stars = metric.compute(model, URL_DATASET);
		Assert.assertEquals("average of two distributions", 3, stars);
	}

	@Test
	public void testCodesDownloadUrl() throws Exception {
		RetrievabilityMetric metric;
		String suffix;
		int stars;

		suffix = "download100";
		metric = new RetrievabilityTestMetric();
		stars = metric.compute(getTestModel(PREFIX + suffix, null), URL_DATASET);
		Assert.assertEquals(suffix, 4, stars);

		suffix = "download200";
		metric = new RetrievabilityTestMetric();
		stars = metric.compute(getTestModel(PREFIX + suffix, null), URL_DATASET);
		Assert.assertEquals(suffix, 5, stars);

		suffix = "download300";
		metric = new RetrievabilityTestMetric();
		stars = metric.compute(getTestModel(PREFIX + suffix, null), URL_DATASET);
		Assert.assertEquals(suffix, 4, stars);

		suffix = "download400";
		metric = new RetrievabilityTestMetric();
		stars = metric.compute(getTestModel(PREFIX + suffix, null), URL_DATASET);
		Assert.assertEquals(suffix, 1, stars);

		suffix = "download500";
		metric = new RetrievabilityTestMetric();
		stars = metric.compute(getTestModel(PREFIX + suffix, null), URL_DATASET);
		Assert.assertEquals(suffix, 1, stars);
	}

}