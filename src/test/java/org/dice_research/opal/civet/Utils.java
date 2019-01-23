package org.dice_research.opal.civet;

import java.net.URL;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public abstract class Utils {

	public static final String MODEL_ZUGBILDUNGSPLAN = "Zugbildungsplan.ttl";
	public static final String LANG_TURTLE = "TURTLE";

	private static ClassLoader classLoader;

	public static Model readModel(String resourceName, String lang) {
		Model model = ModelFactory.createDefaultModel();
		model.read(getResourceUrl(resourceName).toString(), lang);
		return model;
	}

	public static URL getResourceUrl(String name) {
		if (classLoader == null) {
			classLoader = Utils.class.getClassLoader();
		}
		return Utils.class.getClassLoader().getResource(name);
	}

}