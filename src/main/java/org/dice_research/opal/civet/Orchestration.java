package org.dice_research.opal.civet;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO: implement
 */
public class Orchestration {

	private List<URI> uris = new LinkedList<URI>();

	Orchestration(URI uri) {
		this.uris.add(uri);
	}

	Orchestration(List<URI> uris) {
		this.uris = uris;
	}

}