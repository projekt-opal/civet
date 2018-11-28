package org.dice_research.opal.civet.data;

import java.util.LinkedList;
import java.util.List;

/**
 * Abstract {@link DataObject}.
 * 
 * @author Adrian Wilke
 */
public abstract class AbstractDataObject<TYPE> implements DataObject<TYPE> {

	private String id;
	private List<TYPE> values;

	/**
	 * Sets id. Creates empty list of values.
	 * 
	 * @throws NullPointerException if the given ID is null.
	 */
	public AbstractDataObject(String id) throws NullPointerException {
		if (id == null) {
			throw new NullPointerException("Data object ID is null");
		} else {
			this.id = id;
			this.values = new LinkedList<TYPE>();
		}
	}

	/**
	 * Sets id and sets value.
	 * 
	 * @throws NullPointerException if the given ID or the given value is null.
	 */
	public AbstractDataObject(String id, TYPE value) throws NullPointerException {

		// Set ID. Create empty list of values.
		this(id);

		if (value == null) {
			throw new NullPointerException("Data object value is null");
		} else {
			this.values.add(value);
		}
	}

	/**
	 * Sets id and sets list of values.
	 * 
	 * @throws NullPointerException if the given ID or the given list of values is
	 *                              null.
	 */
	public AbstractDataObject(String id, List<TYPE> values) throws NullPointerException {

		// Set ID. Create empty list of values.
		this(id);

		// Replace list of values by given list.
		if (values == null) {
			throw new NullPointerException("Data object values is null");
		} else {
			this.values = values;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEmpty() {
		return values.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<TYPE> getValues() {
		return values;
	}

	/**
	 * {@inheritDoc}
	 */
	public DataObject<TYPE> setValue(TYPE value) throws NullPointerException {
		if (value == null) {
			throw new NullPointerException("Data object value is null");
		} else {
			this.values = new LinkedList<TYPE>();
			this.values.add(value);
			return this;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public DataObject<TYPE> setValues(List<TYPE> values) throws NullPointerException {
		if (values == null) {
			throw new NullPointerException("Data object values is null");
		} else {
			this.values = values;
			return this;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public DataObject<TYPE> addValue(TYPE value) throws NullPointerException {
		if (value == null) {
			throw new NullPointerException("Data object value is null");
		} else {
			this.values.add(value);
			return this;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public DataObject<TYPE> addValues(List<TYPE> values) throws NullPointerException {
		if (values == null) {
			throw new NullPointerException("Data object values is null");
		} else {
			this.values.addAll(values);
			return this;
		}
	}

	@Override
	public String toString() {
		return getId() + " [" + values.size() + "]";
	}

}