package mast;

import java.util.List;


public class Script {
	public final List<CommandFiles> files;
	public final List<CommandRequires> requirements;
	public final List<CommandProhibits> prohibitions;
	public final List<CommandEncloses> enclosement;
	
	//@TODO: how the f do these work?
	public Scope<Symbol> symbol_requires;
	public Scope<Symbol> symbol_prohibits;
	public Scope<Symbol> symbol_encloses;
	public Scope<Symbol> symbol_files;
	
	public Script(List<CommandFiles> files, List<CommandRequires> requirements, 
			List<CommandProhibits> prohibitions, List<CommandEncloses> enclosement) {
		this.files = files;
		this.requirements = requirements;
		this.prohibitions = prohibitions;
		this.enclosement = enclosement;
	}
	
	public String toString() {
		return visit(new StringVisitor(), null);
	}

	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}