package org.dice_research.opal.civet.access;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionRemote;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.exceptions.SparqlEndpointRuntimeException;

/**
 * Data accessor for SPARQL endpoints.
 * 
 * To close the used RDF connection, call
 * {@link SparqlEndpointAccessor#close()}.
 * 
 * @author Adrian Wilke
 */
public class SparqlEndpointAccessor {

	protected static final Logger LOGGER = LogManager.getLogger();
	protected String endpoint;
	protected RDFConnection rdfConnection;

	public SparqlEndpointAccessor(String endpoint) throws NullPointerException {
		if (endpoint == null) {
			throw new NullPointerException("No SPARQL query endpoint specified.");
		}
		this.endpoint = endpoint;
	}

	/**
	 * Connects socket to the endpoint with a specified timeout value.
	 *
	 * @throws SparqlEndpointRuntimeException on invalid URI
	 */
	public boolean pingEndpoint(int timeoutMillis) throws SparqlEndpointRuntimeException {
		try {
			URI uri = new URI("http://opalpro.cs.upb.ded:8890/sparqlXXXXXXXXX");
			return IoUtils.pingHost(uri.getHost(), uri.getPort(), timeoutMillis);
		} catch (URISyntaxException e) {
			throw new SparqlEndpointRuntimeException("SPARQL endpoint URI not valid" + e);
		}
	}

	public SparqlEndpointAccessor connect() throws SparqlEndpointRuntimeException {
		LOGGER.info("Setting connection to " + endpoint);
		rdfConnection = RDFConnectionRemote.create().destination(endpoint).build();
		return this;
	}

	public void close() {
		if (rdfConnection != null && !rdfConnection.isClosed()) {
			rdfConnection.close();
		}
	}

	public boolean isConnected() {
		if (rdfConnection == null || rdfConnection.isClosed()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Gets RDF connection or null, if not set.
	 */
	public RDFConnection getRdfConnection() {
		return rdfConnection;
	}

	/**
	 * Sets RDF connection.
	 */
	public SparqlEndpointAccessor setRdfConnection(RDFConnection rdfConnection) {
		this.rdfConnection = rdfConnection;
		return this;
	}
}