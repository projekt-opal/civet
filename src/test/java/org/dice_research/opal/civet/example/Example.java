package org.dice_research.opal.civet.example;

import java.io.File;

import org.apache.jena.rdf.model.Model;
import org.dice_research.opal.civet.Civet;
import org.dice_research.opal.common.utilities.FileHandler;

public class Example {

	/**
	 * Computes quality metric scores (measurements).
	 * 
	 * @param turtleInputFile  A TURTLE file to read
	 * @param turtleOutputFile A TURTLE file to write results
	 * @param datasetUri       A URI of a dcat:Dataset inside the TURTLE data
	 * 
	 * @see https://www.w3.org/TR/turtle/
	 * @see https://www.w3.org/TR/vocab-dcat/
	 */
	public void evaluateMetadata(File turtleInputFile, File turtleOutputFile, String datasetUri) throws Exception {

		// Load TURTLE file into model
		Model model = FileHandler.importModel(turtleInputFile);

		Civet civet = new Civet();

		// If long running metrics should be included.
		// (optional method call, default: false)
		civet.setIncludeLongRunning(true);

		// If it should be logged, if a measurement could not be computed
		// (optional method call, default: true)
		civet.setLogIfNotComputed(false);

		// If existing measurements should be removed
		// (optional method call, default: true)
		civet.setRemoveMeasurements(true);

		// Update model
		civet.processModel(model, datasetUri);

		// Write updated model into TURTLE file
		FileHandler.export(turtleOutputFile, model);
	}
}