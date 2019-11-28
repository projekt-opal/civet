package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The CategorizationMetric awards stars based on the number of keywords and
 * themes of a dataset.
 * 
 * Keywords/tags are literals. Themes/categories should be of type skos:Concept.
 * 
 * @author Adrian Wilke
 */
public class CategorizationMetric implements Metric {

	private static final String DESCRIPTION = "Computes a score based on the use of "
			+ "keywords (tags) and themes (categories). "
			+ "For each case, 1 star is awarded: Keyords used at all, several keywords used, "
			+ "all keywords of correct type, themes used at all, and all themes of correct type.";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		Resource dataset = ResourceFactory.createResource(datasetUri);

		// Count keywords and check types
		NodeIterator keywordIterator = model.listObjectsOfProperty(dataset, DCAT.keyword);
		int numberOfKeywords = 0;
		boolean allKeywordsOfCorrectType = true;
		while (keywordIterator.hasNext()) {
			numberOfKeywords++;
			if (!keywordIterator.next().isLiteral()) {
				allKeywordsOfCorrectType = false;
			}
		}

		// Count themes and check types
		NodeIterator themeIterator = model.listObjectsOfProperty(dataset, DCAT.theme);
		int numberOfThemes = 0;
		boolean allThemesOfCorrectType = true;
		while (themeIterator.hasNext()) {
			numberOfThemes++;
			RDFNode theme = themeIterator.next();
			if (theme.isResource()) {
				Statement typeStatement = theme.asResource().getProperty(RDF.type);
				if (typeStatement == null) {
					allThemesOfCorrectType = false;
				} else {
					RDFNode type = typeStatement.getObject();
					if (type.isResource()) {
						if (!type.asResource().getURI().equals(SKOS.Concept.getURI())) {
							allThemesOfCorrectType = false;
						}
					} else {
						allThemesOfCorrectType = false;
					}
				}
			} else {
				allThemesOfCorrectType = false;
			}
		}

		// Compute score
		int score = 0;
		if (numberOfKeywords > 0) {
			score++;
			if (numberOfKeywords > 1) {
				score++;
			}
			if (allKeywordsOfCorrectType) {
				score++;
			}
		}
		if (numberOfThemes > 0) {
			score++;
			if (allThemesOfCorrectType) {
				score++;
			}
		}

		return score;
	}

	@Override
	public String getDescription() throws Exception {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_CATEGORIZATION.getURI();
	}

}