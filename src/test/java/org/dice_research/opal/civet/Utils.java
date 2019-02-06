package org.dice_research.opal.civet;

import java.net.URL;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public abstract class Utils {

	// mcloud
	public static final String MODEL_ZUGBILDUNGSPLAN = "Zugbildungsplan.ttl";

	// mcloud, 3 distributions csv csv xlsx
	public static final String MODEL_STATIONSDATEN = "Stationsdaten.ttl";

	// mcloud, 4 distributions zip
	public static final String MODEL_STRECKEN = "Strecken.ttl";

	// govdata, no description
	public static final String MODEL_RHABABER = "Govdata-Rhabarber.ttl";

	// govdata, 4 distributions
	public static final String MODEL_ALLERMOEHE = "Govdata-Allermoehe.ttl";

	// europeandataportal, 2 distributions
	public static final String MODEL_ICELAND = "Europeandataportal-Iceland.ttl";

	// europeandataportal, titles in various languages
	public static final String MODEL_DURCHSCHNITTSALTER = "Europeandataportal-Durchschnittsalter.ttl";

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