package mast;

import java.util.HashMap;
import java.util.Map;

public class Scope<T extends Symbol> {
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
}
