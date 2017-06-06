package mast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Scope<T extends Symbol> implements Iterable<T> {
	public final String name;
	public final Map<String, T> tab = new HashMap<>();
	
	public Scope(String name) {
		this.name = name;
	}
	
	public void define(T s) {
		tab.put(s.name, s);
	}
	
	public T resolve(String name) {
		return tab.get(name);
	}

	@Override
	public Iterator<T> iterator() {
		return tab.values().iterator();
	}
	
	
}
