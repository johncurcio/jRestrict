package mast;

public abstract class JavaArgs extends Node {

	public final String arg;
	
	public JavaArgs(int pos, String arg) {
		super(pos);
		this.arg = arg;
	}
	
	public abstract String toString();
	
	public abstract <C, R> R visit(Visitor<C, R> visitor, C ctx);

}
