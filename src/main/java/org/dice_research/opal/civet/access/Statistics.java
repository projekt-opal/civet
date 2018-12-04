package org.dice_research.opal.civet.access;

import org.apache.jena.query.ResultSetFormatter;

/**
 * Prints statistics of SPARQL endpoint.
 * 
 * @author Adrian Wilke
 */
public class Statistics extends SparqlEndpointAccessor {

	public Statistics(String endpoint) {
		super(endpoint);
	}

	@Override
	public Statistics connect() {
		super.connect();
		return this;
	}

	public void print(String query) {
		System.out.println(query);
		rdfConnection.queryResultSet(query, ResultSetFormatter::out);
		System.out.println();
	}

	public void print() {
		String query;

		// General

		query = "SELECT DISTINCT ?concept WHERE {[] a ?concept} ORDER BY ?concept";
		System.out.println(query);
		rdfConnection.queryResultSet(query, ResultSetFormatter::out);
		System.out.println();

		query = "SELECT (COUNT(?s) AS ?triples) WHERE { ?s ?p ?o }";
		System.out.println(query);
		rdfConnection.queryResultSet(query, ResultSetFormatter::out);
		System.out.println();

		query = "SELECT DISTINCT ?predicate WHERE { ?s ?predicate ?o } ORDER BY ?predicate";
		System.out.println(query);
		rdfConnection.queryResultSet(query, ResultSetFormatter::out);
		System.out.println();

		// Catalog

		query = "SELECT DISTINCT ?catalog WHERE { ?catalog a <http://www.w3.org/ns/dcat#Catalog> } LIMIT 5";
		System.out.println(query);
		rdfConnection.queryResultSet(query, ResultSetFormatter::out);
		System.out.println();

		System.out.println();
		System.out.println();

		// Distribution

		query = "SELECT DISTINCT (COUNT(?distribution) as ?distributions) WHERE { ?distribution a <http://www.w3.org/ns/dcat#Distribution> }";
		System.out.println(query);
		rdfConnection.queryResultSet(query, ResultSetFormatter::out);
		System.out.println();

		query = "SELECT DISTINCT ?distribution WHERE { ?distribution a <http://www.w3.org/ns/dcat#Distribution> } LIMIT 5";
		System.out.println(query);
		rdfConnection.queryResultSet(query, ResultSetFormatter::out);
		System.out.println();

		query = "SELECT DISTINCT ?predicate WHERE { ?s a <http://www.w3.org/ns/dcat#Distribution> . ?s ?predicate ?o } ORDER BY ?predicate";
		System.out.println(query);
		rdfConnection.queryResultSet(query, ResultSetFormatter::out);
		System.out.println();

		System.out.println();
		System.out.println();

		// Dataset

		query = "SELECT DISTINCT (COUNT(?dataset) as ?datasets) WHERE { ?dataset a <http://www.w3.org/ns/dcat#Dataset> }";
		System.out.println(query);
		rdfConnection.queryResultSet(query, ResultSetFormatter::out);
		System.out.println();

		query = "SELECT DISTINCT ?dataset WHERE { ?dataset a <http://www.w3.org/ns/dcat#Dataset> } LIMIT 5";
		System.out.println(query);
		rdfConnection.queryResultSet(query, ResultSetFormatter::out);
		System.out.println();

		query = "SELECT DISTINCT ?predicate WHERE { ?s a <http://www.w3.org/ns/dcat#Dataset> . ?s ?predicate ?o } ORDER BY ?predicate";
		System.out.println(query);
		rdfConnection.queryResultSet(query, ResultSetFormatter::out);
		System.out.println();

		System.out.println();
		System.out.println();

		// Combinations

		query = "SELECT DISTINCT ?dataset (COUNT(?distribution) as ?distributions) "
				+ "WHERE { ?dataset a <http://www.w3.org/ns/dcat#Dataset> . ?dataset <dcat:distribution> ?distribution . ?distribution a <http://www.w3.org/ns/dcat#Distribution> } "
				+ "GROUP BY ?dataset " + "ORDER BY DESC(?distributions) LIMIT 5";
		System.out.println(query);
		rdfConnection.queryResultSet(query, ResultSetFormatter::out);
		System.out.println();

		// URLs of a dataset

		String dataset = "<http://europeandataportal.projekt-opal.de/dataset/donnees-temps-reel-de-mesure-des-concentrations-de-polluants-atmospheriques-reglementes-1>";
		query = "SELECT DISTINCT ?accessURL ?downloadURL " + "WHERE { "
				+ "?dataset a <http://www.w3.org/ns/dcat#Dataset> . " + "?dataset <dcat:distribution> ?distribution . "
				+ dataset + " <dcat:distribution> ?distribution . "
				+ "OPTIONAL { ?distribution <http://www.w3.org/ns/dcat#accessURL> ?accessURL } . "
				+ "OPTIONAL { ?distribution <http://www.w3.org/ns/dcat#downloadURL> ?downloadURL } " + "} LIMIT 5 ";
		System.out.println(query);
		rdfConnection.queryResultSet(query, ResultSetFormatter::out);
		System.out.println();

	}

	/**
	 * Main entry point.
	 * 
	 * @param args
	 *            [0] SPARQL endpoint
	 * 
	 *            [1] (optional): SPARQL query
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Please provide a SPARQL endpoint.");
			System.exit(1);
		}

		String query = null;

		// Define a query here in Java to just execute it.

		if (args.length > 1) {
			query = args[1];
		}

		Statistics statistics = new Statistics(args[0]).connect();
		if (query == null) {
			statistics.print();
		} else {
			statistics.print(query);
		}
		statistics.close();
	}
}