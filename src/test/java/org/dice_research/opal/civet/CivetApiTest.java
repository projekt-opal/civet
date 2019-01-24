package org.dice_research.opal.civet;

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
import org.dice_research.opal.civet.metrics.CategorizationMetric;
import org.dice_research.opal.civet.metrics.DescriptionMetric;
import org.dice_research.opal.civet.metrics.LicenseSpecifiedMetric;
import org.dice_research.opal.civet.metrics.UpdateRateMetric;
import org.dice_research.opal.common.vocabulary.Dqv;
import org.junit.Test;

public class CivetApiTest {

	/**
	 * Tests {@link CivetApi#compute(Model)}
	 */
	@Test
	public void testModel() throws Exception {

		Model sourceModel = Utils.readModel(Utils.MODEL_ZUGBILDUNGSPLAN, Utils.LANG_TURTLE);
		Model model = new CivetApi().compute(sourceModel);

		// Model was changed
		assertFalse(sourceModel.isIsomorphicWith(model));

		// At least one dataset
		List<Resource> datasets = new LinkedList<>();
		NodeIterator datasetIterator = model.listObjectsOfProperty(org.apache.jena.vocabulary.DCAT.dataset);
		while (datasetIterator.hasNext()) {
			datasets.add(model.getResource(datasetIterator.next().toString()));
		}
		assertTrue(datasets.size() >= 1);

		// Get metric results
		Map<String, Float> metricResults = new HashMap<>();
		NodeIterator blankIterator = model.listObjectsOfProperty(datasets.get(0), Dqv.HAS_QUALITY_MEASUREMENT);
		while (blankIterator.hasNext()) {
			Resource blank = blankIterator.next().asResource();
			metricResults.put(blank.getPropertyResourceValue(Dqv.IS_MEASUREMENT_OF).getURI(),
					blank.getProperty(Dqv.HAS_VALUE).getLiteral().getFloat());
		}

		// Has long description
		// TODO: Has multiple descriptions
		assertTrue(metricResults.get(new DescriptionMetric().getResultsUri()) > 3);

		// Distribution license is given
		assertTrue(metricResults.get(new LicenseSpecifiedMetric().getResultsUri()) == 5);

		// Has one theme / category
		assertTrue(metricResults.get(new CategorizationMetric().getResultsUri()) == 4);

		// TODO
		assertTrue(metricResults.get(new UpdateRateMetric().getResultsUri()) == 0);
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