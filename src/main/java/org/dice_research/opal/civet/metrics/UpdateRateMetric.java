package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The UpdateRateMetric awards stars based on how often the
 * dataset is updated
 */

public class UpdateRateMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = "Computes the update rate/frequency"
			+ "If data is updated WEEKLY, DAILY, HOURLY,"
			+ "CONTINUOUS, CONT, 5 stars are awarded. "
			+ "If data is updated MONTHLY, 4 stars are awarded."
			+ "If data is updated QUARTERLY, 3 stars are awarded."
			+ "If data is updated ANNUALLY,ANNUAL_2,TRIENNIAL"
			+ "2 stars are awarded."
			+ "If data is updated IRREGULARLY"
			+ "1 star is awarded."
			+ "FOR UNKNOWN ,null is returned. ";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {

		LOGGER.info("Processing dataset " + datasetUri);

		Resource dataset = ResourceFactory.createResource(datasetUri);
		Statement statement = model.getProperty(dataset, DCTerms.accrualPeriodicity);

		String updateRate = "";
		if(statement != null)
			 updateRate = String.valueOf(statement.getObject()).toUpperCase();
		else
			return null;

		     if(updateRate.contains("UNKNOWN"))
			    return null;
		else if (updateRate.contains("IRREG"))
				return 1;
		else if (updateRate.contains("TRIENNIAL") || updateRate.contains("ANNUAL_2")
				|| updateRate.contains("ANNUAL"))
				return 2;
		else if(updateRate.contains("QUARTERLY"))
				return 3;
		else if (updateRate.contains("MONTHLY") || updateRate.contains("BIMONTHLY"))
				return 4;
		else if (updateRate.contains("WEEKLY") || updateRate.contains("DAILY")
				|| updateRate.contains("HOURLY") || updateRate.contains("CONT")
				|| updateRate.contains("BIWEEKLY"))
				return 5;
		else
				return 0;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_UPDATE_RATE.getURI();
	}
}