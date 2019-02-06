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

	public static String sparqlUpdateEndpointHost = "opalpro.cs.upb.de";
	public static int sparqlUpdateEndpointPort = 3030;
	public static String sparqlUpdateEndpointPath = "/civet/update";
	public static String sparqlUpdateEndpoint = "http://" + sparqlUpdateEndpointHost + ":" + sparqlUpdateEndpointPort
			+ sparqlUpdateEndpointPath;

	public static String namedGraph = "http://projekt-opal.de";

	// Fuseki, port 3030, January 2019
	public static String datasetUriBerlin = "http://projekt-opal.de/dataset/berlinumweltzone-wms";
	public static String datasetUriZugbildungsplan = "http://projekt-opal.de/dataset/2a490c08-92dd-4aba-af96-cbf9d5f02f9a";
}