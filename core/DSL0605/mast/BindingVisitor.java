package mast;

import java.util.List;

public class BindingVisitor implements Visitor<Void, Void> {
	Scope<EventSymbol> events;
	Scope<CommandSymbol> commands;
	Scope<StateSymbol> states;

	final List<String> errors;
	
	public BindingVisitor(List<String> errors) {
		this.errors = errors;
	}
	
	@Override
	public Void visit(Action ac, Void ctx) {
		ac.symbol = commands.resolve(ac.nome);
		if(ac.symbol == null) {
			errors.add("ação " + ac.nome + " não corresponde a um comando na posição " + ac.pos);
		}
		return null;
	}

	@Override
	public Void visit(Command cmd, Void ctx) {
		return null;
	}

	@Override
	public Void visit(Event ev, Void ctx) {
		return null;
	}

	@Override
	public Void visit(Machine m, Void ctx) {
		events = m.sevents;
		commands = m.scommands;
		states = m.sstates;
		for(ResetEvent rev: m.revents) {
			rev.visit(this, ctx);
		}
		for(State st: m.states) {
			st.visit(this, ctx);
		}
		return null;
	}

	@Override
	public Void visit(ResetEvent rev, Void ctx) {
		rev.symbol = events.resolve(rev.nome);
		if(rev.symbol == null) {
			errors.add("evento de reset " + rev.nome + " não corresponde a um evento na posição " + rev.pos);
		}
		return null;
	}

	@Override
	public Void visit(State st, Void ctx) {
		StateSymbol symbol = st.symbol;
		if(symbol != null) {
			for(Action ac: st.actions) {
				ac.visit(this, ctx);
				symbol.actions.add(ac.symbol);
			}
			for(Transition tr: st.transitions) {
				tr.visit(this, ctx);
				if(symbol.transitions.containsKey(tr.strigger)) {
					errors.add("transição com mesmo disparador " + tr.trigger + " na posição " + tr.pos);
				} else {
					symbol.transitions.put(tr.strigger, tr.starget);
				}
			}
		}
		return null;
	}

	@Override
	public Void visit(Transition tr, Void ctx) {
		tr.strigger = events.resolve(tr.trigger);
		if(tr.strigger == null) {
			errors.add("transição usa um evento " + tr.trigger + " não declarado na posição " + tr.pos);
		}
		tr.starget = states.resolve(tr.target);
		if(tr.starget == null) {
			errors.add("transição usa um estado " + tr.target + " não declarado na posição " + tr.pos);
		}
		return null;
	}

}
