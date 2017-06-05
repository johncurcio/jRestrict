package mast;

public interface Visitor<C,R> {
	R visit(Files fi, C ctx);
	R visit(Requires req, C ctx);
	
	//@TODO: remove this
	R visit(Action ac, C ctx);
	R visit(Command cmd, C ctx);
	R visit(Event ev, C ctx);
	R visit(Machine m, C ctx);
	R visit(ResetEvent rev, C ctx);
	R visit(State st, C ctx);
	R visit(Transition tr, C ctx);
}
