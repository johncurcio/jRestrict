package mast;

public class Transition extends Node {
	public final String trigger;
	public final String target;

	EventSymbol strigger;
	StateSymbol starget;
	
	public Transition(int pos, String trigger, String target) {
		super(pos);
		this.trigger = trigger;
		this.target = target;
	}

	public String toString() {
		return trigger + " => " + target;
	}

	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}
