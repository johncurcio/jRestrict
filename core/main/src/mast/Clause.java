package mast;

import java.util.List;

public abstract class Clause extends Node {

	public final List<JavaArgs> args;
	
	public Clause(int pos, List<JavaArgs> args) {
		super(pos);
		this.args = args;
	}
	
	public abstract String toString();
	
	public abstract <C, R> R visit(Visitor<C, R> visitor, C ctx);

}
