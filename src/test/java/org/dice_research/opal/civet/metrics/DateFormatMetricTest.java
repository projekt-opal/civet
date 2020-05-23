package org.dice_research.opal.civet.metrics;

import org.apache.jena.ext.com.google.common.annotations.VisibleForTesting;
import org.apache.jena.rdf.model.Model;
import org.dice_research.opal.civet.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link Dateformat}.
 * 
 * @author Aamir Mohammed
 */
public class DateFormatMetricTest {

	@VisibleForTesting
	TestData testdata;
	@VisibleForTesting
	final String MODEL = "TestDateFormatMetric.ttl";
	@VisibleForTesting
	Model model;

	@Before
	public void setUp() throws Exception {
		testdata = new TestData();
		model = testdata.getModel(MODEL);
	}

	@Test
	public void correctFormat() throws Exception {
		// dataset and distribution contains all correct format
		DateFormatMetric metric = new DateFormatMetric();
		final String datasetUri = "http://projekt-opal.de/dataset/_mcloudde_standortederdauerzhlstellenradverkehrindsseldorf";
		Integer stars = metric.compute(model, datasetUri);
		Assert.assertEquals("It contains all correct format", 5, stars.intValue());
	}

	@Test
	public void mostCorrectFormat() throws Exception {
		// dataset contains two correct formats, distribution contains two
		// correct and one incorrect format
		DateFormatMetric metric = new DateFormatMetric();
		final String datasetUri = "http://projekt-opal.de/dataset/_mcloudde_vieljhrlicherasterdesmittlerenvegetationsbeginnsindeutschland";
		Integer stars = metric.compute(model, datasetUri);
		Assert.assertEquals("It contains almost correct format", 4, stars.intValue());
	}

	@Test
	public void someCorrectFormat() throws Exception {
		// dataset contains two correct formats, distribution contains two
		// correct and two incorrect formats
		DateFormatMetric metric = new DateFormatMetric();
		final String datasetUri = "http://projekt-opal.de/dataset/_mcloudde_gerastertetglichemittlerebodennahelufttemperaturfreuropaprojektdecregmiklip-versionv001abgelstever";
		Integer stars = metric.compute(model, datasetUri);
		Assert.assertEquals("It contains half correct format", 3, stars.intValue());
	}

	@Test
	public void lessCorrectFormat() throws Exception {
		// dataset contains two correct formats and distribution contains
		// two incorrect formats
		DateFormatMetric metric = new DateFormatMetric();
		final String datasetUri = "http://projekt-opal.de/dataset/_mcloudde_gerastertestglichesminimumderbodennahenlufttemperaturfreuropaprojektdecregmiklip-versionv001abgels";
		Integer stars = metric.compute(model, datasetUri);
		Assert.assertEquals("It contains less correct format", 2, stars.intValue());
	}

	@Test
	public void oneCorrectFormat() throws Exception {
		// dataset contains one correct and one incorrect format.
		// distribution contains two incorrect formats
		DateFormatMetric metric = new DateFormatMetric();
		final String datasetUri = "http://projekt-opal.de/dataset/_mcloudde_gezeiten-undwindstaukurvenfr16pegelinderdeutschenbuchtmintlich";
		Integer stars = metric.compute(model, datasetUri);
		Assert.assertEquals("It contains one correct format", 1, stars.intValue());
	}

	@Test
	public void noCorrectFormat() throws Exception {
		// dataset and distribution contains one incorrect format
		DateFormatMetric metric = new DateFormatMetric();
		final String datasetUri = "http://projekt-opal.de/dataset/_mcloudde_schienennetzdichteingebietsflche";
		Integer stars = metric.compute(model, datasetUri);
		Assert.assertEquals("It contains no correct format", 0, stars.intValue());
	}

}