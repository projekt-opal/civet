package org.dice_research.opal.civet.metrics;

import java.util.HashSet;
import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCAT;
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

		// TODO: Check format / mediaType

		// Check download URL
		Set<String> extensions = new HashSet<>();
		NodeIterator distributionIterator = model.listObjectsOfProperty(dataset, DCAT.distribution);
		while (distributionIterator.hasNext()) {
			RDFNode distribution = distributionIterator.next();
			if (distribution.isResource()) {
				NodeIterator downloadUrlIterator = model.listObjectsOfProperty(distribution.asResource(),
						DCAT.downloadURL);
				while (downloadUrlIterator.hasNext()) {
					RDFNode downloadUrl = downloadUrlIterator.next();
					if (downloadUrl.isURIResource()) {
						addExtension(downloadUrl.asResource().getURI().toLowerCase(), extensions);
					}
				}
			}
		}

		if (extensions.size() > 1) {
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