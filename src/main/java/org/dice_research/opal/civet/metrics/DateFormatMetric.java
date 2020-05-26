
package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The date format metric checks, according to DCAT, if date formats are
 * "encoded using the ISO 8601 Date and Time compliant string".
 * 
 * For a given dataset and all its distributions, the predicates issued and
 * modified are checked. Finally, an average score is calculated.
 * 
 * @see https://www.w3.org/TR/vocab-dcat-2/#Property:resource_release_date
 * @see https://www.w3.org/TR/vocab-dcat-2/#Property:resource_update_date
 * @see https://www.w3.org/TR/NOTE-datetime
 * 
 * @author Aamir Mohammed
 * @author Adrian Wilke
 */
public class DateFormatMetric implements Metric {

	private static final String DESCRIPTION = "Checks if date formats are encoded according to ISO 8601.";

	/**
	 * Checks date format according to ISO 8601.
	 * 
	 * @see https://www.w3.org/TR/NOTE-datetime
	 */
	public static boolean isValidDateFormat(String dateString) {
		if (dateString.matches("\\d{4}-\\d{2}-\\d{2}")) {
			// "YYYY-MM-DD"
			return true;
		} else if (dateString.matches("\\d{4}")) {
			// "YYYY"
			return true;
		} else if (dateString.matches("\\d{4}-\\d{2}")) {
			// "YYYY-MM"
			return true;
		} else if (dateString.matches("\\d{4}-\\d{2}-\\d{2}[T]\\d{2}:\\d{2}:\\d{2}[+]\\d{2}:\\d{2}")) {
			// "YYYY-MM-DDThh:mm:ssTZD"
			return true;
		} else if (dateString.matches("\\d{4}-\\d{2}-\\d{2}[T]\\d{2}:\\d{2}[+]\\d{2}:\\d{2}")) {
			// "YYYY-MM-DDThh:mmTZD"
			return true;
		} else if (dateString.matches("\\d{4}-\\d{2}-\\d{2}[T]\\d{2}:\\d{2}:\\d{2}[.]\\d+[+]\\d{2}:\\d{2}")) {
			// "YYYY-MM-DDThh:mm:ss.sTZD"
			return true;
		} else {
			return false;
		}
	}

	private int getScoreForFormat(Resource resource, Property property) {
		if (isValidDateFormat(resource.getProperty(property).getObject().toString())) {
			return 5;
		} else {
			return 0;
		}
	}

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		Integer dateCounter = 0;
		Integer score = 0;

		// Check dataset
		Resource dataset = model.createResource(datasetUri);
		if (dataset.hasProperty(DCTerms.issued)) {
			dateCounter++;
			score += getScoreForFormat(dataset, DCTerms.issued);
		}
		if (dataset.hasProperty(DCTerms.modified)) {
			dateCounter++;
			score += getScoreForFormat(dataset, DCTerms.modified);
		}

		// Check distributions
		StmtIterator distributions = model
				.listStatements(new SimpleSelector(dataset, DCAT.distribution, (RDFNode) null));
		while (distributions.hasNext()) {

			RDFNode distributionNode = distributions.nextStatement().getObject();
			if (!distributionNode.isResource()) {
				continue;
			}

			Resource distribution = distributionNode.asResource();
			if (distribution.hasProperty(DCTerms.issued)) {
				dateCounter++;
				score += getScoreForFormat(distribution, DCTerms.issued);
			}
			if (distribution.hasProperty(DCTerms.modified)) {
				dateCounter++;
				score += getScoreForFormat(distribution, DCTerms.modified);
			}
		}

		// Overall score
		if (dateCounter == 0) {
			// No date format given
			return null;
		} else {
			// Return average
			return score / dateCounter;
		}
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_DATE_FORMAT.getURI();
	}

}