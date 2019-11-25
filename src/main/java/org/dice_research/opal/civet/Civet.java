package org.dice_research.opal.civet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.civet.metrics.CategorizationMetric;
import org.dice_research.opal.common.interfaces.JenaModelProcessor;
import org.dice_research.opal.common.vocabulary.Dqv;
import org.dice_research.opal.common.vocabulary.Opal;

/**
 * Civet - OPAL quality metric component.
 *
 * @author Adrian Wilke
 */
public class Civet implements JenaModelProcessor {

	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Computes quality metric scores.
	 */
	@Override
	public Model process(Model model, String datasetUri) throws Exception {
		Resource dataset = ResourceFactory.createResource(datasetUri);

		Model returnModel = ModelFactory.createDefaultModel();
		returnModel.add(model);

		// TODO: Remove existing metric scores.

		for (Entry<Resource, Metric> metric : getMetrics().entrySet()) {
			Integer score = null;

			try {
				score = metric.getValue().compute(model, datasetUri);
			} catch (Exception e) {
				LOGGER.error("Exception on computing " + metric.getValue().getUri() + " for " + datasetUri, e);
				continue;
			}

			returnModel.add(createMetricStatements(dataset, metric.getKey(), score));
		}

		return returnModel;
	}

	/**
	 * Gets map of metric URIs and related metric instances.
	 */
	public Map<Resource, Metric> getMetrics() {
		Map<Resource, Metric> metrics = new HashMap<Resource, Metric>();

		metrics.put(Opal.OPAL_METRIC_CATEGORIZATION, new CategorizationMetric());

		return metrics;
	}

	/**
	 * Creates statements to insert a metric score.
	 */
	protected List<Statement> createMetricStatements(Resource dataset, Resource metric, int score) {
		Resource measurement = ResourceFactory.createResource();
		Literal scoreLiteral = ResourceFactory.createTypedLiteral(String.valueOf(score), XSDDatatype.XSDinteger);

		List<Statement> statements = new ArrayList<Statement>(4);
		statements.add(ResourceFactory.createStatement(dataset, Dqv.HAS_QUALITY_MEASUREMENT, measurement));
		statements.add(ResourceFactory.createStatement(measurement, RDF.type, Dqv.QUALITY_MEASUREMENT));
		statements.add(ResourceFactory.createStatement(measurement, Dqv.IS_MEASUREMENT_OF, metric));
		statements.add(ResourceFactory.createStatement(measurement, Dqv.HAS_VALUE, scoreLiteral));

		return statements;
	}

}