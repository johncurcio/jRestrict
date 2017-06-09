package mast;

public class CommandEncloses extends Commands {
	
	public CommandEncloses(int pos, Clause clause) {
		super(pos, clause);
	}
	
	public String toString() {
		return clause.toString();
	}
	
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}