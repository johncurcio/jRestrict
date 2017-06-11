package mast;

import java.util.List;

public class BindingVisitor implements Visitor<Void, Void> {
	public Scope<CommandSymbol> sRequires;
	public Scope<CommandSymbol> sProhibits;
	public Scope<CommandSymbol> sEncloses;
	public Scope<FileSymbol> sFiles;
	final List<String> errors;
	
	String argument = "";
	ProhibitsVisitor prohibitsVisitor;

	public BindingVisitor(List<String> errors) {
		this.errors = errors;
		prohibitsVisitor = new ProhibitsVisitor(errors);
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
		script.visit(prohibitsVisitor, ctx);
		
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

	@Override
	public Void visit(ClauseRetType clause, Void ctx) {
		return null;
	}

	@Override
	public Void visit(ClauseType clause, Void ctx) {
		return null;
	}

	@Override
	public Void visit(ClauseVarType clause, Void ctx) {
		return null;
	}

	@Override
	public Void visit(ClauseLoop clause, Void ctx) {
		return null;
	}

	@Override
	public Void visit(ClauseBranch clause, Void ctx) {
		return null;
	}

	@Override
	public Void visit(ClauseImport clause, Void ctx) {
		return null;
	}

	@Override
	public Void visit(ClauseModifier clause, Void ctx) {
		return null;
	}

	@Override
	public Void visit(ClauseOperator clause, Void ctx) {
		return null;
	}
}
