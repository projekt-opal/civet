package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.SKOS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimelinessMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = "Computes the time when it was last modified"
			+ "If updated between now and 7 days, 5 stars are awarded. "
			+ "If updated between 7 days and 2 months , 4 stars are awarded."
			+ "If updated between 2 months and an year, 3 stars are awarded."
			+ "If updated between 1 year and 2 year, 2 stars are awarded."
			+ "If updated between 2 year and 4 year, 1 stars are awarded."
			+ "If updated before 4 year, 0 stars are awarded.";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {

		LOGGER.info("Processing dataset " + datasetUri);

		Resource dataset = ResourceFactory.createResource(datasetUri);
		Statement statement = model.getProperty(dataset, DCTerms.modified);

		String datetext ="";
		if(statement != null) {
			List<Pattern> patterns = new ArrayList<>();

			patterns.add(Pattern.compile("\\d{4}-\\d{2}-\\d{2}"));

			//			YYYY-MM-DDThh:mm:ssTZD (eg 1997-07-16T19:20:30+01:00)
			patterns.add(Pattern.compile("\\d{4}[-]\\d{2}[-]\\d{2}[T][0-9]{2}[:]" +
					"\\d{2}[:]\\d{2}[+]\\d{2}[:]\\d{2}"));

			//			YYYY-MM-DDThh:mmTZD (eg 1997-07-16T19:20+01:00)
			patterns.add(Pattern.compile("\\d{4}[-]\\d{2}[-]\\d{2}[T][0-9]{2}[:]" +
					"\\d{2}[+]\\d{2}[:]\\d{2}"));

			//			YYYY-MM-DDThh:mm:ss.sTZD (eg 1997-07-16T19:20:30.45+01:00)
			patterns.add(Pattern.compile("\\d{4}[-]\\d{2}[-]\\d{2}[T][0-9]{2}" +
					"[:]\\d{2}[:]\\d{2}[.]\\d{2}[+]\\d{2}[:]\\d{2}"));

			datetext = String.valueOf(statement.getObject());
			long millis=System.currentTimeMillis();
			java.sql.Date current=new java.sql.Date(millis);

			for (Pattern pattern: patterns) {
				String modifiedDay = "";
				Matcher matcher = pattern.matcher(datetext);
				if(matcher.matches()) {
					System.out.println("63");
					modifiedDay = matcher.group(0).substring(0, 10);
					Date modified = new SimpleDateFormat("yyyy-MM-dd").parse(modifiedDay);
					long diff = current.getTime() - modified.getTime();
					long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
					return getRating(days);
				}
			}

			Pattern patternYear = Pattern.compile("\\d{4}");
			Matcher matcherYear = patternYear.matcher(datetext);
			if (matcherYear.matches())
			{
				Date modified = new SimpleDateFormat("yyyy").
						parse(matcherYear.group(0));
				long diff = current.getTime() - modified.getTime();
				long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
				return getRating(days);
			}

			Pattern patternYearMonth = Pattern.compile("\\d{4}-\\d{2}");
			Matcher matcherYearMonth = patternYearMonth.matcher(datetext);
			if (matcherYearMonth.matches())
			{
				Date modified = new SimpleDateFormat("yyyy-MM").
						parse(matcherYearMonth.group(0));
				long diff = current.getTime() - modified.getTime();
				long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
				return getRating(days);
			}
		}

		return null;
	}

	private int getRating(long days)
	{
		System.out.println(days +"days");

		if (days < 7) // A week
			return 5;
		else if (days < 60) // 2 months
			return 4;
		else if (days < 365) // 1 year
			return 3;
		else if (days < 730) // 2 years
			return 2;
		else if (days < 1460) // 4 years
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