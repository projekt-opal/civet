package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The ReadabilityMetric awards stars based on the how readable
 * is the description..
 *
 * The readability of text can be estimated by
 * sentence length and parts of speech. Usage of a readability test, e.g.
 * Flesch-Reading-Ease
 *
 * Flesch Index = 206.835  - 84.6 *( Syllables/ Words) - 1.015 *(Words / Sentences)
 * http://users.csc.calpoly.edu/~jdalbey/305/Projects/FleschReadabilityProject.html
 *
 * - get description through dc:description .
 * Pattern References:
 * References - https://github.com/ogrodnek/java_fathom
 *
 */
public class ReadabilityMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = "Computes the flesch reading score"
			+ "If the score is greater than 92, 5 stars are awarded. "
			+ "If the score is greater than 70, 4 stars are awarded. "
			+ "If the score is greater than 70, 3 stars are awarded. "
			+ "If the score is greater than 15, 2 stars are awarded. "
			+ "If the score is greater than 0, 1 stars are awarded. "
			+ "else, 0 stars are awarded.";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		LOGGER.info("Processing dataset " + datasetUri);

		Resource dataset = ResourceFactory.createResource(datasetUri);
		Statement statement = model.getProperty(dataset, DCTerms.description);

		String description = "";
		if(statement != null)
			description = String.valueOf(statement.getObject());
		else
			return 0;

		if(!description.contains("@en"))
			return null;

		System.out.println(description);
		String[] words = description.split("[^A-Za-z0-9]+");
		for (int i = 0; i < words.length; i++)
			words[i] = words[i].replaceAll("[^\\w]", "");

		int numberOfWords = words.length;
		int totalSyllables = 0;

		for (String word : words)
			totalSyllables += getSyllableslablesCount(word);

		int sentencesNumber = description.split("[!?.:]+").length;
		double fleschIndex = 206.835-84.6*totalSyllables/numberOfWords
				-1.015*numberOfWords/sentencesNumber;

		if(fleschIndex > 92)
			return 5;
		else if (fleschIndex >70)
			return 4;
		else if (fleschIndex >40)
			return 3;
		else if (fleschIndex >15)
			return 2;
		else if (fleschIndex > 0)
			return 1;

		return 0;
	}

	public int getSyllableslablesCount(String word1)
	{
		final Pattern[] SubSyllables = new Pattern[] { Pattern.compile("cial"), Pattern.compile("tia"),
				Pattern.compile("cius"), Pattern.compile("cious"), Pattern.compile("giu"), // belgium!
				Pattern.compile("ion"), Pattern.compile("iou"), Pattern.compile("sia$"), Pattern.compile(".ely$"),
		};

		final Pattern[] AddSyllables = new Pattern[] { Pattern.compile("ia"), Pattern.compile("riet"),
				Pattern.compile("dien"), Pattern.compile("iu"), Pattern.compile("io"), Pattern.compile("ii"),
				Pattern.compile("[aeiouym]bl$"),
				Pattern.compile("[aeiou]{3}"),
				Pattern.compile("^mc"), Pattern.compile("ism$"),
				Pattern.compile("([^aeiouy])\1l$"),
				Pattern.compile("[^l]lien"),
				Pattern.compile("^coa[dglx]."),
				Pattern.compile("[^gq]ua[^auieo]"),
		};

		final String word = word1.toLowerCase().replaceAll
				("'", "").replaceAll("e$", "");

		if (word.length() == 1) {
			return 1;
		}

		final String[] wordSplit = word.split("[^aeiouy]+"); // '-' should be perhaps
		// added?

		int Syllables = 0;

		// special cases
		for (final Pattern p : SubSyllables) {
			final Matcher m = p.matcher(word);

			if (m.find())
				Syllables--;
		}

		for (final Pattern p : AddSyllables) {
			final Matcher m = p.matcher(word);

			if (m.find())
				Syllables++;
		}

		// count vowel groupings
		if (wordSplit.length > 0 && "".equals(wordSplit[0])) {
			Syllables += wordSplit.length - 1;
		} else {
			Syllables += wordSplit.length;
		}

		if (Syllables == 0) {
			// no vowels
			Syllables = 1;
		}
		return  Syllables ;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_READABILITY.getURI();
	}

}