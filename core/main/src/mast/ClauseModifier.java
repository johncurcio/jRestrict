package mast;

import java.util.List;

public class ClauseModifier extends Clause {

	public ClauseModifier(int pos, String type, List<JavaArgs> args) {
		super(pos, type, args);
	}

	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}

}