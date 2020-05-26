package org.dice_research.opal.civet.metrics;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCAT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.Metric;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * The Accessibility awards stars based on the Accessibility of metadata.
 * A connection is made using the URL and ratings are evaluated based on returned status code.
 *
 * @see https://kinsta.com/blog/http-status-codes/
 * @see https://stackoverflow.com/a/3584332
 *
 * @author Amit Kumar
 */

public class AccessibilityMetric implements Metric {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DESCRIPTION = "Computes the quality of dataset as per contactability metric." 
			+ "Four kinds of ratings are awarded to the dataset which are following: "
			+ "5 Stars: If the URL is in the dataset and returns a successfull code HTTP 200: “Everything is OK.” upon making connection."
			+ "2 Stars: If the URL is in the dataset and returns a status code HTTP 401: “Unauthorized” or “Authorization Required.” "
			+ "1 Stars: If the URL is in the dataset and it return a status code 403: “Access to that resource is forbidden.” "
			+ "0 Stars: If the URL is not found in the dataset.";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		LOGGER.info("Processing dataset " + datasetUri);
		Resource dataset = ResourceFactory.createResource(datasetUri);
		NodeIterator distributionObjectsIterator = model.listObjectsOfProperty(dataset,DCAT.distribution);
 		int result = 0;
 		HashMap<URL,  Integer> URLRatingMap=new HashMap<URL,Integer>();    
 		int countURL=0;
		URL urlObj;
		while(distributionObjectsIterator.hasNext()) {
			countURL++;
			Resource distribution = (Resource) distributionObjectsIterator.next();

			if (distribution.hasProperty(DCAT.accessURL)) {
				RDFNode accessUrl = distribution.getProperty(DCAT.accessURL).getObject();

				// creating a connection using URL and providing a rating based on the status code returned.
				urlObj = new URL(accessUrl.toString());
				HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
				try {
					con.setRequestMethod("HEAD");
					con.setConnectTimeout(3000);
					con.connect();
					int responseCode = con.getResponseCode();

					if (200 <= responseCode && responseCode <= 399) {
						result = 5;
						URLRatingMap.put(urlObj, result);
					} else if (400 <= responseCode && responseCode < 500) {
						result = 2;
						URLRatingMap.put(urlObj, result);
					} else if (500 <= responseCode && responseCode <= 521) {
						result = 1;
						URLRatingMap.put(urlObj, result);
					} else {
						result = 0;
						URLRatingMap.put(urlObj, result);
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
					result = 0;
				} catch (IOException e) {
					e.printStackTrace();
					result = 0;
				} catch (Exception e) {
					result = 0;
				} finally {
					if (con != null) {
						con.disconnect();
					}
				}
			}
			else{
				result = 0;
				URLRatingMap.put(null, result);
			}
			if(distribution.hasProperty(DCAT.downloadURL)){
				RDFNode downloadUrl = distribution.getProperty(DCAT.downloadURL).getObject();

				// creating a connection using URL and providing a rating based on the status code returned.
				urlObj = new URL(downloadUrl.toString());
				System.out.println(urlObj);

				HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
				try {
					con.setRequestMethod("HEAD");
					con.setConnectTimeout(3000);
					con.connect();
					int responseCode = con.getResponseCode();

					if (200 <= responseCode && responseCode <= 399) {
						result = 5;
						URLRatingMap.put(urlObj, result);
					} else if (400 <= responseCode && responseCode < 500) {
						result = 2;
						URLRatingMap.put(urlObj, result);
					} else if (500 <= responseCode && responseCode <= 521) {
						result = 1;
						URLRatingMap.put(urlObj, result);
					} else {
						result = 0;
						URLRatingMap.put(urlObj, result);
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
					result = 0;
				} catch (IOException e) {
					e.printStackTrace();
					result = 0;
				} catch (Exception e) {
					result = 0;
				} finally {
					if (con != null) {
						con.disconnect();
					}
				}

			}
			else{
				result = 0;
				URLRatingMap.put(null, result);
			}

		}
		// computing an average of URL ratings from hashmap
		int sumRating=0;
		int averageRating=0;
		
		for (Integer i : URLRatingMap.values()) {
			sumRating+=i;
		    }
		try {
			averageRating = Math.round(sumRating / countURL);
		}
		catch(ArithmeticException e){
			averageRating=0;
		}

		return averageRating;
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