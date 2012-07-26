package javatools.datatypes;

import java.util.Arrays;
import java.util.WeakHashMap;

import javatools.administrative.D;

/**
 * This class is part of the Java Tools (see
 * http://mpii.de/yago-naga/javatools). It is licensed under the Creative
 * Commons Attribution License (see http://creativecommons.org/licenses/by/3.0)
 * by the YAGO-NAGA team (see http://mpii.de/yago-naga).
 * 
 * This class represents strings with 1 byte per character. Thus, they use
 * roughly half as much space as ordinary strings -- but they also cannot store
 * all characters.
 * 
 * @author Fabian M. Suchanek
 * 
 */
public class ByteString implements CharSequence {
	/** Holds all strings */
	protected static WeakHashMap<ByteString, ByteString> values = new WeakHashMap<ByteString, ByteString>();

	/** Holds the string */
	protected byte[] data;

	/** Hash code */
	protected int hashCode;

	/** is interned */
	protected boolean isInterned = false;

	public ByteString(ByteString s, int start, int end) {
		data = Arrays.copyOfRange(s.data, start, end);
		hashCode = Arrays.hashCode(data);
	}

	public ByteString intern() {
		if (isInterned)
			return (this);
		ByteString canonic = values.get(this);
		if (canonic != null)
			return (canonic);
		isInterned = true;
		synchronized (values) {
			values.put(this, this);
		}
		return (this);
	}

	public ByteString(CharSequence s) {
		data = new byte[s.length()];
		for (int i = 0; i < s.length(); i++) {
			data[i] = (byte) (int) s.charAt(i);
		}
		hashCode = Arrays.hashCode(data);
	}

	@Override
	public char charAt(int arg0) {
		return (char) data[arg0];
	}

	@Override
	public int length() {
		return data.length;
	}

	@Override
	public CharSequence subSequence(int arg0, int arg1) {
		return new ByteString(this, arg0, arg1);
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ByteString && Arrays.equals(((ByteString) obj).data, data);
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < length(); i++) {
			b.append(charAt(i));
		}
		return b.toString();
	}

	public static void main(String[] args) throws Exception {
		D.p(new ByteString("Hello!"));
	}
}
