package org.dice_research.opal.civet;

/**
 * Test configuration
 * 
 * TODO: Put in properties file or use system variables
 * 
 * @author Adrian Wilke
 */
public abstract class Config {

	public static String sparqlQueryEndpointHost = "opalpro.cs.upb.de";
	public static int sparqlQueryEndpointPort = 3030;
	public static String sparqlQueryEndpointPath = "/civet/sparql";
	public static String sparqlQueryEndpoint = "http://" + sparqlQueryEndpointHost + ":" + sparqlQueryEndpointPort
			+ sparqlQueryEndpointPath;

	public static String datasetUriEuroPortal = "http://europeandataportal.projekt-opal.de/dataset/https-datenregister-berlin-de-dataset-ff105c67-6fb2-46da-9eac-15c730be8921";
	public static String datasetUriMcloud = "http://mcloud.projekt-opal.de/dataset/stadtklnverkehrsunfallentwicklungstadtgebietkoeln2017";
}