package org.dice_research.opal.civet.metrics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.DCTerms;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * Awards stars based on the time when a dataset was last modified.
 * 
 * Uses ISO 8601 (xsd:gYear, xsd:gYearMonth, xsd:date, or xsd:dateTime).
 * 
 * @see https://www.w3.org/TR/vocab-dcat-2/#Property:resource_update_date
 * @see https://www.w3.org/TR/NOTE-datetime
 *
 * @author Vikrant Singh
 * @author Adrian Wilke
 */
public class TimelinessMetric implements Metric {

	private static final String DESCRIPTION = "Awards stars based on the time when a dataset was last modified. "
			+ "If updated between now and 1 month, 5 stars are awarded. "
			+ "If updated between 1 month and 6 months , 4 stars are awarded. "
			+ "If updated between 6 months and 1 year, 3 stars are awarded. "
			+ "If updated between 1 year and 2 years, 2 stars are awarded. "
			+ "If updated between 2 year and 3 years, 1 stars are awarded. "
			+ "If updated before 3 years, 0 stars are awarded.";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {

		// Get modified-date or return null
		Resource dataset = ResourceFactory.createResource(datasetUri);
		Statement statement = model.getProperty(dataset, DCTerms.modified);
		if (statement == null) {
			return null;
		} else if (!statement.getObject().isLiteral()) {
			return null;
		}
		String datetext = statement.getObject().asLiteral().getString();

		List<Pattern> patterns = getPatterns();
		Date current = new Date();

		for (Pattern pattern : patterns) {
			String modifiedDay = "";
			Matcher matcher = pattern.matcher(datetext);
			if (matcher.matches()) {
				modifiedDay = matcher.group(0).substring(0, 10);
				Date modified = new SimpleDateFormat("yyyy-MM-dd").parse(modifiedDay);
				long diff = current.getTime() - modified.getTime();
				long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
				return getRating(days);
			}
		}

		Pattern patternYear = Pattern.compile("\\d{4}");
		Matcher matcherYear = patternYear.matcher(datetext);
		if (matcherYear.matches()) {
			Date modified = new SimpleDateFormat("yyyy").parse(matcherYear.group(0));
			long diff = current.getTime() - modified.getTime();
			long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			// Use best case instead of beginning of year
			if (days > 365) {
				days -= 365;
			}
			return getRating(days);
		}

		Pattern patternYearMonth = Pattern.compile("\\d{4}-\\d{2}");
		Matcher matcherYearMonth = patternYearMonth.matcher(datetext);
		if (matcherYearMonth.matches()) {
			Date modified = new SimpleDateFormat("yyyy-MM").parse(matcherYearMonth.group(0));
			long diff = current.getTime() - modified.getTime();
			long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			return getRating(days);
		}

		return null;
	}

	/**
	 * @see https://www.w3.org/TR/NOTE-datetime
	 */
	protected List<Pattern> getPatterns() {
		List<Pattern> patterns = new ArrayList<>();

		// YYYY-MM-DD (eg 1997-07-16)
		patterns.add(Pattern.compile("\\d{4}-\\d{2}-\\d{2}"));

		// YYYY-MM-DDThh:mmTZD (eg 1997-07-16T19:20+01:00)
		patterns.add(Pattern.compile("\\d{4}[-]\\d{2}[-]\\d{2}[T][0-9]{2}[:]\\d{2}[+]\\d{2}[:]\\d{2}"));

		// YYYY-MM-DDThh:mm:ssTZD (eg 1997-07-16T19:20:30+01:00)
		patterns.add(Pattern.compile("\\d{4}[-]\\d{2}[-]\\d{2}[T][0-9]{2}[:]\\d{2}[:]\\d{2}[+]\\d{2}[:]\\d{2}"));

		// YYYY-MM-DDThh:mm:ss.sTZD (eg 1997-07-16T19:20:30.45+01:00)
		patterns.add(
				Pattern.compile("\\d{4}[-]\\d{2}[-]\\d{2}[T][0-9]{2}[:]\\d{2}[:]\\d{2}[.]\\d{2}[+]\\d{2}[:]\\d{2}"));

		return patterns;
	}

	protected int getRating(long days) {
		if (days <= 31) // 1 months
			return 5;
		else if (days <= 31 * 6) // 6 months
			return 4;
		else if (days <= 365) // 1 year
			return 3;
		else if (days <= 365 * 2) // 2 years
			return 2;
		else if (days <= 365 * 3) // 3 years
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