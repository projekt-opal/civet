package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DCAT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

import java.util.List;

/**
 * The VersionMetric awards stars based on the version given in
 * distribution.
 */
public class VersionMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = "Computes if Version Number is there "
			+ "If Version Number is given, 5 stars are awarded. "
			+ "Else null is returned";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {

		LOGGER.info("Processing dataset " + datasetUri);

		Resource distribution = ResourceFactory.createResource(datasetUri);

		Statement statement = model.getProperty(distribution, DCAT.accessURL);

		String accessUrl = "";
		if(statement != null)
			accessUrl = String.valueOf(statement.getObject());

		if(accessUrl.toLowerCase().contains("version"))
			return 5;

		return null;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_VERSION_NUMBERING.getURI();
	}

}