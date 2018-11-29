package org.dice_research.opal.civet.webdemo;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;

import org.dice_research.opal.civet.CivetApi;

public class CivetHandler extends AbstractHandler {

	@Override
	public void handle() throws WebserverIoException {

		StringBuilder htmlBuilder = new StringBuilder("<h2>Metric results</h2>");
		if (parameters.containsKey("uri")) {
			try {
				htmlBuilder.append("<ul>");
				Map<String, Float> scores = executeCivet(parameters.get("uri"));
				for (Entry<String, Float> score : scores.entrySet()) {
					htmlBuilder.append("<li>");
					htmlBuilder.append(score.getValue());
					htmlBuilder.append(" ");
					htmlBuilder.append(score.getKey());
					htmlBuilder.append("</li>");
				}
				htmlBuilder.append("</ul>");
				setOkWithBody(htmlBuilder.toString());
			} catch (URISyntaxException e) {
				LOGGER.error(e);
				setInternalServerError(e.getMessage());
				return;
			}

		} else {
			htmlBuilder.append("No dataset URI defined");
			setOkWithBody(htmlBuilder.toString());
		}

	}

	public Map<String, Float> executeCivet(String datasetUri) throws URISyntaxException {
		CivetApi civetApi = new CivetApi().setSparqlQueryEndpoint(Configuration.ENDPOINT);
		try {
			return civetApi.compute(new URI(datasetUri), civetApi.getAllMetricIds());
		} finally {
			civetApi.getRdfConnection().close();
		}
	}

}
