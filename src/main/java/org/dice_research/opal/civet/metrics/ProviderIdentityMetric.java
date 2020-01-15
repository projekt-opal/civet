package org.dice_research.opal.civet.metrics;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The ProviderIdentity gives a rating to a dataset based on available
 * informations about the dataset publisher.
 * 
 * @author Gourab Sahu
 */
public class ProviderIdentityMetric implements Metric {

	public static boolean isValidURL(String checkURL) {
		/*
		 * Here we check whether the URL of foaf:homepage is a valid URL or not.
		 */
		try {
			new URL(checkURL).toURI();
			return true;
		}

		catch (Exception e) {
			return false;
		}
	}

	public void evaluatePublisher(RDFNode publisher, HashMap<String, Integer> PublisherScore) {

		Resource Publisher = (Resource) publisher;
		/*
		 * If a publisher is of type foaf:org or foaf:person and has an non-empty name
		 * then 5 stars are awarded.
		 */
		if ((Publisher.hasProperty(RDF.type, FOAF.Agent) && (!Publisher.getProperty(FOAF.name).getObject().toString().isEmpty()))) {

			/*
			 * If we do not have a publisher yet, then rate the first publisher ( of type
			 * foaf:org /foaf:name) 5 stars.
			 */
			if (PublisherScore.size() == 0)
				PublisherScore.put(Publisher.getProperty(FOAF.name).getObject().toString(), 5);
			/*
			 * If we already have a publisher and then we discover another new publisher
			 * then the publisher information is not consistent, because of inconsistency we
			 * award 3/5.
			 * 
			 * 2 star less for being inconsistent
			 */
			else if (!(PublisherScore.size() == 0)
					&& !(PublisherScore.containsKey(Publisher.getProperty(FOAF.name).getObject().toString()))) {
				PublisherScore.clear();
				PublisherScore.put("InconsistentPublishers", 3);
			}
		}
		/*
		 * If blanknode has a non-empty foaf:name but not sure about the type i.e not a
		 * foaf:organisation or foaf:Person then 4 stars are awarded for not following
		 * DCAT recommendations.
		 */
		else if (Publisher.hasProperty(FOAF.name)
				&& (!Publisher.getProperty(FOAF.name).getObject().toString().isEmpty())) {
			// If we find the first publisher but no mention of foaf:type then award 4
			// stars.
			// 1 star less for not following DCAT's Foaf recommendations
			if (PublisherScore.size() == 0
					&& !(PublisherScore.containsKey(Publisher.getProperty(FOAF.name).getObject().toString())))
				PublisherScore.put(Publisher.getProperty(FOAF.name).getObject().toString(), 4);
			/*
			 * If we already have a publisher and then we discover an another new publisher
			 * then the publisher information is not consistent, because of inconsistency we
			 * award 2/5 because of inconsistency + not following DCAT
			 * recommendation(foaf:type)
			 * 
			 * 2 star less for being inconsistent, -1 star less for not following DCAT foaf
			 * recommendation
			 */
			else if (!(PublisherScore.size() == 0)
					&& !(PublisherScore.containsKey(Publisher.getProperty(FOAF.name).getObject().toString()))) {
				PublisherScore.clear();
				PublisherScore.put("InconsistentPublishers", 2);
			}
		}

	}

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = "Check if a consistent Dataset Provider/Publisher name is given "
			+ "If dataset has only 1 Publisher then 5 atars are awarded "
			+ "If dataset has more than 1 publishers but they are consistent then 5 stars are awarded "
			+ "If dataset has more than 1 publishers but they are inconsistent then 3 stars are awarded, 2 star less for inconsistency "
			+ "If dataset has an empty blanknode for publlisher then 0 stars are awarded"

			+ "If dataset has a non-empty blanknode for publlisher with publisher info then 5 stars are awarded"
			+ "If dataset has more than 1 non-empty blanknode for publlisher with consistent publisher info then 5 stars are awarded"
			+ "If dataset has more than 1 non-empty blanknode for publlisher with inconsistent publisher info then 3 stars are awarded, 2 star less"
			+ "for Inconsistency." + "If dataset has no provider information at all then 0 stars are awarded"

			+ "Before awarding stars, we are checking whether the publishers are of type Foaf:Organization or Foaf:Person. IF this info is"
			+ "not there then additionaly 1 more star will be deducted from scoring"

			+ "If no publisher info given then check for dcat:landingPage and if found award 5 star"
			+ "If no publisher info, dcat:landingPage found then check for dcat:accessURL and award 5 stars if found.";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {

		LOGGER.info("Processing dataset " + datasetUri);

		Resource dataset = ResourceFactory.createResource(datasetUri);

		// NodeIterator nodeIterator = model.listObjectsOfProperty(dataset,
		// DCAT.keyword);

		// Score to return
		int score = 0;

		// Will be used to check for consistency
		HashMap<String, Integer> PublisherScore = new HashMap<String, Integer>();

		// We will store landingpages here
		ArrayList<String> LandingPageStore = new ArrayList<String>();

		// We will store accessURL here
		ArrayList<String> AccessURLStore = new ArrayList<String>();

		// Total number of distributions
		int TotalNumberOfDistributions = 0;

//		StmtIterator IteratorOverPublisher = model
//				.listStatements(new SimpleSelector(null, DCTerms.publisher, (RDFNode) null));

		NodeIterator IteratorOverPublisher = model.listObjectsOfProperty(dataset, DCTerms.publisher);

		while (IteratorOverPublisher.hasNext()) {

			// Statement StatementWithPublisher = IteratorOverPublisher.nextStatement();
			// RDFNode Publisher = StatementWithPublisher.getObject();
			RDFNode Publisher = IteratorOverPublisher.next();
			// Check if the publisher object is a blank node.
			// We 1st start with checking if a non-empty blank node.
			if (Publisher.isAnon()) {

				evaluatePublisher(Publisher, PublisherScore);

			} else if (Publisher.isURIResource()) {

				evaluatePublisher(Publisher, PublisherScore);
			} else
				/*
				 * 1. A Publisher could be an URI of type foaf:org or foaf:name. We checked
				 * that. 2. A Publisher could be a blanknode of type foaf:org or foaf:name. We
				 * checked that. 3. A Publisher could be an empty blank node.
				 */
				// This is the 3rd case. Award 0 star
				PublisherScore.put("EmptyBlankNodeForPublisher", 0);
		}

		/*
		 * If PublisherInfo=0 then check if dcat:landingPage is available in
		 * dcat:catalog
		 */
		// System.out.println(PublisherScore.size());
		// System.out.println(PublisherScore.containsKey("EmptyBlankNodeForPublisher"));
		if (PublisherScore.size() == 0 || PublisherScore.containsKey("EmptyBlankNodeForPublisher")) {

			NodeIterator landingpages = model.listObjectsOfProperty(dataset, DCAT.landingPage);

			while (landingpages.hasNext()) {

				Object landingPage = landingpages.next();
				// If we do not have any landingPage in store then add it.
				if (LandingPageStore.size() == 0) {
					if (isValidURL(landingPage.toString()))
						LandingPageStore.add(landingPage.toString());
				}

				else if (LandingPageStore.size() != 0) {
					// If landing pages are inconsistent then add the landing page to store.
					if (!(LandingPageStore.contains(landingPage.toString()))) {
						if (isValidURL(landingPage.toString()))
							LandingPageStore.add(landingPage.toString());
					}
					/*
					 * At the end if we have only one value in LandingPageStore then we will award 5
					 * stars to the dataset.
					 */
				}
			}

			if (LandingPageStore.size() == 1)
				PublisherScore.put("LandingPageScore", 5);
			else
				PublisherScore.put("LandingPageScore", 3);
		
			// If LandingPage is not there then check for AccessURL in the distribution
			if (LandingPageStore.size() == 0) {

				NodeIterator DistributionIterator = model.listObjectsOfProperty(dataset,DCAT.distribution);
				while (DistributionIterator.hasNext()) {

					TotalNumberOfDistributions++;

					Resource Distribution = (Resource) DistributionIterator.next();

					if (Distribution.hasProperty(DCAT.accessURL)) {

						if (isValidURL(Distribution.getProperty(DCAT.accessURL).getObject().toString()))
							AccessURLStore.add(Distribution.getProperty(DCAT.accessURL).getObject().toString());
					}
				}

				// Calculate a score based on availability of AccessURL and store a score in
				// PublisherScore.
				if (AccessURLStore.size() > 0) {

					int TotalPercentageOfAccessURL = (AccessURLStore.size() * 100) / TotalNumberOfDistributions;

					if (TotalPercentageOfAccessURL == 100)
						PublisherScore.put("AccessURLScore", 5);
					else if (TotalPercentageOfAccessURL < 100 && TotalPercentageOfAccessURL >= 75)
						PublisherScore.put("AccessURLScore", 4);
					else if (TotalPercentageOfAccessURL < 75 && TotalPercentageOfAccessURL >= 50)
						PublisherScore.put("AccessURLScore", 3);
					else if (TotalPercentageOfAccessURL < 50 && TotalPercentageOfAccessURL >= 25)
						PublisherScore.put("AccessURLScore", 2);
					else if (TotalPercentageOfAccessURL < 25 && TotalPercentageOfAccessURL > 0)
						PublisherScore.put("AccessURLScore", 1);
						
				}
				else
					PublisherScore.put("No provider identity information at all", 0);
			}
		}
		// There will be always only one key and value
		for (String key : PublisherScore.keySet())
			score = PublisherScore.get(key);

		return score;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_CATEGORIZATION.getURI();
	}

}