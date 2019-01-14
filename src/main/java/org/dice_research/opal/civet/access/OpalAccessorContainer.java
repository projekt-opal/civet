package org.dice_research.opal.civet.access;

import java.util.HashMap;
import java.util.Map;

import org.dice_research.opal.civet.data.DataContainer;

/**
 * Simple container for return values in {@link OpalAccessor} requests.
 * 
 * @author Adrian Wilke
 */
public class OpalAccessorContainer {

	public Map<String, DataContainer> dataContainers = new HashMap<>();
	public int refreshIndex;

}