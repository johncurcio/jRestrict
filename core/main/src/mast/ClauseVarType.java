package mast;

import java.util.List;

public class ClauseVarType extends Clause {

	public ClauseVarType(int pos, List<JavaArgs> args) {
		super(pos, args);
	}

	@Override
	public String toString() {
		return this.args.toString();
	}
	
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}

}
