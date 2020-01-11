package org.dice_research.opal.civet;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.common.vocabulary.Dqv;

/**
 * Utility methods, which are not part of the Civet API.
 * 
 * @author Adrian Wilke
 */
public abstract class Utils {

	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Creates statements to insert a metric score.
	 */
	public static List<Statement> createMetricStatements(Resource dataset, Resource metric, int score) {
		Resource measurement = ResourceFactory.createResource();
		Literal scoreLiteral = ResourceFactory.createTypedLiteral(String.valueOf(score), XSDDatatype.XSDinteger);

		List<Statement> statements = new ArrayList<Statement>(4);
		statements.add(ResourceFactory.createStatement(dataset, Dqv.HAS_QUALITY_MEASUREMENT, measurement));
		statements.add(ResourceFactory.createStatement(measurement, RDF.type, Dqv.QUALITY_MEASUREMENT));
		statements.add(ResourceFactory.createStatement(measurement, Dqv.IS_MEASUREMENT_OF, metric));
		statements.add(ResourceFactory.createStatement(measurement, Dqv.HAS_VALUE, scoreLiteral));

		return statements;
	}

	/**
	 * Removes existing measurements.
	 */
	protected static void removeAllMeasurements(Model model, Resource dataset) {
		NodeIterator measurements = model.listObjectsOfProperty(dataset, Dqv.HAS_QUALITY_MEASUREMENT);
		while (measurements.hasNext()) {
			RDFNode measurement = measurements.next();
			if (measurement.isResource()) {
				measurement.asResource().removeProperties();
				model.remove(dataset, Dqv.HAS_QUALITY_MEASUREMENT, measurement);
			} else {
				LOGGER.warn(
						"Measurement is not a resource: " + measurement.toString() + ", dataset " + dataset.getURI());
			}
		}
	}
}
