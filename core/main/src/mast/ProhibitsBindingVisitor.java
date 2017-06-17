package mast;

import java.util.List;

public class ProhibitsBindingVisitor implements Visitor<Void, Void> {
	public Scope<CommandSymbol> sRequires;
	public Scope<CommandSymbol> sProhibits;
	public Scope<CommandSymbol> sEncloses;
	final List<String> errors;
		
	public ProhibitsBindingVisitor(List<String> errors) {
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
		this.sEncloses  = script.sEncloses;
		this.sProhibits = script.sProhibits;
		this.sRequires  = script.sRequires;
		for(CommandProhibits pb: script.prohibitions) {
			pb.visit(this, ctx);
		}
		return null;
	}	

	@Override
	public Void visit(CommandRequires cmd, Void ctx) {
		return null;
	}
	
	@Override
	public Void visit(CommandProhibits cmd, Void ctx) {
		cmd.clause.visit(this, ctx);
		return null;
	}

	@Override
	public Void visit(CommandEncloses cmd, Void ctx) {
		return null;
	}
	
	private void clauseVisit(Clause clause, CommandSymbol encloses){
		boolean errorFound;
		String error = "";
		if (encloses != null){
			for (JavaArgs arg: clause.args){
				errorFound = true;
				error = "";
				for (JavaArgs enc: encloses.clause.args){
					error = "[ERROR] Prohibiting a " + clause.type + " not allowed to be used by encloses:\n"
					      + "        prohibits has [" + arg.arg + "] but encloses only allows "+ encloses.clause.args.toString()+".";						
					if (enc.equals(arg)){
						errorFound = false;
						break;
					}
				}
				if (errorFound){
					errors.add(error);
				}
			}
		}
	}

	@Override
	public Void visit(ClauseRetType clause, Void ctx) {
		this.clauseVisit(clause, this.sEncloses.resolve(clause.type));
		this.clauseVisit(clause, this.sEncloses.resolve("type"));
		return null;
	}

	@Override
	public Void visit(ClauseType clause, Void ctx) {
		this.clauseVisit(clause, this.sEncloses.resolve(clause.type));
		this.clauseVisit(clause, this.sEncloses.resolve("vartype"));
		this.clauseVisit(clause, this.sEncloses.resolve("rettype"));
		return null;
	}

	@Override
	public Void visit(ClauseVarType clause, Void ctx) {
		this.clauseVisit(clause, this.sEncloses.resolve(clause.type));
		this.clauseVisit(clause, this.sEncloses.resolve("type"));
		return null;
	}

	@Override
	public Void visit(ClauseLoop clause, Void ctx) {
		this.clauseVisit(clause, this.sEncloses.resolve(clause.type));
		return null;
	}

	@Override
	public Void visit(ClauseBranch clause, Void ctx) {
		this.clauseVisit(clause, this.sEncloses.resolve(clause.type));
		return null;
	}
	
	@Override
	public Void visit(ClauseImport clause, Void ctx) {
		CommandSymbol enclosesImport  = this.sEncloses.resolve(clause.type);
		boolean errorFound;
		String error = "";
		if (enclosesImport != null){
			for (JavaArgs importarg: clause.args){
				errorFound = true;
				error = "";
				for (JavaArgs encimport: enclosesImport.clause.args){
					error = "[ERROR] Prohibiting an " + clause.type + " not allowed to be used by encloses:\n"
					      + "        prohibits has [" + importarg.arg + "] but encloses only allows "+ enclosesImport.clause.args.toString()+".";						
					if (encimport.equals(importarg) || importarg.arg.contains(encimport.arg)){
						errorFound = false;
						break;
					}
				}
				if (errorFound){
					errors.add(error);
				}
			}
		}
		return null;
	}

	@Override
	public Void visit(ClauseModifier clause, Void ctx) {
		this.clauseVisit(clause, this.sEncloses.resolve(clause.type));
		return null;
	}

	@Override
	public Void visit(ClauseOperator clause, Void ctx) {
		this.clauseVisit(clause, this.sEncloses.resolve(clause.type));
		return null;
	}
}
