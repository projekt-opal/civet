package org.dice_research.opal.civet.metrics;

import java.net.URL;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The ProviderIdentityMetric provides a rating to a dataset based on direct
 * data publisher information and landing-pages of datasets as well as
 * access-urls of distributions.
 * 
 * @author Gourab Sahu, Adrian Wilke
 */
public class ProviderIdentityMetric implements Metric {

	private static final String DESCRIPTION = "The data provider identiy is mainly awarded by direct contact information. "
			+ "Additionally, landing-pages of datasets and access-urls of distributions are checked.";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		Resource dataset = model.getResource(datasetUri);
		int score = 0;

		// Check publisher. Use best information record.
		NodeIterator pubNodeIterator = model.listObjectsOfProperty(dataset, DCTerms.publisher);
		while (pubNodeIterator.hasNext()) {
			score = Math.max(score, evaluatePublisher(pubNodeIterator.next()));
			// Early return on one optimal publisher information
			if (score == 5) {
				return score;
			}
		}

		// Check landing page
		if (isPropValidUrl(dataset, DCAT.landingPage)) {
			score = Math.max(score, 3);
		}

		// Check distributions
		int distCounter = 0;
		int distScores = 0;
		NodeIterator distributionIterator = model.listObjectsOfProperty(dataset, DCAT.distribution);
		while (distributionIterator.hasNext()) {
			RDFNode distNode = distributionIterator.next();
			if (distNode.isResource()) {
				Resource dist = distNode.asResource();
				int distScore = 0;

				// Check publisher
				pubNodeIterator = model.listObjectsOfProperty(dist, DCTerms.publisher);
				while (pubNodeIterator.hasNext()) {
					distScore = Math.max(distScore, evaluatePublisher(pubNodeIterator.next()));
				}

				// Check accessURL
				if (isPropValidUrl(dist, DCAT.accessURL)) {
					distScore = Math.max(distScore, 3);
				}

				distCounter++;
				distScores += distScore;
			}
		}
		return Math.max(score, (int) Math.ceil(1f * distScores / distCounter));
	}

	/**
	 * Evaluates publisher information. DCAT2 states that "Resources of type
	 * foaf:Agent are recommended as values for this property."
	 * 
	 * FOAF: Agent has sub-classes Group Person Organization
	 * 
	 * @see https://www.w3.org/TR/vocab-dcat-2/#Property:resource_publisher
	 * @see http://xmlns.com/foaf/spec/#term_Agent
	 * 
	 * @return score in [0,5]
	 */
	protected int evaluatePublisher(RDFNode pubNode) {
		if (pubNode.isResource()) {
			Resource pubRes = pubNode.asResource();
			int score = 0;

			if (pubRes.isURIResource()) {
				// +4 for URI
				score += 4;

			} else if (pubRes.listProperties().hasNext()) {
				// +4 for no URI, but additional information (name, etc.) given
				score += 4;
			}

			// +1 for correct type
			if (pubRes.hasProperty(RDF.type, FOAF.Agent) || pubRes.hasProperty(RDF.type, FOAF.Person)
					|| pubRes.hasProperty(RDF.type, FOAF.Organization) || pubRes.hasProperty(RDF.type, FOAF.Group)) {
				score++;
			}

			return score;

		} else if (pubNode.isLiteral()) {
			String literal = pubNode.asLiteral().getString();

			if (isValidUrl(literal)) {
				// URL given, but not used as URI resource
				return 4;

			} else if (!literal.isBlank()) {
				// Non-empty literals
				return 3;

			} else {
				// No value [sic]
				return 0;
			}

		} else {
			// Other cases
			return 0;
		}
	}

	protected boolean isPropValidUrl(Resource resource, Property property) {
		StmtIterator objectIterator = resource.listProperties(property);
		while (objectIterator.hasNext()) {
			RDFNode object = objectIterator.next().getObject();
			if (object.isURIResource()) {
				return isValidUrl(object.asResource().getURI());
			} else if (object.isLiteral()) {
				return isValidUrl(object.asLiteral().getString());
			}
		}
		return false;
	}

	protected static boolean isValidUrl(String url) {
		try {
			new URL(url);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_PROVIDER_IDENTITY.getURI();
	}

}