package org.dice_research.opal.civet.vocabulary;

/**
 * OPAL quality vocabulary.
 * 
 * The OPAL vocabulary re-uses elements of other vocabularies. See the other
 * classes in this package.
 * 
 * 
 * @author Adrian Wilke
 */
public class Opal {

	// Namespaces

	public static final String NS_OPAL_DIMENSIONS = "http://dimension.projekt-opal.de/";
	public static final String NS_OPAL_METRICS = "http://metric.projekt-opal.de/";

	// Category Intrinsic
	// Dimension: Expressiveness

	public static final String OPAL_DIMENSION_EXPRESSIVENES = NS_OPAL_DIMENSIONS + "expressiveness";
	public static final String OPAL_METRIC_CATEGORIZATION = NS_OPAL_METRICS + "categorization";
	public static final String OPAL_METRIC_DESCRIPTION = NS_OPAL_METRICS + "description";

	// Category Contextual
	// Dimension: Temporal

	public static final String OPAL_DIMENSION_TEMPORAL = NS_OPAL_DIMENSIONS + "temporal";
	public static final String OPAL_METRIC_UPDATE_RATE = NS_OPAL_METRICS + "update_rate";

	// Category Accessibility
	// Dimension: Rights

	public static final String OPAL_DIMENSION_RIGHTS = NS_OPAL_DIMENSIONS + "rights";
	// TODO: Currently, it is only checked, if any license information is given
	public static final String OPAL_METRIC_LICENSE_SPECIFIED = NS_OPAL_METRICS + "known_license";

}