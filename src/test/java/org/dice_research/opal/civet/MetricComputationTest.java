package org.dice_research.opal.civet;

import org.apache.jena.rdf.model.Model;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link Civet}.
 *
 * @author Adrian Wilke
 */
public class MetricComputationTest {

	TestData testdata;

	private static final String TEST_EDP_ICE = "Europeandataportal-Iceland.ttl";
	private static final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/http___europeandataportal_eu_set_data__3dff988d_59d2_415d_b2da_818e8ef3111701";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	/**
	 * Tests computation of metric(s) and extension of the related model.
	 */
	@Test
	public void testComputation() throws Exception {

		Model model = testdata.getModel(TEST_EDP_ICE);
		Model returnModel = new Civet().process(model, TEST_EDP_ICE_DATASET);

		Assert.assertTrue("Model contains additional statements.", model.size() < returnModel.size());
	}

	/**
	 * Tests computation of metric(s) and extension of the related model.
	 */
	@Test
	public void testRemove() throws Exception {

		Model model = testdata.getModel(TEST_EDP_ICE);

		// Will add measurements
		Model extendedModel = new Civet().process(model, TEST_EDP_ICE_DATASET);
		Assert.assertNotSame("Measurements added.", model.size(), extendedModel.size());

		// Will remove existing measurements and add them again
		Model reprocessedModel = new Civet().process(extendedModel, TEST_EDP_ICE_DATASET);
		Assert.assertEquals("Measurements removed and added.", extendedModel.size(), reprocessedModel.size());

		// Will add additional measurements (duplicates)
		Civet civet = new Civet();
		civet.setRemoveMeasurements(false);
		reprocessedModel = civet.process(extendedModel, TEST_EDP_ICE_DATASET);
		Assert.assertNotSame("Measurements added twice.", extendedModel.size(), reprocessedModel.size());
	}
}