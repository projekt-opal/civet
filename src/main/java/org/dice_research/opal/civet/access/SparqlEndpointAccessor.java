package org.dice_research.opal.civet.access;

import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionRemote;

/**
 * Data accessor for SPARQL endpoints.
 * 
 * To close the used RDF connection, call
 * {@link SparqlEndpointAccessor#close()}.
 * 
 * @author Adrian Wilke
 */
public class SparqlEndpointAccessor {

	// TODO Examples:
	// https://www.wikidata.org/wiki/Wikidata:SPARQL_query_service/queries/examples

	protected String endpoint;
	protected RDFConnection rdfConnection;

	public SparqlEndpointAccessor(String endpoint) {
		this.endpoint = endpoint;
	}

	public SparqlEndpointAccessor connect() {
		// Examples: https://jena.apache.org/documentation/rdfconnection/
		rdfConnection = RDFConnectionRemote.create().destination(endpoint).build();
		return this;
	}

	public void close() {
		if (rdfConnection != null && !rdfConnection.isClosed()) {
			rdfConnection.close();
		}
	}
}