package org.dice_research.opal.civet.example;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import org.dice_research.opal.common.utilities.FileHandler;
import org.junit.Test;

/**
 * Minimal working example.
 *
 * @author Adrian Wilke
 */
public class ExampleTest {

	@Test
	public void test() throws Exception {

		File turtleInputFile = File.createTempFile(ExampleTest.class.getName(), ".in.txt");
		FileHandler.export(turtleInputFile, getModel());
//		turtleInputFile.deleteOnExit();

		File turtleOutputFile = File.createTempFile(ExampleTest.class.getName(), "");
//		turtleOutputFile.deleteOnExit();

		Example example = new Example();
		example.evaluateMetadata(turtleInputFile, turtleOutputFile, getDatasetUri());

		assertTrue(turtleOutputFile.exists());
		assertNotEquals(turtleInputFile.length(), turtleOutputFile.length());
	}

	protected String getDatasetUri() {
		return "https://example.org/";
	}

	protected Model getModel() {

		Model model = ModelFactory.createDefaultModel();

		String datasetUri = getDatasetUri();
		Resource dataset = ResourceFactory.createResource(datasetUri);
		model.add(dataset, RDF.type, DCAT.Dataset);

		Literal keywordA = ResourceFactory.createPlainLiteral("keywordA");
		model.addLiteral(dataset, DCAT.keyword, keywordA);

		Literal keywordB = ResourceFactory.createPlainLiteral("keywordB");
		model.addLiteral(dataset, DCAT.keyword, keywordB);

		Resource theme = ResourceFactory.createResource("https://example.org/theme");
		Statement themeAndType = ResourceFactory.createStatement(theme, RDF.type, SKOS.Concept);
		model.add(dataset, DCAT.theme, theme);
		model.add(themeAndType);

		return model;
	}

}