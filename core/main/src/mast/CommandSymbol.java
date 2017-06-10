package mast;

public class CommandSymbol extends Symbol {
	
	public final Clause clause;
	
	public CommandSymbol(String name, Clause c) {
		super(name);
		this.clause = c;
	}

}
