package mast;
/**
 * machine := events revents? commands state+
events := "events" event+ "end"
event := nome codigo
commands := "commands" command+ "end"
command := nome codigo
revents := "resetEvents" nome+ "end"
state := "state" nome actions? transition* "end"
actions := "actions" "{" nome+ "}"
transition := nome "=>" nome

reservada   := ('events' | 'state' | 'end' | 'action') ![a-zA-Z]
nome        := !reservada [a-zA-Z]+
codigo      := !nome [A-Z0-9][A-Z0-9][A-Z0-9][A-Z0-9]
comentario  := '--'[^\n]*
espaco      := [ \n\r\t]+ | comentario

 */
import java.util.List;

public class State extends Node {
	public final String nome;
	public final List<Action> actions;
	public final List<Transition> transitions;
	
	StateSymbol symbol;
	
	public State(int pos, String nome, List<Action> actions,
			List<Transition> transitions) {
		super(pos);
		this.nome = nome;
		this.actions = actions;
		this.transitions = transitions;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("state " + nome + "\n");
		if(!actions.isEmpty()) {
			buf.append("  actions {");
			for(Action ac: actions) {
				buf.append(" " + ac.toString());
			}
			buf.append(" }\n");
		}
		for(Transition tr: transitions) {
			buf.append("  " + tr.toString() + "\n");
		}
		buf.append("end\n");
		return buf.toString();
	}

	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}
