package org.dice_research.opal.civet.webdemo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionRemote;

public class DatasetHandler extends AbstractHandler {

	@Override
	public void handle() throws WebserverIoException {
		StringBuilder htmlBuilder = new StringBuilder("<h2>Random datasets</h2>");
		htmlBuilder.append("<ul>");
		for (String dataset : getRandomDatasets()) {
			datasetToHtml(htmlBuilder, dataset);
		}
		htmlBuilder.append("</ul>");
		setOkWithBody(htmlBuilder.toString());

	}

	private void datasetToHtml(StringBuilder stringBuilder, String dataset) throws WebserverIoException {
		stringBuilder.append("<li>");
		stringBuilder.append("<a href=\"../civet/");
		stringBuilder.append("?uri=");
		try {
			stringBuilder.append(URLEncoder.encode(dataset, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new WebserverIoException(e);
		}
		stringBuilder.append("\">");
		stringBuilder.append(StringEscapeUtils.escapeHtml4(dataset));
		stringBuilder.append("</a>");
		stringBuilder.append("</li>");
	}

	private List<String> getRandomDatasets() {
		RDFConnection rdfConnection = RDFConnectionRemote.create().destination(Configuration.ENDPOINT).build();
		List<String> datasets = new LinkedList<String>();
		Query query = QueryFactory.create(SparqlQueries.RANDOM_DATASETS);
		QueryExecution queryExecution = rdfConnection.query(query);
		ResultSet resultSet = queryExecution.execSelect();
		while (resultSet.hasNext()) {
			datasets.add(resultSet.next().get("?dataset").toString());
		}
		rdfConnection.close();
		return datasets;
	}

}