package mast;

import java.util.List;

public class BindingVisitor implements Visitor<Void, Void> {
	public Scope<CommandSymbol> cms;
	public Scope<CommandSymbol> sRequires;
	public Scope<CommandSymbol> sProhibits;
	public Scope<CommandSymbol> sEncloses;
	public Scope<FileSymbol> sFiles;
	final List<String> errors;
	
	String argument = "";
	ProhibitsVisitor prohibitsVisitor;
	RequiresVisitor requiresVisitor;
	EnclosesVisitor enclosesVisitor;
	
	public BindingVisitor(List<String> errors) {
		this.errors = errors;
		prohibitsVisitor = new ProhibitsVisitor(errors);
		requiresVisitor = new RequiresVisitor(errors);
		enclosesVisitor = new EnclosesVisitor(errors);
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
		for(CommandEncloses en: script.enclosement) {
			en.visit(this, ctx);
		}
		for(CommandRequires req: script.requirements) {
			req.visit(this, ctx);
		}
		for(CommandProhibits pb: script.prohibitions) {
			pb.visit(this, ctx);
		}
		/*for(CommandFiles fi: script.files) {
			fi.visit(this, ctx);
		}*/
		if (errors.size() == 0){
			script.visit(prohibitsVisitor, ctx);
			script.visit(requiresVisitor, ctx);
			script.visit(enclosesVisitor, ctx);
		}
		return null;
	}	

	@Override
	public Void visit(CommandRequires cmd, Void ctx) {
		cmd.clause.visit(this, ctx);		
		/*CommandSymbol cm = this.sEncloses.resolve(cmd.clause.type);
		if (cm != null){
			for (JavaArgs arg: cmd.clause.args){
				if (!cm.clause.args.toString().contains(arg.arg)){
					errors.add("[ERROR] You're requiring a clause not allowed to be used (" + cmd.clause.type + ": "+ arg.arg + ")." );
				}
			}
		}
		
		if (cmd.clause.type.toString().equals("type")){
			this.cms = this.sProhibits;
			this.visit((ClauseType)cmd.clause, ctx);
		}*/
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

	@Override
	public Void visit(ClauseRetType clause, Void ctx) {
		return null;
	}

	@Override
	public Void visit(ClauseType clause, Void ctx) {
		/*CommandSymbol vartype = this.cms.resolve("vartype");
		CommandSymbol returntype = this.cms.resolve("returntype");
		if (vartype != null){
			for (JavaArgs arg: vartype.clause.args){
				if (clause.args.toString().contains(arg.arg)){
					errors.add("[ERROR] You're prohibiting and requiring the usage of the same clause (" + clause.type + ": "+ arg.arg + "), (" + vartype.clause.type + ": "+ arg.arg + ")." );
				}
			}
		}
		if (returntype != null){
			for (JavaArgs arg: returntype.clause.args){
				if (clause.args.toString().contains(arg.arg)){
					errors.add("[ERROR] You're prohibiting and requiring the usage of the same clause (" + clause.type + ": "+ arg.arg + "), (" + returntype.clause.type + ": "+ arg.arg + ")." );
				}
			}
		}
		*/
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
		return null;
	}

	@Override
	public Void visit(ClauseOperator clause, Void ctx) {
		return null;
	}
}
