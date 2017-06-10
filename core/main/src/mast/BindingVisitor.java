package mast;

import java.util.List;

public class BindingVisitor implements Visitor<Void, Void> {
	public Scope<CommandSymbol> sRequires;
	public Scope<CommandSymbol> sProhibits;
	public Scope<CommandSymbol> sEncloses;
	public Scope<FileSymbol> sFiles;
	final List<String> errors;

	public BindingVisitor(List<String> errors) {
		this.errors = errors;
	}

	@Override
	public Void visit(CommandFiles fi, Void ctx) {
		return null;
	}
	
	@Override
	public Void visit(JavaArgs arg, Void ctx) {
		return null;
	}

	@Override
	public Void visit(Clause cl, Void ctx) {
		return null;
	}

	@Override
	public Void visit(Script script, Void ctx) {
		this.sEncloses = script.sEncloses;
		this.sFiles = script.sFiles;
		this.sProhibits = script.sProhibits;
		this.sRequires = script.sRequires;
		return null;
	}

	@Override
	public Void visit(CommandRequires cmd, Void ctx) {
		return null;
	}

	@Override
	public Void visit(CommandProhibits cmd, Void ctx) {
		return null;
	}

	@Override
	public Void visit(CommandEncloses cmd, Void ctx) {
		return null;
	}
}
