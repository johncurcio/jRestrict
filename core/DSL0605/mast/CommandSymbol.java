package mast;

public class CommandSymbol extends Symbol {
	public final String codigo;
	
	public CommandSymbol(String name, String codigo) {
		super(name);
		this.codigo = codigo;
	}
}
