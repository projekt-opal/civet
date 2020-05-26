package org.dice_research.opal.civet.example;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dice_research.opal.common.utilities.FileHandler;
import org.junit.Test;

/**
 * Minimal working example.
 *
 * @author Adrian Wilke
 */
public class ExampleTest {

	/**
	 * Delete test files on exit. Should be true by default. If is false, the path
	 * of created files are logged.
	 */
	public static final boolean DELETE_ON_EXIT = true;

	private static final Logger LOGGER = LogManager.getLogger();

	@Test
	public void test() throws Exception {

		if (!DELETE_ON_EXIT) {
			LOGGER.warn("DELETE_ON_EXIT is false");
		}

		File turtleInputFile = File.createTempFile(ExampleTest.class.getName(), ".in.txt");
		FileHandler.export(turtleInputFile, getModel());
		if (DELETE_ON_EXIT) {
			turtleInputFile.deleteOnExit();
		} else {
			LOGGER.info("Created file: " + turtleInputFile.getAbsolutePath());
		}

		File turtleOutputFile = File.createTempFile(ExampleTest.class.getName(), "");
		if (DELETE_ON_EXIT) {
			turtleOutputFile.deleteOnExit();
		} else {
			LOGGER.info("Created file: " + turtleOutputFile.getAbsolutePath());
		}

		Example example = new Example();
		example.evaluateMetadata(turtleInputFile, turtleOutputFile, getDatasetUri());

		if (!DELETE_ON_EXIT) {
			System.out.println(FileUtils.readFileToString(turtleOutputFile, StandardCharsets.UTF_8));
		}

		assertTrue(turtleOutputFile.exists());
		assertNotEquals(turtleInputFile.length(), turtleOutputFile.length());
	}

	protected String getDatasetUri() {
		return "https://www.kreis-paderborn.de/kreis_paderborn/geoportal/opendata/";
	}

	protected Model getModel() {

		// Example based on
		// https://www.kreis-paderborn.de/kreis_paderborn/geoportal/opendata/

		Model model = ModelFactory.createDefaultModel();

		String datasetUri = getDatasetUri();
		Resource dataset = ResourceFactory.createResource(datasetUri);
		model.add(dataset, RDF.type, DCAT.Dataset);

		Literal keywordA = ResourceFactory.createPlainLiteral("Paderborn");
		model.addLiteral(dataset, DCAT.keyword, keywordA);

		Literal keywordB = ResourceFactory.createPlainLiteral("map");
		model.addLiteral(dataset, DCAT.keyword, keywordB);

		Literal accrualPeriodicity = ResourceFactory.createPlainLiteral("IRREG");
		model.addLiteral(dataset, DCTerms.accrualPeriodicity, accrualPeriodicity);

		Literal license = ResourceFactory.createPlainLiteral("http://www.govdata.de/dl-de/by-2-0");
		model.addLiteral(dataset, DCTerms.license, license);

		Resource publisher = ResourceFactory.createResource("https://www.kreis-paderborn.de/");
		model.add(publisher, RDF.type, FOAF.Agent);
		model.add(dataset, DCTerms.publisher, publisher);

		String distributionUri = "https://www.kreis-paderborn.de/opendata/alkis/";
		Resource distribution = ResourceFactory.createResource(distributionUri);
		model.add(distribution, RDF.type, DCAT.Distribution);
		model.add(dataset, DCAT.distribution, distribution);

		Literal accessURL = ResourceFactory.createPlainLiteral(distributionUri);
		model.addLiteral(distribution, DCAT.accessURL, accessURL);

		return model;
	}

}