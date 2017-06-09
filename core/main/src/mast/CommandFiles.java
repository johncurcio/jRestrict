package mast;

public class CommandFiles extends Node {
	
	public final String filename;
	
	public CommandFiles(int pos, String filename) {
		super(pos);
		this.filename = filename;
	}
	
	public String toString() {
		return this.filename;
	}
	
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}
