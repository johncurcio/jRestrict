package mast;

public class EventSymbol extends Symbol {
	public final String codigo;
	
	public EventSymbol(String name, String codigo) {
		super(name);
		this.codigo = codigo;
	}
}
