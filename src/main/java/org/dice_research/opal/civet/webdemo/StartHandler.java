package org.dice_research.opal.civet.webdemo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringEscapeUtils;

public class StartHandler extends AbstractHandler {

	@Override
	public void handle() throws WebserverIoException {

		String html = getResource(Templates.START);
		html = html.replace(Templates.START_HTML_MARKER_ENDPOINT, Configuration.ENDPOINT);

		StringBuilder queryBuilder = new StringBuilder();
		sparqlToHtml(queryBuilder, SparqlQueries.COUNT_TRIPLES, "Count triples");
		sparqlToHtml(queryBuilder, SparqlQueries.COUNT_DISTRIBUTION, "Count distributions");
		sparqlToHtml(queryBuilder, SparqlQueries.COUNT_DATASET, "Count datasets");
		sparqlToHtml(queryBuilder, SparqlQueries.CONCEPTS, "List concepts");
		sparqlToHtml(queryBuilder, SparqlQueries.PREDICATES, "List predicates");
		sparqlToHtml(queryBuilder, SparqlQueries.PREDICATES_DISTRIBUTIONS, "List predicates of distributions");
		sparqlToHtml(queryBuilder, SparqlQueries.PREDICATES_DATASETS, "List predicates of datasets");
		sparqlToHtml(queryBuilder, SparqlQueries.RANDOM_DISTRIBUTIONS, "List random distributions");
		sparqlToHtml(queryBuilder, SparqlQueries.RANDOM_DATASETS, "List random datasets");
		html = html.replace(Templates.START_HTML_MARKER_ENDPOINT_QUERIES, queryBuilder);

		setOkWithBody(html);
	}

	private void sparqlToHtml(StringBuilder stringBuilder, String sparqlQuery, String linkText)
			throws WebserverIoException {
		stringBuilder.append("<li>");
		stringBuilder.append("<a href=\"");
		stringBuilder.append(Configuration.ENDPOINT);
		stringBuilder.append("?format=text%2Fhtml&query=");
		try {
			stringBuilder.append(URLEncoder.encode(sparqlQuery, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new WebserverIoException(e);
		}
		stringBuilder.append("\">");
		stringBuilder.append(linkText);
		stringBuilder.append("</a> (");
		stringBuilder.append(StringEscapeUtils.escapeHtml4(sparqlQuery));
		stringBuilder.append(")</li>");
	}
}