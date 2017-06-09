package mast;

public class JavaLoop extends JavaArgs {
	
	public JavaLoop(int pos, String modifier) {
		super(pos, modifier);
	}
	
	public String toString(){
		return this.arg;
	}
	
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}

