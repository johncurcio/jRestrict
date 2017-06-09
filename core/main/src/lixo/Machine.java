package lixo;

import java.util.List;

import mast.Scope;
import mast.StringVisitor;
import mast.Visitor;

public class Machine {
	public final List<Event> events;
	public final List<Command> commands;
	public final List<ResetEvent> revents;
	public final List<State> states;
	
	public Scope<EventSymbol> sevents;
	public Scope<CommandSymbol> scommands;
	public Scope<StateSymbol> sstates;
	
	public Machine(List<Event> events, List<Command> commands,
			List<ResetEvent> revents, List<State> states) {
		this.events = events;
		this.commands = commands;
		this.revents = revents;
		this.states = states;
	}
	
	public String toString() {
//		StringBuffer buf = new StringBuffer();
//		buf.append("events\n");
//		for(Event ev: events) {
//			buf.append(" " + ev.toString() + "\n");
//		}
//		buf.append("end\n");
//		if(!revents.isEmpty()) {
//			buf.append("resetEvents\n");
//			for(ResetEvent rev: revents) {
//				buf.append(" " + rev.toString() + "\n");
//			}
//			buf.append("end\n");
//		}
//		buf.append("commands\n");
//		for(Command cmd: commands) {
//			buf.append(" " + cmd.toString() + "\n");
//		}
//		buf.append("end\n");
//		for(State st: states) buf.append(st.toString());
//		return buf.toString();
		return visit(new StringVisitor(), null);
	}

	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}
