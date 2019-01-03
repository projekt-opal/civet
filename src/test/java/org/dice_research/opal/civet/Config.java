package org.dice_research.opal.civet;

public abstract class Config {

	public static String sparqlQueryEndpointHost = "opalpro.cs.upb.de";
	public static int sparqlQueryEndpointPort = 8890;
	public static String sparqlQueryEndpointPath = "/sparql";
	public static String sparqlQueryEndpoint = "http://" + sparqlQueryEndpointHost + ":" + sparqlQueryEndpointPort
			+ sparqlQueryEndpointPath;

	public static String datasetUriEuroPortal = "http://europeandataportal.projekt-opal.de/dataset/https-datenregister-berlin-de-dataset-ff105c67-6fb2-46da-9eac-15c730be8921";
	public static String datasetUriMcloud = "http://mcloud.projekt-opal.de/dataset/stadtklnverkehrsunfallentwicklungstadtgebietkoeln2017";

}
