package org.dice_research.opal.civet.metrics;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The AvailabilityOfLicensesMetric provides a rating to a dataset based on the
 * number of available licenses/rights in a dataset.
 * 
 * @author Gourab Sahu, Adrian Wilke
 */
public class LicenseAvailabilityMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();

	private static final String DESCRIPTION = "If dataset has rights/license info then award 5 stars "
			+ "Else If dataset has no rights/license but all distributions in dataset have licenses/rights then give 5 star"
			+ "Else If dataset has no rights/license but less than 100% and more than or equal to 75% dataset's distribution has then 4 stars are awarded"
			+ "Else If dataset has no rights/license but less than 75% and more than or equal to 50% dataset's distribution has then 3 stars are awarded"
			+ "Else If dataset has no rights/license but less than 50% and more than or equal to 25% dataset's distribution has then 2 stars are awarded"
			+ "Else If dataset has no rights/license but less than 25% and more than  0% dataset's distribution has then 1 star is awarded"
			+ "Else if no License at all then 0 star is awarded.";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {

		Resource dataset = model.getResource(datasetUri);

		int counterDistributions = 0;
		int counterLicenseInformation = 0;

		if (hasLicenseInformation(dataset)) {
			// License/rights information in dataset
			return 5;

		} else {
			// No license/rights information in dataset, need to check each distribution
			NodeIterator distributionsIterator = model.listObjectsOfProperty(dataset, DCAT.distribution);
			while (distributionsIterator.hasNext()) {
				counterDistributions++;
				if (hasLicenseInformation(distributionsIterator.next())) {
					counterLicenseInformation++;
				}
			}
		}

		// Do not divide by zero.
		// No distributions given.
		if (counterDistributions == 0) {
			LOGGER.warn("No distributions for " + datasetUri);
			return null;
		}

		int percentage = (100 * counterLicenseInformation / counterDistributions);
		if (percentage == 100)
			return 5;
		else if (percentage < 100 && percentage >= 75)
			return 4;
		else if (percentage < 75 && percentage >= 50)
			return 3;
		else if (percentage < 50 && percentage >= 25)
			return 2;
		else if (percentage < 25 && percentage > 0)
			return 1;
		else
			return 0;
	}

	protected boolean hasLicenseInformation(RDFNode rdfNode) {
		if (rdfNode.isResource()) {
			Resource resource = rdfNode.asResource();
			if (resource.hasProperty(DCTerms.license)) {
				if (!resource.getProperty(DCTerms.license).getObject().isAnon()) {
					return true;
				}
			} else if (resource.hasProperty(DCTerms.rights)) {
				if (!resource.getProperty(DCTerms.rights).getObject().isAnon()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		// TODO: New URI required
		return Opal.OPAL_METRIC_KNOWN_LICENSE.getURI();
	}

}