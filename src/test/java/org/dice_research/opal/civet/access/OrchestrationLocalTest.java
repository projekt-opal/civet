package org.dice_research.opal.civet.access;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Resource;
import org.dice_research.opal.civet.Orchestration;
import org.dice_research.opal.civet.Utils;
import org.dice_research.opal.civet.metrics.CategorizationMetric;
import org.dice_research.opal.civet.metrics.DescriptionMetric;
import org.dice_research.opal.civet.metrics.LicenseSpecifiedMetric;
import org.dice_research.opal.civet.metrics.UpdateRateMetric;
import org.dice_research.opal.common.vocabulary.Dqv;
import org.junit.Test;

public class OrchestrationLocalTest {

	@Test
	public void test() throws URISyntaxException {

		// Configure endpoint
		Orchestration orchestration = new Orchestration();
		Model sourceModel = Utils.readModel(Utils.MODEL_ZUGBILDUNGSPLAN, Utils.LANG_TURTLE);
		Model model = orchestration.compute(sourceModel);

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

}