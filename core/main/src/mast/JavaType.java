package mast;

public class JavaType extends JavaArgs {
	
	public JavaType(int pos, String type) {
		super(pos, type);
	}
	
	public String toString(){
		return this.arg;
	}
	
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}
