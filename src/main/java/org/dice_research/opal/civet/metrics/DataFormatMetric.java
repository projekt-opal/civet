package org.dice_research.opal.civet.metrics;

import java.util.HashMap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The DataFormatMetric gives a rating to a dataset based on whether a
 * data-format or file-format information is available or not.
 * 
 * Data/file format information is normally available at distribution level
 * which is expressed with predicate dct:format or dcat:mediaType. This metric
 * checks how many distributions have an object value for dct:format or
 * dcat:mediaType and based on that an average rating is calculated.
 * 
 * @see https://www.w3.org/TR/vocab-dcat-1/#Property:distribution_format
 * @see https://www.w3.org/TR/vocab-dcat-1/#Property:distribution_media_type
 * 
 * @author Gourab Sahu
 * @author Adrian Wilke
 */

public class DataFormatMetric implements Metric {

	private static final String DESCRIPTION = "A dataset can have many distributions. Here, individual scores for each distribution are calculated. "
			+ "A distribution gets 5 stars if either the mediatype the format is given. "
			+ "Finally, an average score for the dataset is calculated based on the distribution scores.";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {

		Resource dataset = ResourceFactory.createResource(datasetUri);

		// Total number of distributions in a dataset.
		int distributionCounter = 0;

		// Store a score for each distribution, we will use it for final evaluation.
		HashMap<String, Integer> distributionsAndScores = new HashMap<String, Integer>();

		NodeIterator distributionIterator = model.listObjectsOfProperty(dataset, DCAT.distribution);
		while (distributionIterator.hasNext()) {

			// Give 5 stars to a distribution if it has information about MediaType or
			// format. Else give 0 stars.
			RDFNode distributionNode = distributionIterator.nextNode();
			if (distributionNode.isResource()) {
				Resource distribution = distributionNode.asResource();

				if (distribution.hasProperty(DCTerms.format)) {
					RDFNode formatNode = distribution.getProperty(DCTerms.format).getObject();
					if (formatNode.isURIResource() || formatNode.isLiteral() && !formatNode.toString().isEmpty())
						distributionsAndScores.put(distribution.toString(), 5);
				}

				else if (distribution.hasProperty(DCAT.mediaType)) {
					RDFNode mediaTypeNode = distribution.getProperty(DCAT.mediaType).getObject();
					if (mediaTypeNode.isURIResource()
							|| mediaTypeNode.isLiteral() && !mediaTypeNode.toString().isEmpty())
						distributionsAndScores.put(distribution.toString(), 5);

				} else {
					distributionsAndScores.put(distributionNode.toString(), 0);
				}

			} else {
				distributionsAndScores.put(distributionNode.toString(), 0);
			}

			// To calculate how many distributions in a dataset. It will be used for
			// scoring.
			distributionCounter++;
		}

		// Score evaluation: Average
		float sum = 0;
		for (String key : distributionsAndScores.keySet()) {
			sum += distributionsAndScores.get(key);
		}
		return (int) Math.ceil(sum / distributionCounter);
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_DATA_FORMAT.getURI();
	}

}