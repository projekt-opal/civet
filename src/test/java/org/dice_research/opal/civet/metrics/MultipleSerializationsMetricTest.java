package org.dice_research.opal.civet.metrics;

import org.dice_research.opal.civet.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link MultipleSerializationsMetric}.
 *
 * @author Adrian Wilke
 */
public class MultipleSerializationsMetricTest {

	TestData testdata;

	private static final String TEST_EDP_ICE = "Europeandataportal-Iceland.ttl";
	private static final String TEST_EDP_ICE_DATASET = "http://projekt-opal.de/dataset/http___europeandataportal_eu_set_data__3dff988d_59d2_415d_b2da_818e8ef3111701";

	private static final String TEST_GOV_ALLER = "Govdata-Allermoehe.ttl";
	private static final String TEST_GOV_ALLER_DATASET = "http://projekt-opal.de/dataset/https___ckan_govdata_de_001c9703_3556_4f62_a376_85804f18ab52";

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
	}

	/**
	 * Tests example datasets.
	 */
	@Test
	public void testEdpIce() throws Exception {

		// Compute stars
		MultipleSerializationsMetric metric = new MultipleSerializationsMetric();
		Integer stars = metric.compute(testdata.getModel(TEST_EDP_ICE), TEST_EDP_ICE_DATASET);

		// No download URL provided
		// Should result in 0 stars
		Assert.assertEquals(TEST_EDP_ICE_DATASET, 0, stars.intValue());

		// Compute stars
		metric = new MultipleSerializationsMetric();
		stars = metric.compute(testdata.getModel(TEST_GOV_ALLER), TEST_GOV_ALLER_DATASET);

		// Several download URLs provided. File extension only pdf.
		// Several formats provided: [PDF, gml, HTML]
		// Should result in 5 stars
		Assert.assertEquals(TEST_GOV_ALLER_DATASET, 5, stars.intValue());
	}
}