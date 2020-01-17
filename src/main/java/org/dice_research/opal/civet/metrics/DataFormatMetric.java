package org.dice_research.opal.civet.metrics;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.io.File;
import java.io.IOException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;


/**
 * The DataFormatMetric gives a rating to a dataset based on 
 * whether a data-format or file-format information is available
 * or not.
 * 
 * Data/file format information is normally available at distribution
 * level which is expressed with predicate dct:format or dcat:mediaType.
 * This metric checks how many distributions have an object value for
 * dct:format or dcat:mediaType and based on that an average rating 
 * is calculated.
 * 
 * @see https://www.w3.org/TR/vocab-dcat-1/#Property:distribution_format
 * @see https://www.w3.org/TR/vocab-dcat-1/#Property:distribution_media_type
 * 
 * @author Gourab Sahu
 */

public class DataFormatMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = 
			  "A dataset can have many distributions, individual score for each distribution are calculated"
			 +"A distribution gets 5 star if it has a non-empty object for property dcat:mediatype or dct:format"
			 +"Finally scores per each distribution are summed up and an average score for the dataset is calculated."
			 ;
	
	
	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {

		LOGGER.info("Processing dataset " + datasetUri);

		Resource dataset = ResourceFactory.createResource(datasetUri);

		//Total number of distributions in a dataset.
				int total_distributions = 0;
				
				//Store a score for each distribution, we will use it for final evaluation.
				HashMap<String, Integer> distributions_and_scores = new HashMap<String, Integer>();	
				
				NodeIterator distributions_iterator = model.listObjectsOfProperty(dataset, DCAT.distribution);
					
					while(distributions_iterator.hasNext()) {
						
						/*
						 * Give 5 stars to a distribution if it has information about 
						 * MediaType or Dataformat. Else give 0 stars.
						 */
						Resource distribution = (Resource) distributions_iterator.nextNode();
						if(distribution.hasProperty(DCTerms.format))
						{
							if(!(distribution.getProperty(DCTerms.format).getObject().isAnon()) && !(distribution.getProperty(DCTerms.format).getObject().toString().isEmpty()))
									distributions_and_scores.put(distribution.toString(), 5);
						}
						else if(distribution.hasProperty(DCAT.mediaType)) {
							
							if(!(distribution.getProperty(DCAT.mediaType).getObject().isAnon()) && !(distribution.getProperty(DCAT.mediaType).getObject().toString().isEmpty()))
								distributions_and_scores.put(distribution.toString(), 5);
						}
						else
							distributions_and_scores.put(distribution.toString(), 0);
						
						//To calculate how many distributions in a dataset. It will be used for scoring.
						total_distributions++;
				}
				
					/**
					 * Score Evaluation:
					 * Total Number of distributions in Dataset = x
					 * Total aggregated scores of all distributions = y
					 * Overall Score = y/x
					 */
					float aggregatedScoreOfAllDistributions = 0;
					int overall_score = 0;
					for (String key : distributions_and_scores.keySet()) {
						aggregatedScoreOfAllDistributions+=distributions_and_scores.get(key);
					}
					
					overall_score = (int) Math.ceil(aggregatedScoreOfAllDistributions/total_distributions);

				return overall_score;

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
