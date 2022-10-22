package info.itsthesky.util;

public class Pair<K, V> {

	private final K key;
	private final V value;

	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	@Override
	public String toString() {
		return getKey() + ", " + getValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (!(obj instanceof final Pair<?, ?> pair)) return false;
		return pair.getKey().equals(getKey()) && pair.getValue().equals(getValue());
	}

	@Override
	public int hashCode() {
		return getKey().hashCode() + getValue().hashCode();
	}
}
