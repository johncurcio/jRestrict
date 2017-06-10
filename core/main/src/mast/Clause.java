package mast;

import java.util.List;

public abstract class Clause extends Node {

	public final List<JavaArgs> args;
	public final String type;
	
	public Clause(int pos, String type, List<JavaArgs> args) {
		super(pos);
		this.type = type;
		this.args = args;
	}
	
	public String toString() {
		return this.args.toString();
	}
	
	public abstract <C, R> R visit(Visitor<C, R> visitor, C ctx);

}
