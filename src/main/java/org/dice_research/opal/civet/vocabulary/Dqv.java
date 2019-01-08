package org.dice_research.opal.civet.vocabulary;

/**
 * Data Quality Vocabulary (DQV)
 * 
 * DQV hierarchy (general to specific): Category > Dimension > Metric.
 * 
 * @see https://www.w3.org/TR/vocab-dqv/
 * 
 * @author Adrian Wilke
 */
public class Dqv {

	public static final String NS = "http://www.w3.org/ns/dqv#";

	public static final String IN_CATEGORY = NS + "inCategory";
	public static final String IN_DIMENSION = NS + "inDimension";

	public static final String CATEGORY = NS + "Category";
	public static final String DIMENSION = NS + "Dimension";
	public static final String METRIC = NS + "Metric";
}