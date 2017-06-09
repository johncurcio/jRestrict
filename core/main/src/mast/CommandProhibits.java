package mast;

public class CommandProhibits extends Commands {
	
	public CommandProhibits(int pos, Clause clause) {
		super(pos, clause);
	}
	
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}
