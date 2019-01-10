package org.dice_research.opal.civet.data;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dice_research.opal.civet.exceptions.UnknownIdRuntimeException;
import org.dice_research.opal.civet.metrics.Metric;
import org.dice_research.opal.civet.metrics.Metrics;

/**
 * Data container for {@link DataObject}s.
 * 
 * Data-object IDs and types are defined in {@link DataObjects}.
 *
 * @author Adrian Wilke
 */
public class DataContainer {

	// Data-object ID to object
	private Map<String, DataObject<?>> dataObjects = new HashMap<String, DataObject<?>>();

	// Metrix-ID to metric-result
	private Map<String, Float> metricResults = new HashMap<String, Float>();

	/**
	 * Creates new data-container with data-objects of source data-container.
	 * Data-objects will have same types and IDs, but no values.
	 * 
	 * @param dataContainer Source data-container
	 * 
	 * @throws IOException          If type of source data-object is unknown.
	 * @throws NullPointerException if the one of given IDs is null.
	 */
	public static DataContainer create(DataContainer dataContainer) throws NullPointerException, IOException {
		DataContainer newDataContainer = new DataContainer();
		for (DataObject<?> dataObject : dataContainer.getDataObjects()) {
			newDataContainer.putDataObject(AbstractDataObject.create(dataObject));
		}
		return newDataContainer;
	}

	/**
	 * Gets data object.
	 * 
	 * @param id as specified in class constants of {@link DataObjects}
	 * 
	 * @throws NullPointerException      if the given ID is null.
	 * @throws UnknownIdRuntimeException if the given ID has not been defined.
	 */
	public DataObject<?> getDataObject(String id) throws NullPointerException, UnknownIdRuntimeException {
		if (id == null) {
			throw new NullPointerException("Data object ID is null");
		} else if (dataObjects.containsKey(id)) {
			return dataObjects.get(id);
		} else {
			throw new UnknownIdRuntimeException("Unknown data object ID: " + id);
		}
	}

	/**
	 * Gets casted data object.
	 * 
	 * @throws NullPointerException      if the given ID is null.
	 * @throws UnknownIdRuntimeException if the given ID has not been defined.
	 * @throws ClassCastException        if type of data object does not fit.
	 */
	public IntegerDataObject getIntegerDataObject(String id)
			throws NullPointerException, UnknownIdRuntimeException, ClassCastException {
		return IntegerDataObject.cast(getDataObject(id));
	}

	/**
	 * Gets casted data object.
	 * 
	 * @throws NullPointerException      if the given ID is null.
	 * @throws UnknownIdRuntimeException if the given ID has not been defined.
	 * @throws ClassCastException        if type of data object does not fit.
	 */
	public StringDataObject getStringDataObject(String id)
			throws NullPointerException, UnknownIdRuntimeException, ClassCastException {
		return StringDataObject.cast(getDataObject(id));
	}

	/**
	 * Puts data object in container.
	 * 
	 * @param dataObject implementation of {@link AbstractDataObject}
	 * 
	 * @throws NullPointerException if the given data object is null.
	 */
	public DataContainer putDataObject(DataObject<?> dataObject) throws NullPointerException {
		if (dataObject == null) {
			throw new NullPointerException("Data object is null");
		} else {
			dataObjects.put(dataObject.getId(), dataObject);
			return this;
		}
	}

	/**
	 * Calculates metric results and stores them in this object.
	 */
	public DataContainer calculateMetrics(Collection<String> metricIds) {
		Map<String, Metric> availableMetrics = Metrics.getMetrics();
		for (String metricId : metricIds) {
			metricResults.put(metricId, availableMetrics.get(metricId).getScore(this));
		}
		return this;
	}

	/**
	 * Gets metric results calculated by {@link #calculateMetrics(Collection)}.
	 */
	public Map<String, Float> getMetricResults() {
		return metricResults;
	}

	/**
	 * Gets contained data object IDs.
	 */
	public Set<String> getIds() {
		return dataObjects.keySet();
	}

	/**
	 * Gets contained data objects.
	 */
	public Collection<DataObject<?>> getDataObjects() {
		return dataObjects.values();
	}

	/**
	 * Gets all contained data as string representation.
	 */
	public String toExtendedString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (Entry<String, DataObject<?>> dataObject : dataObjects.entrySet()) {
			stringBuilder.append(dataObject.getKey());
			stringBuilder.append(System.lineSeparator());
			for (Object value : dataObject.getValue().getValues()) {
				stringBuilder.append(" ");
				stringBuilder.append(value.toString());
				stringBuilder.append(System.lineSeparator());
			}
		}
		return stringBuilder.toString();
	}
}