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
 * Datasets are available in the form of distributions where 
 * each distribution is supposed to provide data/file format information
 * through dct:format or dcat:mediaType predicate. In this metric the 
 * validity of data/file format is not checked.
 * 
 * @see https://www.w3.org/TR/vocab-dcat-1/#Property:distribution_format
 * @see https://www.w3.org/TR/vocab-dcat-1/#Property:distribution_media_type
 * 
 * @author Gourab Sahu
 */
public class DataFormatMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = 
			 " A dataset can have many distributions, we will calculate individual score for each distribution and"
			 +"a distribution gets 5 star if it has non-empty value for property dcat:mediatype or dct:dataformat"
			 + "We will not check the validity the provided mediatype(IANA type) or dataformat as this is not feasible "
			 +"finally add all these scores and calculate the averge score"
			 +"We are doing a ceiling for the score e.g score 2.5 will be returned as 3."
			 ;
	
	
	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {

		LOGGER.info("Processing dataset " + datasetUri);

		Resource dataset = ResourceFactory.createResource(datasetUri);

		//Total number of distributions in a dataset.
				int TotalDistributions = 0;
				
				//Store a score for each distribution, we will use it for final evaluation.
				HashMap<String, Integer> DistributionsAndScores = new HashMap<String, Integer>();	
				
				NodeIterator DistributionsIterator = model.listObjectsOfProperty(dataset, DCAT.distribution);
					
					while(DistributionsIterator.hasNext()) {
						
						/*
						 * Give 5 stars to a distribution if it has information about 
						 * MediaType or Dataformat. Else give 0 stars.
						 */
						Resource Distribution = (Resource) DistributionsIterator.nextNode();
						if(Distribution.hasProperty(DCTerms.format))
						{
							if(!(Distribution.getProperty(DCTerms.format).getObject().isAnon()) && !(Distribution.getProperty(DCTerms.format).getObject().toString().isEmpty()))
									DistributionsAndScores.put(Distribution.toString(), 5);
						}
						else if(Distribution.hasProperty(DCAT.mediaType)) {
							
							if(!(Distribution.getProperty(DCAT.mediaType).getObject().isAnon()) && !(Distribution.getProperty(DCAT.mediaType).getObject().toString().isEmpty()))
								DistributionsAndScores.put(Distribution.toString(), 5);
						}
						else
							DistributionsAndScores.put(Distribution.toString(), 0);
						
						//To calculate how many distributions in a dataset. It will be used for scoring.
						TotalDistributions++;
				}
				
					/**
					 * Score Evaluation:
					 * Total Number of distributions in Dataset = x
					 * Total aggregated scores of all distributions = y
					 * Overall Score = y/x
					 */
					float AggregatedScoreOfAllDistributions = 0;
					int OverallScore = 0;
					for (String key : DistributionsAndScores.keySet()) {
						AggregatedScoreOfAllDistributions+=DistributionsAndScores.get(key);
						System.out.println(key +":"+ DistributionsAndScores.get(key));
					}
					
					OverallScore = (int) Math.ceil(AggregatedScoreOfAllDistributions/TotalDistributions);

				return OverallScore;

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
