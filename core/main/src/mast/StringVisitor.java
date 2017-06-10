package mast;

public class StringVisitor implements Visitor<Void, String> {

	@Override
	public String visit(CommandFiles fi, Void ctx) {
		return fi.filename;
	}
	
	@Override
	public String visit(JavaArgs args, Void ctx) {
		return args.arg;
	}

	@Override
	public String visit(Clause clause, Void ctx) {
		String out = clause.type + ": ";
		for (JavaArgs arg: clause.args){
			out += arg.toString() + ", ";
		}
		out = out.replaceAll(", $", "");
		return out;
	}
	
	@Override
	public String visit(Script script, Void ctx) {
		StringBuffer buf = new StringBuffer();
		buf.append("files { \n");
		for(CommandFiles fi: script.files){
			buf.append(" " + fi.visit(this, ctx) + ";\n");
		}
		buf.append("}\n\n");
		buf.append("requires { \n");
		for(CommandRequires fi: script.requirements){
			buf.append(" " + fi.visit(this, ctx) + ";\n");
		}
		buf.append("}\n\n");
		buf.append("prohibits { \n");
		for(CommandProhibits fi: script.prohibitions){
			buf.append(" " + fi.visit(this, ctx) + ";\n");
		}
		buf.append("}\n\n");
		buf.append("encloses { \n");
		for(CommandEncloses fi: script.enclosement){
			buf.append(" " + fi.visit(this, ctx) + ";\n");
		}
		buf.append("}\n");
		return buf.toString();
	}

	@Override
	public String visit(CommandRequires cmd, Void ctx) {
		return visit(cmd.clause, ctx);
	}

	@Override
	public String visit(CommandProhibits cmd, Void ctx) {
		return visit(cmd.clause, ctx);
	}

	@Override
	public String visit(CommandEncloses cmd, Void ctx) {
		return visit(cmd.clause, ctx);
	}	

}
