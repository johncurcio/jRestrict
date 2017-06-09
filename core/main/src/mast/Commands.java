package mast;

public abstract class Commands extends Node {
	public final Clause clause;
	
	public Commands(int pos, Clause clause) {
		super(pos);
		this.clause = clause;
	}
	
	public abstract String toString();
	
	public abstract <C, R> R visit(Visitor<C, R> visitor, C ctx);
	
}
