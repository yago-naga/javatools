package javatools.datatypes;

import javatools.administrative.D;

/**
 * 
 * This class is part of the Java Tools (see
 * http://mpii.de/yago-naga/javatools). It is licensed under the Creative
 * Commons Attribution License (see http://creativecommons.org/licenses/by/3.0)
 * by the YAGO-NAGA team (see http://mpii.de/yago-naga).
 * 
 * This class implements a FrequencyVector with a DoubleHashMap
 * 
 * @author Fabian M. Suchanek
 * 
 * @param <K>
 */
public class FastFrequencyVector<K> extends DoubleHashMap<K> {

	/** Holds the maximum of this vector */
	protected double max = Double.NEGATIVE_INFINITY;

	/** Holds the sum of this vector */
	protected double sum = 0;

	public double sum() {
		return(sum);
	}
	public double max() {
		return(max);
	}
	
	@Override
	public boolean add(K key, double delta) {
		int pos = find(key);
		if (keys[pos] == null) {
			keys[pos] = key;
			values[pos] = delta;
			sum += delta;
			if (delta > max)
				max = delta;
			size++;
			if (size > keys.length * 3 / 4)
				rehash();
			return (true);
		}
		values[pos] += delta;
		sum += delta;
		if (values[pos] > max)
			max = values[pos];
		else if (max == values[pos] - delta)
			max = findMax();
		return (false);
	}

	@Override
	public boolean put(K key, double value) {
		int i = index(key);
		while (true) {
			if (keys[i] == null) {
				keys[i] = key;
				values[i] = value;
				sum += value;
				if (value > max)
					max = value;
				size++;
				if (size > keys.length * 3 / 4)
					rehash();
				return (true);
			}
			if (keys[i].equals(key)) {
				sum -= values[i];
				sum += value;
				boolean wasMax = values[i] == max;
				values[i] = value;
				if (value >= max) {
					max = value;
				} else if (wasMax) {
					max = findMax();
				}
				return (false);
			}
			i++;
			if (i == keys.length)
				i = 0;
		}
	}

	protected double findMax() {
		double max = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < keys.length; i++) {
			if (keys[i] != null && values[i] > max)
				max = values[i];
		}
		return (max);
	}

	/** Normalizes this vector by dividing by the max */
	public void normalizeMax() {
		if (size == 0)
			return;
		for (int i = 0; i < keys.length; i++) {
			if (keys[i] != null) {
				values[i] /= max;
			}
		}
		sum = sum / max;
		max = size > 0 ? 1.0 : Double.NEGATIVE_INFINITY;
	}

	/** Returns the value divided by the max*/
	public double getMaxNormalized(K key) {
		return(get(key)/max);
	}
	
	/** Computes the fuzzy precision of this vector wrt the other vector */
	public double fuzzyPrecisionTo(FastFrequencyVector<K> other) {
		return (other.fuzzyRecallTo(this));
	}

	/** Computes the fuzzy recall of this vector wrt the other vector */
	public double fuzzyRecallTo(FastFrequencyVector<K> other) {
		if (other.sum() == 0)
			return (1.0);
		double fuzzyRecall = 0;
		for (K trueTerm : other.keys()) {
			double trueValue = other.getMaxNormalized(trueTerm);
			double guessedValue = this.getMaxNormalized(trueTerm);
			if (trueValue > guessedValue) {
				fuzzyRecall += trueValue - guessedValue;
			}
		}
		fuzzyRecall = 1 - fuzzyRecall / other.sum() * other.max();
		if (fuzzyRecall < 0)
			fuzzyRecall = 0; // Small rounding errors may occur
		return (fuzzyRecall);
	}

	public static void main(String[] args) throws Exception {
		FastFrequencyVector<String> person=new FastFrequencyVector<String>();
		person.put("birthDate", 100);
		person.put("birthPlace", 80);
		person.put("deathPlace", 50);
		person.put("wonPrize", 10);
		FastFrequencyVector<String> livingPerson=new FastFrequencyVector<String>();
		livingPerson.put("birthDate", 20);
		livingPerson.put("birthPlace", 16);
		// no death place
		FastFrequencyVector<String> scientist=new FastFrequencyVector<String>();
		scientist.put("birthDate", 10);
		scientist.put("birthPlace", 10);
		scientist.put("deathPlace", 8);
		scientist.put("wonPrize", 8);
		D.p("LivingPerson covers what person has:", livingPerson.fuzzyRecallTo(person));
		D.p("LivingPerson is covered by person:", livingPerson.fuzzyPrecisionTo(person));
		D.p("Scientist covers what person has:", scientist.fuzzyRecallTo(person));
		D.p("Scientist is covered by person:", scientist.fuzzyPrecisionTo(person));
	}
}
