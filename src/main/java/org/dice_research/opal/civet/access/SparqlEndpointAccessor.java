package org.dice_research.opal.civet.access;

import java.net.URISyntaxException;

import org.apache.jena.arq.querybuilder.SelectBuilder;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.query.Query;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionRemote;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.lang.sparql_11.ParseException;

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

	public static void main(String[] args) throws ParseException, URISyntaxException {
		// TODO sparql dev

		String dataset = "http://europeandataportal.projekt-opal.de/dataset/0021";
		Node datasetNode = NodeFactory.createURI(dataset);

		// "SELECT DISTINCT ?d ?p WHERE { "
		// + "OPTIONAL { <http://europeandataportal.projekt-opal.de/dataset/0022>
		// <http://purl.org/dc/terms/description> ?d } . "
		// + "OPTIONAL { <http://europeandataportal.projekt-opal.de/dataset/0022>
		// <http://purl.org/dc/terms/publisher> ?p } "
		// + "} ";

		SelectBuilder selectBuilder = new SelectBuilder();
		selectBuilder.addVar("d").addOptional("?s", NodeFactory.createURI("http://purl.org/dc/terms/description"),
				"?d");
		selectBuilder.addVar("p").addOptional("?s", NodeFactory.createURI("http://purl.org/dc/terms/publisher"), "?p");
		selectBuilder.setVar(Var.alloc("s"), dataset);
		Query query = selectBuilder.build();
		System.out.println(query);

		// Following: Bind variable to uri

		// "SELECT DISTINCT ?d ?p WHERE { "
		// + "values ?s { <http://europeandataportal.projekt-opal.de/dataset/0022> }"
		// + "OPTIONAL { ?s <http://purl.org/dc/terms/description> ?d } . "
		// + "OPTIONAL { ?s <http://purl.org/dc/terms/publisher> ?p } " + "} "

		// SelectBuilder selectBuilder = new SelectBuilder();
		// selectBuilder.addVar("d").addOptional("?ss",
		// NodeFactory.createURI("http://purl.org/dc/terms/description"),
		// "?d");
		// selectBuilder.addVar("p").addOptional("?ss",
		// NodeFactory.createURI("http://purl.org/dc/terms/publisher"), "?p");
		// selectBuilder.addBind("?ss", "?s");
		// selectBuilder.setVar(Var.alloc("?s"), datasetNode);
		// Query query = selectBuilder.build();
		// System.out.println(query);
	}
}