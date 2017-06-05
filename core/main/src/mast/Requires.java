package mast;

public class Requires extends Node {
	public final String clause;
	
	//shoudn't be ClauseSymbol!!!
	Symbol symbol;
	
	public Requires(int pos, String clause) {
		super(pos);
		this.clause = clause;
	}
	
	public String toString() {
		return clause;
	}
	
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}
