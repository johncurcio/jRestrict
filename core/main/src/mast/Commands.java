package mast;

public abstract class Commands extends Node {
	public final Clause clause;
	
	public Commands(int pos, Clause clause) {
		super(pos);
		this.clause = clause;
	}
	
	public String toString() {
		return clause.type + ": " + clause.toString();
	}
	
	public abstract <C, R> R visit(Visitor<C, R> visitor, C ctx);
	
}
