package mast;


public interface Visitor<C,R> {
	R visit(CommandFiles fi, C ctx);
	//R visit(Commands cmd, C ctx);
	R visit(CommandRequires cmd, C ctx);
	R visit(CommandProhibits cmd, C ctx);
	R visit(CommandEncloses cmd, C ctx);
	R visit(JavaArgs arg, C ctx);
	R visit(Clause clause, C ctx);
	R visit(Script script, C ctx);
}
