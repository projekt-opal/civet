package org.dice_research.opal.civet.webdemo;

/**
 * Constants for processing templates.
 *
 * @author Adrian Wilke
 */
public abstract class SparqlQueries {

	public final static String COUNT_TRIPLES = "SELECT (COUNT(?s) AS ?triples) WHERE { ?s ?p ?o }";
	public final static String COUNT_DISTRIBUTION = "SELECT DISTINCT (COUNT(?distribution) as ?distributions) WHERE { ?distribution a <http://www.w3.org/ns/dcat#Distribution> }";
	public final static String COUNT_DATASET = "SELECT DISTINCT (COUNT(?dataset) as ?datasets) WHERE { ?dataset a <http://www.w3.org/ns/dcat#Dataset> }";
	public final static String CONCEPTS = "SELECT DISTINCT ?concept WHERE {[] a ?concept} ORDER BY ?concept";
	public final static String PREDICATES = "SELECT DISTINCT ?predicate WHERE { ?s ?predicate ?o } ORDER BY ?predicate";
	public final static String PREDICATES_DATASETS = "SELECT DISTINCT ?predicate WHERE { ?s a <http://www.w3.org/ns/dcat#Dataset> . ?s ?predicate ?o } ORDER BY ?predicate";
	public final static String PREDICATES_DISTRIBUTIONS = "SELECT DISTINCT ?predicate WHERE { ?s a <http://www.w3.org/ns/dcat#Distribution> . ?s ?predicate ?o } ORDER BY ?predicate";
	public final static String RANDOM_DISTRIBUTIONS = "SELECT DISTINCT ?distribution WHERE { ?distribution a <http://www.w3.org/ns/dcat#Distribution> } ORDER BY RAND() LIMIT 10";
	public final static String RANDOM_DATASETS = "SELECT DISTINCT ?dataset WHERE { ?dataset a <http://www.w3.org/ns/dcat#Dataset> } ORDER BY RAND() LIMIT 10";
	

}