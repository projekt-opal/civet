package org.dice_research.opal.civet;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * Provides Jena models for tests.
 * 
 * Use {@link #getTurtleResourceNames()} to get available TURTLE resources.
 * 
 * Use {@link #getModel(String)} to get a Jena model for a TURTLE resource.
 * 
 * @author Adrian Wilke
 */
public class TestData {

	private static final String LANG_TURTLE = "TURTLE";

	/**
	 * Provides a Jena model for the given resource.
	 */
	public Model getModel(String resourceName) {
		Model model = ModelFactory.createDefaultModel();
		model.read(getResourceUrl(resourceName).toString(), LANG_TURTLE);
		return model;
	}

	/**
	 * Provides a list of available TURTLE resources.
	 */
	public List<String> getTurtleResourceNames() {

		// Get resources directory
		File resourcesDirectory = null;
		try {
			resourcesDirectory = new File(getResourceUrl(".").toURI());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

		// Get turtle files
		File[] files = resourcesDirectory.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(".ttl")) {
					return true;
				} else {
					return false;
				}
			}
		});

		// Extract filenames
		List<String> filenames = new LinkedList<>();
		for (File file : files) {
			filenames.add(file.getName());
		}

		return filenames;
	}

	private URL getResourceUrl(String resourceName) {
		return TestData.class.getClassLoader().getResource(resourceName);
	}
}