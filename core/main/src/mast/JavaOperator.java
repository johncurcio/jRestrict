package mast;

public class JavaOperator extends JavaArgs {
	
	public JavaOperator(int pos, String op) {
		super(pos, op);
	}
	
	public String toString(){
		return this.arg;
	}
	
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}