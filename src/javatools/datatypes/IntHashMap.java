package javatools.datatypes;

import java.lang.reflect.Array;

import javatools.administrative.D;

/**
 * 
 * This class is part of the Java Tools (see
 * http://mpii.de/yago-naga/javatools). It is licensed under the Creative
 * Commons Attribution License (see http://creativecommons.org/licenses/by/3.0)
 * by the YAGO-NAGA team (see http://mpii.de/yago-naga).
 *
 * This class implements a HashMap with integer values.
 * 
 * @author Fabian M. Suchanek
 * 
 * @param <K>
 */
public class IntHashMap<K> {

  /** Holds the keys */
  protected Object[] keys;
  
  /** Holds the values */
  protected int[] values;

	/** Holds size */
	protected int size;

	public IntHashMap() {
		keys = new Object[10];
		values=new int[10];
	}

	protected int index(Object key, int len) {
		return (Math.abs(key.hashCode()) % len);
	}

	protected int index(K key) {
		return (index(key, keys.length));
	}

	/** Retrieves a value */
	public int get(K key) {
		return (get(key, -1));
	}

	/** Retrieves a value */
	public int get(K key, int defaultValue) {
		int i = index(key);
		while (true) {
			if (keys[i] == null)
				return (defaultValue);
			if (keys[i].equals(key))
				return (values[i]);
			i++;
			if (i == keys.length)
				i = 0;
		}
	}

	/** Increases a value */
	public boolean increase(K key) {
		int i = index(key);
		while (true) {
			if (keys[i] == null)
				return (false);
			if (keys[i].equals(key)) {
				values[i]++;
				return (true);
			}
			i++;
			if (i == keys.length)
				i = 0;
		}
	}

	/** Returns keys. Can be used only once. */
	public Iterable<K> keys() {
		final Object[] e = keys;
		return (new PeekIterator<K>() {
			int pos = -1;

			@SuppressWarnings("unchecked")
      @Override
			protected K internalNext() throws Exception {
				pos++;
				for (; pos < keys.length; pos++) {
					if (e[pos] != null) {
						return ((K)e[pos]);
					}
				}
				return (null);
			}

		});
	}

	/** Adds a key */
	public boolean put(K key, int value) {
		if (put(keys, values, key, value)) {
			size++;
			if (size > keys.length * 3 / 4)
				rehash();
			return (true);
		}
		return (false);
	}

	/** Adds a key */
	protected boolean put(Object[] keys, int[] values, Object key, int value) {
		int i = index(key, keys.length);
		while (true) {
			if (keys[i] == null) {
				keys[i] = key;
				values[i]=value;
				return (true);
			}
			if (keys[i].equals(key)) {
				values[i] = value;
				return (false);
			}
			i++;
			if (i == keys.length)
				i = 0;
		}
	}

	/** Rehashes */
	protected void rehash() {
		Object[] newKeys = new Object[keys.length*2];
		int[] newValues=new int[keys.length*2];
		for (int i = 0; i < keys.length; i++) {
			if (keys[i] != null)
				put(newKeys, newValues, keys[i], values[i]);
		}
		keys = newKeys;
		values=newValues;
	}

	/** Test*/
	public static void main(String[] args) throws Exception {
		IntHashMap<String> m = new IntHashMap<String>();
		for (int i = 1; i < 3000; i *= 2)
			m.put("#" + i, i);
		m.put("#0", 17);
		for (String key : m.keys())
			D.p(key, m.get(key));
	}
}
