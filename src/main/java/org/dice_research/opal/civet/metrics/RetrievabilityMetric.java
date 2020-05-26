package org.dice_research.opal.civet.metrics;

import java.net.HttpURLConnection;
import java.net.URL;

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
 * The retrievability metric awards stars based on the accessibility of download
 * and access URLs. A connection is made using the respective URL and ratings
 * are evaluated based on returned status code.
 *
 * @see https://tools.ietf.org/html/rfc7231#section-6
 * @see https://www.iana.org/assignments/http-status-codes/http-status-codes.xhtml
 * @see https://en.wikipedia.org/wiki/List_of_HTTP_status_codes
 *
 * @author Amit Kumar
 * @author Adrian Wilke
 */

public class RetrievabilityMetric implements Metric {

	private static final boolean LOG_ACCESSES = false;

	private static final int TIMEOUT = 1000;

	private static final Logger LOGGER = LogManager.getLogger();

	private static final String DESCRIPTION = "Awards stars based on the availablity of distribution files. "
			+ "For this, the HTTP return codes of download URLs and access URLs are evaluated.";

	@Override
	public Integer compute(Model model, String datasetUri) throws Exception {
		int distributionCounter = 0;
		int scoreSum = 0;

		Resource dataset = ResourceFactory.createResource(datasetUri);
		NodeIterator distributionIterator = model.listObjectsOfProperty(dataset, DCAT.distribution);
		while (distributionIterator.hasNext()) {

			// Add distribution
			RDFNode distributionNode = distributionIterator.next();
			if (!distributionNode.isURIResource()) {
				continue;
			}
			Resource distribution = distributionNode.asResource();
			distributionCounter++;

			// Check download URL
			int scoreDistribution = 0;
			if (distribution.hasProperty(DCAT.downloadURL)) {
				RDFNode downloadUrl = distribution.getProperty(DCAT.downloadURL).getObject();
				URL url = new URL(downloadUrl.toString());
				int responseCode = getResponseCode(url);
				if (LOG_ACCESSES) {
					LOGGER.info(responseCode + " downloadURL " + url);
				}
				scoreDistribution = getResponseCodeScore(responseCode);
			}
			// If not already best score, check access URL
			if (scoreDistribution < 5 && distribution.hasProperty(DCAT.accessURL)) {
				RDFNode accessUrl = distribution.getProperty(DCAT.accessURL).getObject();
				URL url = new URL(accessUrl.toString());
				int responseCode = getResponseCode(url);
				if (LOG_ACCESSES) {
					LOGGER.info(responseCode + " accessURL " + url);
				}
				int scoreAccess = getResponseCodeScore(responseCode);
				scoreDistribution = Math.max(scoreDistribution, scoreAccess);
			}

			scoreSum += scoreDistribution;
		}

		if (distributionCounter == 0) {
			return null;
		} else {
			return (int) Math.ceil(scoreSum / distributionCounter);
		}
	}

	/**
	 * Gets score based on response code.
	 */
	protected int getResponseCodeScore(int responseCode) {

		if (100 <= responseCode && responseCode < 200) {
			// 1xx Informational response
			return 4;

		} else if (200 <= responseCode && responseCode < 300) {
			// 2xx Success
			return 5;

		} else if (300 <= responseCode && responseCode < 400) {
			// 3xx Redirection
			return 4;

		} else if (400 <= responseCode && responseCode < 500) {
			// 4xx Client errors
			return 1;

		} else if (500 <= responseCode && responseCode < 600) {
			// 5xx Server errors
			return 1;

		} else {
			// Info: Score 0 is e.g. used at timeouts
			return 0;
		}
	}

	/**
	 * Returns HTTP response code or -1 on exceptions.
	 */
	protected int getResponseCode(URL url) {
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("HEAD");
			connection.setConnectTimeout(TIMEOUT);
			connection.connect();
			return connection.getResponseCode();
		} catch (Exception e) {
			LOGGER.warn(e);
			return -1;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getUri() throws Exception {
		return Opal.OPAL_METRIC_RETRIEVABILITY.getURI();
	}
}