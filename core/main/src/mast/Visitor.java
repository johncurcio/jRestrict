package mast;

import lixo.Command;
import lixo.Event;
import lixo.Machine;
import lixo.ResetEvent;
import lixo.State;
import lixo.Transition;

public interface Visitor<C,R> {
	R visit(CommandFiles fi, C ctx);
	R visit(Commands req, C ctx);
	R visit(JavaArgs req, C ctx);
	R visit(Clause req, C ctx);
	R visit(Script req, C ctx);
	
	//@TODO: remove this
	R visit(Action ac, C ctx);
	R visit(Command cmd, C ctx);
	R visit(Event ev, C ctx);
	R visit(Machine m, C ctx);
	R visit(ResetEvent rev, C ctx);
	R visit(State st, C ctx);
	R visit(Transition tr, C ctx);
}
