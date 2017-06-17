package mast;

import java.util.List;

public class RequiresBindingVisitor implements Visitor<Void, Void> {
	public Scope<CommandSymbol> sRequires;
	public Scope<CommandSymbol> sProhibits;
	public Scope<CommandSymbol> sEncloses;
	final List<String> errors;
		
	public RequiresBindingVisitor(List<String> errors) {
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
		for(CommandRequires req: script.requirements) {
			req.visit(this, ctx);
		}
		return null;
	}	

	@Override
	public Void visit(CommandRequires cmd, Void ctx) {
		cmd.clause.visit(this, ctx);
		return null;
	}
	
	@Override
	public Void visit(CommandProhibits cmd, Void ctx) {
		/*CommandSymbol cm = this.sRequires.resolve(cmd.clause.type);
		if (cm != null){
			for (JavaArgs arg: cm.clause.args){
				if (cmd.clause.args.toString().contains(arg.arg)){
					errors.add("[ERROR] You're prohibiting and requiring the usage of the same clause (" + cmd.clause.type + ": "+ arg.arg + ")." );
				}
			}
		}
		if (cmd.clause.type.toString().equals("type")){
			this.cms = this.sRequires;
			this.visit((ClauseType)cmd.clause, ctx);
		}*/
		return null;
	}

	@Override
	public Void visit(CommandEncloses cmd, Void ctx) {
		/*CommandSymbol cm = this.sProhibits.resolve(cmd.clause.type);
		if (cm != null){
			for (JavaArgs arg: cm.clause.args){
				if (!cmd.clause.args.toString().contains(arg.arg)){
					errors.add("[ERROR] You're prohibiting a clause that's not allowed to be used (" + cmd.clause.type + ": "+ arg.arg + ")." );
				}
			}
		}*/
		return null;
	}
	
	private void clauseVisit(Clause clause, CommandSymbol encloses, CommandSymbol prohibits){
		boolean errorFound;
		String error = "";
		if (encloses != null){
			for (JavaArgs arg: clause.args){
				errorFound = true;
				error = "";
				for (JavaArgs enc: encloses.clause.args){
					error = "[ERROR] Requiring a " + clause.type + " not allowed to be used by encloses:\n"
					      + "        requires has [" + arg.arg + "] but encloses only allows "+ encloses.clause.args.toString()+".";						
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
		if (prohibits != null){
			for (JavaArgs arg: clause.args){
				errorFound = false;
				error = "";
				for (JavaArgs pb: prohibits.clause.args){
					error = "[ERROR] Requiring a " + clause.type + " that has been prohibited from use:\n"
					      + "        requires [" + arg.arg + "] conflicts with prohibits ["+ pb.arg +"].";						
					if (pb.equals(arg)){
						errorFound = true;
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
		this.clauseVisit(clause, this.sEncloses.resolve(clause.type), this.sProhibits.resolve(clause.type));
		this.clauseVisit(clause, this.sEncloses.resolve("type"), this.sProhibits.resolve("type"));
		return null;
	}

	@Override
	public Void visit(ClauseType clause, Void ctx) {
		this.clauseVisit(clause, this.sEncloses.resolve(clause.type), this.sProhibits.resolve(clause.type));
		this.clauseVisit(clause, this.sEncloses.resolve("vartype"), this.sProhibits.resolve("vartype"));
		this.clauseVisit(clause, this.sEncloses.resolve("rettype"), this.sProhibits.resolve("rettype"));
		return null;
	}

	@Override
	public Void visit(ClauseVarType clause, Void ctx) {
		this.clauseVisit(clause, this.sEncloses.resolve(clause.type), this.sProhibits.resolve(clause.type));
		this.clauseVisit(clause, this.sEncloses.resolve("type"), this.sProhibits.resolve("type"));
		return null;
	}

	@Override
	public Void visit(ClauseLoop clause, Void ctx) {
		this.clauseVisit(clause, this.sEncloses.resolve(clause.type), this.sProhibits.resolve(clause.type));
		return null;
	}

	@Override
	public Void visit(ClauseBranch clause, Void ctx) {
		this.clauseVisit(clause, this.sEncloses.resolve(clause.type), this.sProhibits.resolve(clause.type));
		return null;
	}
	
	@Override
	public Void visit(ClauseImport clause, Void ctx) {
		CommandSymbol enclosesImport  = this.sEncloses.resolve(clause.type);
		CommandSymbol prohibitsImport = this.sProhibits.resolve(clause.type);
		boolean errorFound;
		String error = "";
		if (enclosesImport != null){
			for (JavaArgs importarg: clause.args){
				errorFound = true;
				error = "";
				for (JavaArgs encimport: enclosesImport.clause.args){
					error = "[ERROR] Requiring an " + clause.type + " not allowed to be used by encloses:\n"
					      + "        requires has [" + importarg.arg + "] but encloses only allows "+ enclosesImport.clause.args.toString()+".";						
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
		if (prohibitsImport != null){
			for (JavaArgs importarg: clause.args){
				errorFound = false;
				error = "";
				for (JavaArgs pbimport: prohibitsImport.clause.args){
					error = "[ERROR] Requiring an " + clause.type + " that has been prohibited from use:\n"
					      + "        requires [" + importarg.arg + "] conflicts with prohibits ["+ pbimport.arg +"].";						
					if (pbimport.equals(importarg) || importarg.arg.contains(pbimport.arg) || pbimport.arg.contains(importarg.arg)){
						errorFound = true;
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
		this.clauseVisit(clause, this.sEncloses.resolve(clause.type), this.sProhibits.resolve(clause.type));
		return null;
	}

	@Override
	public Void visit(ClauseOperator clause, Void ctx) {
		this.clauseVisit(clause, this.sEncloses.resolve(clause.type), this.sProhibits.resolve(clause.type));
		return null;
	}
}
