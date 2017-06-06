package mast;

import java.util.HashMap;
import java.util.Map;

public class ModelVisitor implements Visitor<Void, Void> {
	public machinemodel.StateMachine machine;
			
	@Override
	public Void visit(Action ac, Void ctx) {
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
		Map<StateSymbol, machinemodel.State> states = new HashMap<>();
		Map<CommandSymbol, machinemodel.Command> commands = new HashMap<>();
		Map<EventSymbol, machinemodel.Event> events = new HashMap<>();
		for(CommandSymbol cmd: m.scommands) {
			commands.put(cmd, new machinemodel.Command(cmd.name, cmd.codigo));
		}
		for(EventSymbol evt: m.sevents) {
			events.put(evt, new machinemodel.Event(evt.name, evt.codigo));
		}
		for(StateSymbol st: m.sstates) {
			states.put(st, new machinemodel.State(st.name));
		}
		for(Map.Entry<StateSymbol, machinemodel.State> par: states.entrySet()) {
			StateSymbol stsymb = par.getKey();
			machinemodel.State st = par.getValue();
			for(CommandSymbol cmd: stsymb.actions) {
				st.addAction(commands.get(cmd));
			}
			for(Map.Entry<EventSymbol, StateSymbol> tr: stsymb.transitions.entrySet()) {
				st.addTransition(events.get(tr.getKey()), states.get(tr.getValue()));
			}
		}
		machine = new machinemodel.StateMachine(states.get(m.states.get(0).symbol));
		for(ResetEvent revt: m.revents) {
			if(revt.symbol != null) {
				machine.addResetEvents(events.get(revt.symbol));
			}
		}
		return null;
	}

	@Override
	public Void visit(ResetEvent rev, Void ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(State st, Void ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(Transition tr, Void ctx) {
		// TODO Auto-generated method stub
		return null;
	}

}
