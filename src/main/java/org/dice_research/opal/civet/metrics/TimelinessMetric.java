package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimelinessMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = "Computes the time when it was last modified"
			+ "If updated between now and 7 days, 5 stars are awarded. "
			+ "If updated between 7 days and 6 months , 4 stars are awarded."
			+ "If updated between 6 months and an year, 3 stars are awarded."
			+ "If updated between 1 year and 2 year, 2 stars are awarded."
			+ "If updated between 2 year and 4 year, 1 stars are awarded."
			+ "If updated between before 4 year, 0 stars ar" +
			"e awarded.";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {

		LOGGER.info("Processing dataset " + datasetUri);

		Resource dataset = ResourceFactory.createResource(datasetUri);
		Statement statement = model.getProperty(dataset, DCTerms.modified);

		String datetext ="";
		if(statement != null) {
			datetext = String.valueOf(statement.getObject());
			Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
			Matcher matcher = pattern.matcher(datetext);
			long millis=System.currentTimeMillis();
			java.sql.Date current=new java.sql.Date(millis);
			if (matcher.find()) {
				Date modified = new SimpleDateFormat("yyyy-MM-dd").parse(matcher.group(0));
				long diff = current.getTime() - modified.getTime();
				long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
				int rating = getRating(days);
				return rating;
			}
		}

		return null;
	}

	public int getRating(long days)
	{
		if (days < 7)
			return 5;
		else if (days < 184)
			return 4;
		else if (days < 365)
			return 3;
		else if (days < 750)
			return 2;
		else if (days < 1500)
			return 1;
		else
			return 0;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_TIMELINESS.getURI();
	}
}