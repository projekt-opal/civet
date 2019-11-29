# OPAL Civet

OPAL metadata quality component.


## Usage with Apache Maven

Add the following lines to your `pom.xml` configuration file:

	<dependencies>
		<dependency>
			<groupId>org.dice-research.opal</groupId>
			<artifactId>civet</artifactId>
			<version>[2,3)</version>
		</dependency>
	</dependencies>
	
	<repositories>
		<repository>
			<id>maven.aksw.internal</id>
			<name>AKSW Repository</name>
			<url>http://maven.aksw.org/archiva/repository/internal</url>
		</repository>
		<repository>
			<id>maven.aksw.snapshots</id>
			<name>AKSW Snapshot Repository</name>
			<url>http://maven.aksw.org/archiva/repository/snapshots</url>
		</repository>
	</repositories>

Available versions are listed at [maven.aksw.org](https://maven.aksw.org/archiva/#advancedsearch~internal/org.dice-research.opal~civet~~~~~30).


## Example

```Java
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

		// If existing measurements should be removed
		// (optional method call, default: true)
		civet.setRemoveMeasurements(true);

		// If it should be logged, if a measurement could not be computed
		// (optional method call, default: true)
		civet.setLogNotComputed(true);

		// Update model
		model = civet.process(model, datasetUri);

		// Write updated model into TURTLE file
		FileHandler.export(turtleOutputFile, model);
	}
}

```


## Note

Civet version 1 can be found at [branch civet-version-1](https://github.com/projekt-opal/civet/tree/civet-version-1).


## Credits

[Data Science Group (DICE)](https://dice-research.org/) at [Paderborn University](https://www.uni-paderborn.de/)

This work has been supported by the German Federal Ministry of Transport and Digital Infrastructure (BMVI) in the project [Open Data Portal Germany (OPAL)](http://projekt-opal.de/) (funding code 19F2028A).
