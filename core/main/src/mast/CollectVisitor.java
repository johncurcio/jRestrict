package mast;

import java.util.List;

public class CollectVisitor implements Visitor<Void, Void> {

	public final Scope<CommandSymbol> sRequires = new Scope<>("requires");
	public final Scope<CommandSymbol> sProhibits = new Scope<>("prohibits");
	public final Scope<CommandSymbol> sEncloses = new Scope<>("encloses");
	public final Scope<FileSymbol> sFiles = new Scope<>("files");
	
	final List<String> errors;
	
	public CollectVisitor(List<String> errors) {
		this.errors = errors;
	}
	
	@Override
	public Void visit(CommandFiles fi, Void ctx) {
		if (sFiles.resolve(fi.filename) == null){
			sFiles.define(new FileSymbol(fi.filename));
		}else{
			errors.add("arquivo <" + fi.filename + "> repetido na posicao " + fi.pos + " para o comando files");			
		}
		return null;
	}
	
	@Override
	public Void visit(JavaArgs req, Void ctx) {
		return null;
	}

	@Override
	public Void visit(Clause clause, Void ctx) {
		return null;
	}
	
	@Override
	public Void visit(Script script, Void ctx) {
		script.sEncloses = this.sEncloses;
		script.sProhibits = this.sProhibits;
		script.sRequires = this.sRequires;
		script.sFiles = this.sFiles;
		for(CommandEncloses en: script.enclosement) {
			en.visit(this, ctx);
		}
		for(CommandRequires req: script.requirements) {
			req.visit(this, ctx);
		}
		for(CommandProhibits pb: script.prohibitions) {
			pb.visit(this, ctx);
		}
		for(CommandFiles fi: script.files) {
			fi.visit(this, ctx);
		}
		return null;
	}	
	
	@Override
	public Void visit(CommandRequires cmd, Void ctx) {
		if (sRequires.resolve(cmd.clause.type) == null){
			sRequires.define(new CommandSymbol(cmd.clause.type, cmd.clause));
		}else{
			errors.add("clausula <" + cmd.clause.type + "> redeclarada na posicao " + cmd.clause.pos + " para o comando requires");
		}
		return null;
	}

	@Override
	public Void visit(CommandProhibits cmd, Void ctx) {
		if (sProhibits.resolve(cmd.clause.type) == null){
			sProhibits.define(new CommandSymbol(cmd.clause.type, cmd.clause));
		}else{
			errors.add("clausula <" + cmd.clause.type + "> redeclarada na posicao " + cmd.clause.pos + " para o comando prohibits");
		}
		return null;
	}

	@Override
	public Void visit(CommandEncloses cmd, Void ctx) {
		if (sEncloses.resolve(cmd.clause.type) == null){
			sEncloses.define(new CommandSymbol(cmd.clause.type, cmd.clause));
		}else{
			errors.add("clausula <" + cmd.clause.type + "> redeclarada na posicao " + cmd.clause.pos+ " para o comando encloses");
		}
		return null;
	}

}
