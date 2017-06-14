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
		CommandSymbol cm = this.sEncloses.resolve(cmd.clause.type);
		if (cm != null){
			for (JavaArgs arg: cmd.clause.args){
				if (!cm.clause.args.toString().contains(arg.arg)){
					errors.add("[ERROR] You're requiring a clause not allowed to be used (" + cmd.clause.type + ": "+ arg.arg + "). Are you sure you meant to do this?" );
				}
			}
		}
		return null;
	}
	
	@Override
	public Void visit(CommandProhibits cmd, Void ctx) {
		CommandSymbol cm = this.sRequires.resolve(cmd.clause.type);
		if (cm != null){
			for (JavaArgs arg: cm.clause.args){
				if (cmd.clause.args.toString().contains(arg.arg)){
					errors.add("[ERROR] You're prohibiting and requiring the usage of the same clause (" + cmd.clause.type + ": "+ arg.arg + "). Are you sure you meant to do this?" );
				}
			}
		}
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
