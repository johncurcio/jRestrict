package mast;


public interface Visitor<C,R> {
	R visit(CommandFiles fi, C ctx);
	R visit(CommandRequires cmd, C ctx);
	R visit(CommandProhibits cmd, C ctx);
	R visit(CommandEncloses cmd, C ctx);
	R visit(JavaArgs arg, C ctx);
	R visit(Clause clause, C ctx);
	R visit(ClauseRetType clause, C ctx);
	R visit(ClauseType clause, C ctx);
	R visit(ClauseVarType clause, C ctx);
	R visit(ClauseLoop clause, C ctx);
	R visit(ClauseBranch clause, C ctx);
	R visit(ClauseImport clause, C ctx);
	R visit(ClauseModifier clause, C ctx);
	R visit(ClauseOperator clause, C ctx);
	R visit(Script script, C ctx);
}
