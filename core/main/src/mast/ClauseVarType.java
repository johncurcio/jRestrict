package mast;

import java.util.List;

public class ClauseVarType extends Clause {

	public ClauseVarType(int pos, String type, List<JavaArgs> args) {
		super(pos, type, args);
	}
	
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}

}
