package org.dice_research.opal.civet.vocabulary;

/**
 * Data Catalog Vocabulary (DCAT)
 * 
 * @see https://www.w3.org/TR/vocab-dcat/
 * 
 * @author Adrian Wilke
 */
public abstract class Dcat {

	public static final String PROPERTY_DISTRIBUTION = org.apache.jena.vocabulary.DCAT.distribution.toString();

	// FIXME: Wrong property used in graph. This is a workaround.
	// org.apache.jena.vocabulary.DCAT.theme.toString();
	public static final String PROPERTY_THEME = org.apache.jena.vocabulary.DCTerms.NS + "theme";

}