package org.dice_research.opal.civet.metrics;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.DCTerms;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The ReadabilityMetric awards stars based on the how readable English
 * descriptions are.
 *
 * The readability of text can be estimated by sentence length and parts of
 * speech. Here, the Flesch-Reading-Ease is used.
 *
 * Flesch Index = 206.835 - 84.6 *( Syllables/ Words) - 1.015 *(Words /
 * Sentences)
 * 
 * @see https://web.archive.org/web/20160712094308/http://www.mang.canterbury.ac.nz/writing_guide/writing/flesch.shtml
 * @see https://en.wikipedia.org/w/index.php?title=Flesch%E2%80%93Kincaid_readability_tests&oldid=931233970
 * @see http://users.csc.calpoly.edu/~jdalbey/305/Projects/FleschReadabilityProject.html
 * 
 * @author Vikrant Singh
 * @author Adrian Wilke
 */
public class ReadabilityMetric implements Metric {

	private static final String DESCRIPTION = "Computes the flesch reading score for English descriptions. "
			+ "If the score is greater than 60, 5 stars are awarded. "
			+ "If the score is greater than 50, 4 stars are awarded. "
			+ "If the score is greater than 30, 3 stars are awarded. "
			+ "If the score is greater than 20, 2 stars are awarded. "
			+ "If the score is greater than 10, 1 stars are awarded. " + "else, 0 stars are awarded.";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		Resource dataset = model.getResource(datasetUri);
		StmtIterator descIterator = dataset.listProperties(DCTerms.description);

		// Get English description
		while (descIterator.hasNext()) {
			RDFNode descNode = descIterator.next().getObject();
			if (descNode.isLiteral() && descNode.asLiteral().getLanguage().startsWith("en")) {
				return computeFleschIndex(descNode.asLiteral().getString());
			}
		}

		// No English description found
		return null;
	}

	protected int computeFleschIndex(String text) {
		String[] words = text.split("[^A-Za-z0-9]+");
		for (int i = 0; i < words.length; i++)
			words[i] = words[i].replaceAll("[^\\w]", "");

		int numberOfWords = words.length;
		int totalSyllables = 0;

		for (String word : words)
			totalSyllables += getSyllableslablesCount(word);

		int sentencesNumber = text.split("[!?.:]+").length;
		if (sentencesNumber > 1)
			sentencesNumber--;

		double fleschIndex = 206.835 - 84.6 * totalSyllables / numberOfWords - 1.015 * numberOfWords / sentencesNumber;

		if (fleschIndex >= 60)
			// 90 to 100: 5th grade / Very easy to read.
			// 80 to 90: 6th grade / Easy to read.
			// 70 to 80: 7th grade / Fairly easy to read.
			// 60 to 70: 8th and 9th grade / Plain English. Easily understood by 13- to
			// 15-year-old students.
			return 5;
		else if (fleschIndex >= 50)
			// 50 to 60: 10th to 12th grade (high school) / Fairly difficult to read.
			return 4;
		else if (fleschIndex >= 30)
			// 30 to 50: college / Difficult to read.
			return 3;
		else if (fleschIndex >= 20)
			return 2;
		else if (fleschIndex >= 10)
			return 1;
		else
			return 0;
	}

	/**
	 * Based on https://github.com/ogrodnek/java_fathom
	 */
	protected int getSyllableslablesCount(String word1) {
		final Pattern[] SubSyllables = new Pattern[] { Pattern.compile("cial"), Pattern.compile("tia"),
				Pattern.compile("cius"), Pattern.compile("cious"), Pattern.compile("giu"), // belgium!
				Pattern.compile("ion"), Pattern.compile("iou"), Pattern.compile("sia$"), Pattern.compile(".ely$"), };

		final Pattern[] AddSyllables = new Pattern[] { Pattern.compile("ia"), Pattern.compile("riet"),
				Pattern.compile("dien"), Pattern.compile("iu"), Pattern.compile("io"), Pattern.compile("ii"),
				Pattern.compile("[aeiouym]bl$"), Pattern.compile("[aeiou]{3}"), Pattern.compile("^mc"),
				Pattern.compile("ism$"), Pattern.compile("([^aeiouy])\1l$"), Pattern.compile("[^l]lien"),
				Pattern.compile("^coa[dglx]."), Pattern.compile("[^gq]ua[^auieo]"), };

		final String word = word1.toLowerCase().replaceAll("'", "").replaceAll("e$", "");

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
		return Syllables;
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