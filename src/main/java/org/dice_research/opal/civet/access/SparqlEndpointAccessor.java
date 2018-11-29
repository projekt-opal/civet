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

	protected String endpoint;
	protected RDFConnection rdfConnection;

	public SparqlEndpointAccessor(String endpoint) {
		this.endpoint = endpoint;
	}

	public SparqlEndpointAccessor connect() {
		rdfConnection = RDFConnectionRemote.create().destination(endpoint).build();
		return this;
	}

	public void close() {
		if (rdfConnection != null && !rdfConnection.isClosed()) {
			rdfConnection.close();
		}
	}
}