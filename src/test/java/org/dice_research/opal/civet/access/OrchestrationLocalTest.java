package org.dice_research.opal.civet.access;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.dice_research.opal.civet.Orchestration;
import org.dice_research.opal.civet.Utils;
import org.dice_research.opal.common.vocabulary.Dqv;
import org.junit.Test;

public class OrchestrationLocalTest {

	@Test
	public void test() throws URISyntaxException {

		// Configure endpoint
		Orchestration orchestration = new Orchestration();
		Model sourceModel = Utils.readModel(Utils.MODEL_ZUGBILDUNGSPLAN, Utils.LANG_TURTLE);
		Model model = orchestration.compute(sourceModel);

		assertFalse(sourceModel.isIsomorphicWith(model));

		List<Resource> datasets = new LinkedList<>();
		ResIterator it = model.listResourcesWithProperty(org.apache.jena.vocabulary.DCAT.dataset);
		while (it.hasNext()) {
			datasets.add(it.next());
		}
		assertTrue(datasets.size() >= 1);

		// TODO: Test concrete results
		// TODO: Update Vocabulary (Upper/lower case)
//		NodeIterator nit = model.listObjectsOfProperty(datasets.get(0), Dqv.HAS_QUALITY_MEASUREMENT);
//		while (nit.hasNext()) {
//			System.out.println(nit.next());
//		}
//
//		datasets = new LinkedList<>();
//		it = model.listResourcesWithProperty(Dqv.HAS_QUALITY_MEASUREMENT);
//		while (it.hasNext()) {
//			System.out.println(it.next());
//		}

	}

}