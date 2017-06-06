package mast;

import java.util.List;

public class Requires extends Node {
	public final List<peg.Symbol> clauses;
	
	//shoudn't be ClauseSymbol!!!
	Symbol symbol;
	
	public Requires(int pos, List<peg.Symbol> clauses) {
		super(pos);
		this.clauses = clauses;
	}
	
	public String toString() {
		return clauses.toString();
	}
	
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}
