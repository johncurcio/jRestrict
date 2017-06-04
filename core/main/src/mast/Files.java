package mast;

public class Files extends Node {
	
	public final String filename;
	
	public Files(int pos, String filename) {
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
