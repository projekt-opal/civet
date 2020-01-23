package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The Representation awards stars based on the the dateformat in the dataset
 * 
 * @author Aamir Mohammed
 */
public class DateformatMetric implements Metric {
	public static int checkDateFormat(String issued) {
		if (issued.matches("\\d{4}-\\d{2}-\\d{2}")) {
			return 5;
		} else {
			return 1;
		}

	}
	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = "Check the metadata field of dateformat"
			+ "If dateformat in the dataset is according to W3C standards then give 5 starts" + "Else return null";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		LOGGER.info("Processing dataset " + datasetUri);

		StmtIterator DatasetIterator = model.listStatements(new SimpleSelector(null, RDF.type, DCAT.Dataset));
		if (DatasetIterator.hasNext()) {
			Statement DataSetSentence = DatasetIterator.nextStatement();
			Resource DataSet = DataSetSentence.getSubject();

			if (DataSet.hasProperty(DCTerms.issued)
					&& !(DataSet.getProperty(DCTerms.issued).getObject().toString().isEmpty())) {
				String dateissued = DataSet.getProperty(DCTerms.issued).getObject().toString();
				int result = checkDateFormat(dateissued);
				System.out.println("result::::::"+result);
				return result;
			} 

		}
		return null;
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