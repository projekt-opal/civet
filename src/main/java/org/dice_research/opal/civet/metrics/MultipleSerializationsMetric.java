package org.dice_research.opal.civet.metrics;

import java.util.HashSet;
import java.util.Set;

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
 * The MultipleSerializationsMetric awards stars based on the existence of
 * multiple file formats are provided for download.
 * 
 * @author Adrian Wilke
 */
public class MultipleSerializationsMetric implements Metric {

	private static final String DESCRIPTION = "Checks, if multiple file formats are provided for download.";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		Resource dataset = ResourceFactory.createResource(datasetUri);

		Set<String> extensions = new HashSet<>();
		Set<String> typesAndFormats = new HashSet<>();

		// Go through distributions
		NodeIterator distributionIterator = model.listObjectsOfProperty(dataset, DCAT.distribution);
		while (distributionIterator.hasNext()) {
			RDFNode distributionNode = distributionIterator.next();
			if (distributionNode.isResource()) {
				Resource distribution = distributionNode.asResource();

				// Get download URL extensions
				NodeIterator downloadUrlIterator = model.listObjectsOfProperty(distribution, DCAT.downloadURL);
				while (downloadUrlIterator.hasNext()) {
					RDFNode downloadUrl = downloadUrlIterator.next();
					if (downloadUrl.isURIResource()) {
						addExtension(downloadUrl.asResource().getURI().toLowerCase(), extensions);
					}
				}

				// Get media types
				NodeIterator mediaTypeIterator = model.listObjectsOfProperty(distribution, DCAT.mediaType);
				while (mediaTypeIterator.hasNext()) {
					RDFNode mediaTypeNode = mediaTypeIterator.next();
					if (mediaTypeNode.isLiteral()) {
						typesAndFormats.add(mediaTypeNode.asLiteral().getString());
					}
				}

				// Get formats
				NodeIterator formatIterator = model.listObjectsOfProperty(distribution, DCTerms.format);
				while (formatIterator.hasNext()) {
					RDFNode formatNode = formatIterator.next();
					if (formatNode.isLiteral()) {
						typesAndFormats.add(formatNode.asLiteral().getString());
					}
				}

			}
		}

		if (extensions.size() > 1 || typesAndFormats.size() > 1) {
			return 5;
		} else {
			return 0;
		}
	}

	protected void addExtension(String value, Set<String> set) {
		int dotIndex = value.lastIndexOf('.');
		// Has to contain dot
		if (dotIndex != -1) {
			// Allow 4 characters for extension
			if (value.length() - dotIndex <= (4 + 1)) {
				set.add(value.substring(dotIndex + 1));
			}
		}
	}

	@Override
	public String getDescription() throws Exception {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_MULTIPLE_SERIALIZATIONS.getURI();
	}

}