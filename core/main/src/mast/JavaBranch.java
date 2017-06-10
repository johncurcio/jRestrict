package mast;

public class JavaBranch extends JavaArgs {
	
	public JavaBranch(int pos, String branch) {
		super(pos, branch);
	}
	
	public String toString(){
		return this.arg;
	}
	
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}