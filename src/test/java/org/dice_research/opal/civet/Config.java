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
	public static String namedGraph = "http://projekt-opal.de";

	// Fuseki, port 3030, January 2019
	public static String datasetUriBerlin = "http://projekt-opal.de/dataset/berlinumweltzone-wms";
}