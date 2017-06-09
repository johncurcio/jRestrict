package mast;

import lixo.Command;
import lixo.Event;
import lixo.Machine;
import lixo.ResetEvent;
import lixo.State;
import lixo.Transition;

public class StringVisitor implements Visitor<Void, String> {

	@Override
	public String visit(CommandFiles fi, Void ctx) {
		return fi.filename;
	}
	
	@Override
	public String visit(Commands req, Void ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(JavaArgs req, Void ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(Clause req, Void ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	
	//@TODO: remove these
	@Override
	public String visit(Action ac, Void ctx) {
		return ac.nome;
	}
	
	@Override
	public String visit(Command cmd, Void ctx) {
		return cmd.nome + " " + cmd.codigo;
	}

	@Override
	public String visit(Event ev, Void ctx) {
		return ev.nome + " " + ev.codigo;
	}

	@Override
	public String visit(Machine m, Void ctx) {
		StringBuffer buf = new StringBuffer();
		buf.append("events\n");
		for(Event ev: m.events) {
			buf.append(" " + ev.visit(this, ctx) + "\n");
		}
		buf.append("end\n");
		if(!m.revents.isEmpty()) {
			buf.append("resetEvents\n");
			for(ResetEvent rev: m.revents) {
				buf.append(" " + rev.visit(this, ctx) + "\n");
			}
			buf.append("end\n");
		}
		buf.append("commands\n");
		for(Command cmd: m.commands) {
			buf.append(" " + cmd.visit(this, ctx) + "\n");
		}
		buf.append("end\n");
		for(State st: m.states) buf.append(st.visit(this, ctx));
		return buf.toString();
	}

	@Override
	public String visit(ResetEvent rev, Void ctx) {
		return rev.nome;
	}

	@Override
	public String visit(State st, Void ctx) {
		StringBuffer buf = new StringBuffer();
		buf.append("state " + st.nome + "\n");
		if(!st.actions.isEmpty()) {
			buf.append("  actions {");
			for(Action ac: st.actions) {
				buf.append(" " + ac.visit(this, ctx));
			}
			buf.append(" }\n");
		}
		for(Transition tr: st.transitions) {
			buf.append("  " + tr.visit(this, ctx) + "\n");
		}
		buf.append("end\n");
		return buf.toString();
	}

	@Override
	public String visit(Transition tr, Void ctx) {
		return tr.trigger + " => " + tr.target;
	}

	@Override
	public String visit(Script req, Void ctx) {
		// TODO Auto-generated method stub
		return null;
	}


}
