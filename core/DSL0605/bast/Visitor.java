package bast;

public interface Visitor<C, R> {
	R visit(Atrib st, C ctx);
	R visit(AtribIndex st, C ctx);
	R visit(Div exp, C ctx);
	R visit(Menor exp, C ctx);
	R visit(Mult exp, C ctx);
	R visit(Soma exp, C ctx);
	R visit(Sub exp, C ctx);
	R visit(Num exp, C ctx);
	R visit(Var exp, C ctx);
	R visit(While st, C ctx);
	R visit(Int2Real exp, C ctx);
	R visit(Index exp, C ctx);
	R visit(New exp, C ctx);
	R visit(Prog prog, C ctx);
	R visit(Struct stru, C ctx);
	R visit(Nil exp, C ctx);
}
