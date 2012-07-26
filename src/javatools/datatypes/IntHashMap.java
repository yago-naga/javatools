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

	/** Entry of a key and an integer value*/
	public static class Entry<K> {
		K key;
		int value;

		public Entry(K key, int value) {
			this.key = key;
			this.value = value;
		}
	}

	/** Holds the entries */
	protected Entry<K>[] entries;

	/** Holds size */
	protected int size;

	@SuppressWarnings("unchecked")
	public IntHashMap() {
		entries = (Entry<K>[]) Array.newInstance(Entry.class, 10);
	}

	protected int index(K key, int len) {
		return (Math.abs(key.hashCode()) % len);
	}

	protected int index(K key) {
		return (index(key, entries.length));
	}

	/** Retrieves a value */
	public int get(K key) {
		return (get(key, -1));
	}

	/** Retrieves a value */
	public int get(K key, int defaultValue) {
		int i = index(key);
		while (true) {
			if (entries[i] == null)
				return (defaultValue);
			if (entries[i].key.equals(key))
				return (entries[i].value);
			i++;
			if (i == entries.length)
				i = 0;
		}
	}

	/** Increases a value */
	public boolean increase(K key) {
		int i = index(key);
		while (true) {
			if (entries[i] == null)
				return (false);
			if (entries[i].key.equals(key)) {
				entries[i].value++;
				return (true);
			}
			i++;
			if (i == entries.length)
				i = 0;
		}
	}

	/** Returns keys. Can be used only once. */
	public Iterable<K> keys() {
		final Entry<K>[] e = entries;
		return (new PeekIterator<K>() {
			int pos = -1;

			@Override
			protected K internalNext() throws Exception {
				pos++;
				for (; pos < entries.length; pos++) {
					if (e[pos] != null) {
						return (e[pos].key);
					}
				}
				return (null);
			}

		});
	}

	/** Adds a key */
	public boolean put(K key, int value) {
		if (put(entries, new Entry<K>(key, value))) {
			size++;
			if (size > entries.length * 3 / 4)
				rehash();
			return (true);
		}
		return (false);
	}

	/** Adds a key */
	protected boolean put(Entry<K>[] entries, Entry<K> entry) {
		int i = index(entry.key, entries.length);
		while (true) {
			if (entries[i] == null) {
				entries[i] = entry;
				return (true);
			}
			if (entries[i].key.equals(entry.key)) {
				entries[i] = entry;
				return (false);
			}
			i++;
			if (i == entries.length)
				i = 0;
		}
	}

	/** Rehashes */
	@SuppressWarnings("unchecked")
	protected void rehash() {
		Entry<K>[] newEntries = (Entry<K>[]) Array.newInstance(Entry.class, entries.length * 2);
		for (int i = 0; i < entries.length; i++) {
			if (entries[i] != null)
				put(newEntries, entries[i]);
		}
		entries = newEntries;
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
