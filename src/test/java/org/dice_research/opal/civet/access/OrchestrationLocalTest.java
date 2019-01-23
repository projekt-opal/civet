package org.dice_research.opal.civet.access;

import static org.junit.Assert.assertFalse;

import java.net.URISyntaxException;

import org.apache.jena.rdf.model.Model;
import org.dice_research.opal.civet.Orchestration;
import org.dice_research.opal.civet.Utils;
import org.junit.Test;

public class OrchestrationLocalTest {

	@Test
	public void test() throws URISyntaxException {

		// Configure endpoint
		Orchestration orchestration = new Orchestration();
		Model sourceModel = Utils.readModel(Utils.MODEL_ZUGBILDUNGSPLAN, Utils.LANG_TURTLE);
		Model model = orchestration.compute(sourceModel);

		// TODO: Test concrete results
		assertFalse(sourceModel.isIsomorphicWith(model));
	}

}