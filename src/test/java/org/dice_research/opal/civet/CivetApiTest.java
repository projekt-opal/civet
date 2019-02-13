package org.dice_research.opal.civet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Resource;
import org.dice_research.opal.civet.exceptions.CivetException;
import org.dice_research.opal.civet.metrics.CategorizationMetric;
import org.dice_research.opal.civet.metrics.ContactUrlMetric;
import org.dice_research.opal.civet.metrics.DescriptionMetric;
import org.dice_research.opal.civet.metrics.KnownLicenseMetric;
import org.dice_research.opal.civet.metrics.LinkedDataMetric;
import org.dice_research.opal.civet.metrics.MultipleSerializationsMetric;
import org.dice_research.opal.civet.metrics.TimelinessMetric;
import org.dice_research.opal.common.vocabulary.Dqv;
import org.junit.Test;

public class CivetApiTest {

	static final String contactUrl = new ContactUrlMetric().getResultsUri();
	static final String description = new DescriptionMetric().getResultsUri();
	static final String licenseSpecified = new KnownLicenseMetric().getResultsUri();
	static final String categorization = new CategorizationMetric().getResultsUri();
	static final String linkedDate = new LinkedDataMetric().getResultsUri();
	static final String multipleSerializations = new MultipleSerializationsMetric().getResultsUri();
	static final String linkedData = new LinkedDataMetric().getResultsUri();
	static final String timeliness = new TimelinessMetric().getResultsUri();

	/**
	 * Tests {@link CivetApi#compute(Model)}
	 */
	@Test
	public void testModel() throws Exception {

		Map<String, Float> metricResults = extractMetricResults(compute(Utils.MODEL_ZUGBILDUNGSPLAN));

		// Title and description
		// Description values are concatenated and should produce a long valuable
		// result.
		assertEquals(metricResults.get(description).floatValue(), 5f, 0);

		// Distribution license is given
		assertEquals(metricResults.get(licenseSpecified).floatValue(), 5f, 0);

		// Has one theme / category
		assertEquals(metricResults.get(categorization).floatValue(), 4f, 0);

		// Only one distribution and format
		assertEquals(metricResults.get(multipleSerializations).floatValue(), 0f, 0);

		// mcloud does not provide RDF
		assertEquals(metricResults.get(linkedData).floatValue(), 0f, 0);

		// issued 26.10.2018. 2 Stars for 6 month.
		assertEquals(metricResults.get(timeliness).floatValue(), 2f, 0);

		// URL is given
		assertEquals(metricResults.get(contactUrl).floatValue(), 5f, 0);

		// ---

		metricResults = extractMetricResults(compute(Utils.MODEL_STATIONSDATEN));
		assertTrue(metricResults.get(description) > 0);
		// Two formats
		assertEquals(metricResults.get(multipleSerializations).floatValue(), 4f, 0);

		metricResults = extractMetricResults(compute(Utils.MODEL_STRECKEN));
		assertTrue(metricResults.get(description) > 0);
		// 4 distributions
		assertEquals(metricResults.get(multipleSerializations).floatValue(), 1f, 0);

		metricResults = extractMetricResults(compute(Utils.MODEL_RHABABER));
		assertTrue(metricResults.get(description) > 0);

		metricResults = extractMetricResults(compute(Utils.MODEL_ALLERMOEHE));
		assertTrue(metricResults.get(description) > 0);
		// issued 2018-11-08. 2 Stars for 6 month.
		assertEquals(metricResults.get(timeliness).floatValue(), 2f, 0);

		metricResults = extractMetricResults(compute(Utils.MODEL_ICELAND));
		assertTrue(metricResults.get(description) > 0);
		// europeandataportal supports RDF
		assertEquals(metricResults.get(linkedData).floatValue(), 5f, 0);

		metricResults = extractMetricResults(compute(Utils.MODEL_DURCHSCHNITTSALTER));
		assertTrue(metricResults.get(description) > 0);
	}

	private Model compute(String modelResourceFile) throws CivetException {
		Model sourceModel = Utils.readModel(modelResourceFile, Utils.LANG_TURTLE);

		// Compute metrics
		Model model = new CivetApi().compute(sourceModel);

		// Assert model has changed
		assertFalse(sourceModel.isIsomorphicWith(model));

		return model;
	}

	/**
	 * Extracts all metric results for first dataset in model
	 */
	private Map<String, Float> extractMetricResults(Model model) {
		Map<String, Float> metricResults = new HashMap<>();

		// Get all datasets
		List<Resource> datasets = new LinkedList<>();
		NodeIterator datasetIterator = model.listObjectsOfProperty(org.apache.jena.vocabulary.DCAT.dataset);
		while (datasetIterator.hasNext()) {
			datasets.add(model.getResource(datasetIterator.next().toString()));
		}

		// Assert that at least one dataset is available
		assertTrue(datasets.size() >= 1);

		// Compute metric results for first dataset
		NodeIterator blankIterator = model.listObjectsOfProperty(datasets.get(0), Dqv.HAS_QUALITY_MEASUREMENT);
		while (blankIterator.hasNext()) {
			Resource blank = blankIterator.next().asResource();
			metricResults.put(blank.getPropertyResourceValue(Dqv.IS_MEASUREMENT_OF).getURI(),
					blank.getProperty(Dqv.HAS_VALUE).getLiteral().getFloat());
		}
		return metricResults;
	}

	/**
	 * Tests {@link CivetApi#computeFuture(Model)}
	 */
	@Test
	public void testModelFuture() throws Exception {
		Model sourceModel = Utils.readModel(Utils.MODEL_ZUGBILDUNGSPLAN, Utils.LANG_TURTLE);
		Future<Model> modelFuture = new CivetApi().computeFuture(sourceModel);
		while (!modelFuture.isDone()) {
			System.out.println(CivetApiTest.class.getSimpleName() + " is computing future model");
			Thread.sleep(200);
		}
		Model model = modelFuture.get();

		// Model was changed
		assertNotNull(model);
		assertFalse(sourceModel.isIsomorphicWith(model));
	}
}