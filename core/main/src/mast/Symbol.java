package mast;

public abstract class Symbol {
	public final String name;
	
	public Symbol(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
}
