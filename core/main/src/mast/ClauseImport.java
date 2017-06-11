package mast;

import java.util.List;

public class ClauseImport extends Clause {

	public ClauseImport(int pos, String type, List<JavaArgs> args) {
		super(pos, type, args);
	}
	
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}

}