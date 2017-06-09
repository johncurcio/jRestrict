package mast;

import java.util.List;

public class ClauseType extends Clause {

	public ClauseType(int pos, String type, List<JavaArgs> args) {
		super(pos, type, args);
	}

	@Override
	public String toString() {
		return this.args.toString();
	}
	
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}

}
