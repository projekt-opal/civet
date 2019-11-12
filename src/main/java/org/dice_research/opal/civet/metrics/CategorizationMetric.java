package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCAT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The CategorizationMetric awards stars based on the number of keywords of a
 * dataset.
 * 
 * @author Adrian Wilke
 */
public class CategorizationMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = "Computes the number of keywords. "
			+ "If exactly one keyword is given, 4 stars are awarded. "
			+ "If more than one keyword is given, 5 stars are awarded.";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {

		LOGGER.info("Processing dataset " + datasetUri);

		Resource dataset = ResourceFactory.createResource(datasetUri);

		NodeIterator nodeIterator = model.listObjectsOfProperty(dataset, DCAT.keyword);

		int numberOfKeywords = 0;
		while (nodeIterator.hasNext()) {
			numberOfKeywords++;
			nodeIterator.next();
		}

		if (numberOfKeywords <= 0) {
			// No keywords used
			return 0;
		} else if (numberOfKeywords <= 1) {
			// Keywords used
			return 4;
		} else {
			// More than 1 keyword: Indicator for extensive use
			return 5;
		}
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_CATEGORIZATION.getURI();
	}

}