package org.dice_research.opal.civet.metrics;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.tika.Tika;
import com.google.common.net.MediaType;
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

import com.google.common.net.MediaType;

/**
 * The DataFormatMetric gives a rating to a dataset based on informations provided 
 * about the mediaType/Format of all distributions.
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
						if( (Distribution.hasProperty(DCAT.mediaType) || Distribution.hasProperty(DCTerms.format)) &&
								(!(Distribution.getProperty(DCAT.mediaType).toString().isEmpty()) || 
										!(Distribution.getProperty(DCTerms.format).toString().isEmpty())))  {

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
				int AggregatedScoreOfAllDistributions = 0;
				int score = 0;
				for (String key : DistributionsAndScores.keySet()) {
					AggregatedScoreOfAllDistributions+=DistributionsAndScores.get(key);
					System.out.println(key +":"+ DistributionsAndScores.get(key));
				}
				
				int OverallScoreinPercent = (AggregatedScoreOfAllDistributions*100)/TotalDistributions;
				
				if (OverallScoreinPercent == 100)
					score = 5;
				else if (OverallScoreinPercent < 100 && OverallScoreinPercent >= 75)
					score = 4;
				else if (OverallScoreinPercent < 75 && OverallScoreinPercent >= 50)
					score = 3;
				else if (OverallScoreinPercent < 50 && OverallScoreinPercent >= 25)
					score = 2;
				else if (OverallScoreinPercent < 25 && OverallScoreinPercent > 0)
					score = 1;

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
