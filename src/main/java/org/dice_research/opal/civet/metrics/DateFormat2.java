
package org.dice_research.opal.civet.metrics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
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
 * The Dateformat metric awards stars based on the the different dateformat in
 * the dataset.RDF properties dct:issued, dct:modified and distribution are
 * checked for dates. The more meaningful dates have been awarded. Rating
 * criteria: 1."if the date is in the format: DD/MM/YYYY hh:mm:s GMT -
 * DD/MM/YYYY hh:mm:s GMT then give 5 stars" 2."if the date is in the format:
 * YYYY.MM.DD hh:mm:s GMT then give 4 stars" 3."if the date is in the format:
 * YYYY.MM.DD then give 3 stars" 4."if the date is in the format: YYYY then give
 * 2 stars" 5."if the date property is empty or have invalid date format then
 * give 1 star"
 * 
 * references: https://www.w3.org/TR/NOTE-datetime
 * 
 * @author Aamir Mohammed
 */
public class DateFormat2 implements Metric {

	static List<String> dateTimeFormat = new ArrayList<String>();
	static {
		dateTimeFormat.add("YYYY-MM-DD hh:mm:s");
		dateTimeFormat.add("dd.mm.yyyy hh:mm:s");
		dateTimeFormat.add("dd-mm-yyyy hh:mm:s");
		dateTimeFormat.add("dd/mm/yyyy hh:mm:s");
		dateTimeFormat.add("YYYY.MM.DD hh:mm:s");
		dateTimeFormat.add("YYYY/MM/DD hh:mm:s");
	}

	static List<String> dateFormat = new ArrayList<String>();
	static {
		dateFormat.add("yyyy-MM-dd");
		dateFormat.add("dd.mm.yyyy");
		dateFormat.add("YYYY/MM/DD");
		dateFormat.add("YYYY.MM.DD");
		dateFormat.add("dd-MM-yyyy");
		dateFormat.add("dd/mm/yyyy");
	}

	static List<String> yearFormat = new ArrayList<String>();
	static {
		yearFormat.add("YYYY");
	}

	public static int checkDateFormat(String issued) {
		if (issued.contains("bis")) {
			String[] range = issued.split("bis");
			return checkRange(range);
		} else if (issued.contains("-")) {
			String[] range = issued.split("-");
			return checkRange(range);
		} else if (issued.contains("—")) {
			String[] range = issued.split("—");
			return checkRange(range);
		} else {

			if (issued.contains("GMT") && issued.split(" ").length == 6)
				return 4;

			for (String string : dateTimeFormat) {
				SimpleDateFormat Format = new SimpleDateFormat(string);
				try {
					Format.parse(issued.trim());
					return 4;

				} catch (ParseException pe) {
					continue;
				}
			}

			for (String string : dateFormat) {
				SimpleDateFormat Format = new SimpleDateFormat(string);
				try {
					Format.parse(issued.trim());
					return 3;

				} catch (ParseException pe) {
					continue;
				}
			}

			for (String string : yearFormat) {
				SimpleDateFormat Format = new SimpleDateFormat(string);
				try {
					Format.parse(issued.trim());
					return 2;

				} catch (ParseException pe) {
					continue;
				}
			}
		}
		return 1;
	}

	public static int checkRange(String[] range) {

		String fromDate = "", toDate = "";
		if (range.length == 2) {
			fromDate = range[0].trim();
			toDate = range[1].trim();
		}
		if (fromDate.contains("GMT") && fromDate.split(" ").length == 6 && toDate.contains("GMT")
				&& toDate.split(" ").length == 6)
			return 4;
		for (String string : dateTimeFormat) {
			SimpleDateFormat Format = new SimpleDateFormat(string);
			try {
				Format.parse(fromDate);
				Format.parse(toDate);
				return 5;

			} catch (ParseException pe) {
				continue;
			}
		}

		for (String string : dateFormat) {
			SimpleDateFormat Format = new SimpleDateFormat(string);
			try {
				Format.parse(fromDate);
				Format.parse(toDate);
				return 4;

			} catch (ParseException pe) {
				continue;
			}
		}

		for (String string : yearFormat) {
			SimpleDateFormat Format = new SimpleDateFormat(string);
			try {
				Format.parse(fromDate);
				Format.parse(toDate);
				return 3;

			} catch (ParseException pe) {
				continue;
			}
		}
		return 1;
	}

	private static final Logger logger = LogManager.getLogger();
	private static final String descriptions = "if the date is in the format: DD/MM/YYYY hh:mm:s GMT - DD/MM/YYYY hh:mm:s GMT then give 5 stars"
			+ "if the date is in the format: YYYY.MM.DD hh:mm:s GMT then give 4 stars"
			+ "if the date is in the format: YYYY.MM.DD then give 3 stars"
			+ "if the date is in the format: YYYY then give 2 stars"
			+ "if the date property is empty or have invalid date format then give 1 star";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		int result = 1;
		logger.info("Processing dataset " + datasetUri);
		Resource dataset = ResourceFactory.createResource(datasetUri);
		Statement dataSetSentence = model.getRequiredProperty(dataset, RDF.type);
		Resource dataSet = dataSetSentence.getSubject();

		if (dataSet.hasProperty(DCTerms.issued)
				&& !(dataSet.getProperty(DCTerms.issued).getObject().toString().isEmpty())) {
			String dateissued = dataSet.getProperty(DCTerms.issued).getObject().toString();
			result = checkDateFormat(dateissued);
		}

		else if (dataSet.hasProperty(DCTerms.modified)
				&& !(dataSet.getProperty(DCTerms.modified).getObject().toString().isEmpty())) {
			String dateissued = dataSet.getProperty(DCTerms.modified).getObject().toString();
			result = checkDateFormat(dateissued);
		} else {
			StmtIterator distribution = model
					.listStatements(new SimpleSelector(dataset, DCAT.distribution, (RDFNode) null));
			if (distribution.hasNext()) {
				Statement distSetSentence = distribution.nextStatement();
				Resource dist = distSetSentence.getObject().asResource();

				if (dist.hasProperty(DCTerms.issued)
						&& !(dist.getProperty(DCTerms.issued).getObject().toString().isEmpty())) {
					String dateissued = dist.getProperty(DCTerms.issued).getObject().toString();
					result = checkDateFormat(dateissued);
				}

				else if (dist.hasProperty(DCTerms.modified)
						&& !(dist.getProperty(DCTerms.modified).getObject().toString().isEmpty())) {
					String dateissued = dist.getProperty(DCTerms.modified).getObject().toString();
					result = checkDateFormat(dateissued);
				}
			}
		}
		return result;
	}

	@Override
	public String getDescription() {
		return descriptions;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_CATEGORIZATION.getURI();
	}

}