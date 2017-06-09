package mast;

public class JavaModifier extends JavaArgs {
	
	public JavaModifier(int pos, String modifier) {
		super(pos, modifier);
	}
	
	public String toString(){
		return this.arg;
	}
	
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}
