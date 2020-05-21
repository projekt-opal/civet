
package org.dice_research.opal.civet.metrics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * For the Date Format metric, the following will be checked: Check, if the date
 * format is according to the W3c standards or not.
 * 
 * According to DCAT https://www.w3.org/TR/vocab-dcat-2/#bib-datetime the
 * correct formats are here: https://www.w3.org/TR/NOTE-datetime. All formats
 * have to be checked.
 * 
 * Here the implemented code checks the turtle file whether the given date
 * format is according to W3c standard or not, if it is of the standard format
 * it gives 5 stars else 0 star.
 * 
 * Finally, an average of the following 4 cases has to be computed for the final
 * score: dataset: issued + modified, distributions: issued + modified
 * 
 * @author Aamir Mohammed
 */
public class DateFormatMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTIONS = "If the date format is in the correct format then give 5 stars else 0 star"
			+ "Finally, an average of the following 4 cases has to be computed for the finalscore: dataset: issued + modified, distributions: issued + modified";

	private static List<String> correctFormat = new ArrayList<String>();
	static {
		correctFormat.add("YYYY");
		correctFormat.add("YYYY-MM");
		correctFormat.add("YYYY-MM-DD");
		correctFormat.add("YYYY-MM-DDThh:mmTZD");
		correctFormat.add("YYYY-MM-DDThh:mm:ssTZD");
		correctFormat.add("YYYY-MM-DDThh:mm:ss.sTZD");
	}

	public static int checkDateFormat(String dct_issued) {
		for (String string : correctFormat) {
			SimpleDateFormat Format = new SimpleDateFormat(string);
			try {
				Format.parse(dct_issued.trim());
				return 5;

			} catch (ParseException pe) {
				continue;
			}
		}
		return 0;
	}

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		int result = 0;
		LOGGER.info("Processing dataset " + datasetUri);
		Resource dataSet = model.createResource(datasetUri);
		if (dataSet.hasProperty(DCTerms.issued)
				&& !(dataSet.getProperty(DCTerms.issued).getObject().toString().isEmpty())) {
			String dct_issued = dataSet.getProperty(DCTerms.issued).getObject().toString();
			result += checkDateFormat(dct_issued);
		} else {
			result += 0;
		}
		if (dataSet.hasProperty(DCTerms.modified)
				&& !(dataSet.getProperty(DCTerms.modified).getObject().toString().isEmpty())) {
			String dateissued = dataSet.getProperty(DCTerms.modified).getObject().toString();
			result += checkDateFormat(dateissued);
		} else {
			result += 0;
		}
		int countDistributions = 0;
		StmtIterator distribution = model
				.listStatements(new SimpleSelector(dataSet, DCAT.distribution, (RDFNode) null));
		while (distribution.hasNext()) {
			Statement distSetSentence = distribution.nextStatement();
			Resource dist = distSetSentence.getObject().asResource();
			if (dist.hasProperty(DCTerms.issued)
					&& !(dist.getProperty(DCTerms.issued).getObject().toString().isEmpty())) {
				String dateissued = dist.getProperty(DCTerms.issued).getObject().toString();
				result += checkDateFormat(dateissued);
			} else {
				result += 0;
			}
			if (dist.hasProperty(DCTerms.modified)
					&& !(dist.getProperty(DCTerms.modified).getObject().toString().isEmpty())) {
				String dateissued = dist.getProperty(DCTerms.modified).getObject().toString();
				result += checkDateFormat(dateissued);
			} else {
				result += 0;
			}
			countDistributions++;
		}
		return result / (2 * countDistributions + 2);
	}

	@Override
	public String getDescription() {
		return DESCRIPTIONS;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_CATEGORIZATION.getURI();
	}

}