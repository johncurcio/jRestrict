package mast;

import java.util.List;

import lixo.Command;
import lixo.CommandSymbol;
import lixo.Event;
import lixo.EventSymbol;
import lixo.Machine;
import lixo.ResetEvent;
import lixo.State;
import lixo.StateSymbol;
import lixo.Transition;

public class CollectVisitor implements Visitor<Void, Void> {
	final Scope<EventSymbol> events = new Scope<>("events");
	final Scope<CommandSymbol> commands = new Scope<>("commands");
	final Scope<StateSymbol> states = new Scope<>("states");
	
	final List<String> errors;
	
	public CollectVisitor(List<String> errors) {
		this.errors = errors;
	}
	
	@Override
	public Void visit(CommandFiles fi, Void ctx) {
		return null;
	}

	@Override
	public Void visit(Commands req, Void ctx) {
		return null;
	}
	
	@Override
	public Void visit(JavaArgs req, Void ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(Clause req, Void ctx) {
		// TODO Auto-generated method stub
		return null;
	}	

	//@TODO: remove these visitors
	@Override
	public Void visit(Action ac, Void ctx) {
		return null;
	}

	@Override
	public Void visit(Command cmd, Void ctx) {
		if(commands.resolve(cmd.nome) == null) {
			commands.define(new CommandSymbol(cmd.nome, cmd.codigo));
		} else {
			errors.add("comando " + cmd.nome + " redeclarado na posicao " + cmd.pos);
		}
		return null;
	}

	@Override
	public Void visit(Event ev, Void ctx) {
		if(events.resolve(ev.nome) == null) {
			events.define(new EventSymbol(ev.nome, ev.codigo));
		} else {
			errors.add("evento " + ev.nome + " redeclarado na posição " + ev.pos);
		}
		return null;
	}

	@Override
	public Void visit(Machine m, Void ctx) {
		m.sevents = events;
		m.scommands = commands;
		m.sstates = states;
		for(Event ev: m.events) {
			ev.visit(this, ctx);
		}
		for(Command c: m.commands) {
			c.visit(this, ctx);
		}
		for(State s: m.states) {
			s.visit(this, ctx);
		}
		return null;
	}

	@Override
	public Void visit(ResetEvent rev, Void ctx) {
		return null;
	}

	@Override
	public Void visit(State st, Void ctx) {
		if(states.resolve(st.nome) == null) {
			st.symbol = new StateSymbol(st.nome);
			states.define(st.symbol);
		} else {
			errors.add("estado " + st.nome + " redeclarado na posicao " + st.pos);
		}
		return null;
	}

	@Override
	public Void visit(Transition tr, Void ctx) {
		return null;
	}

	@Override
	public Void visit(Script req, Void ctx) {
		// TODO Auto-generated method stub
		return null;
	}

}
