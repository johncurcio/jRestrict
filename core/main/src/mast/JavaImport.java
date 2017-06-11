package mast;

public class JavaImport extends JavaArgs {
	
	public final String reg;
	
	public JavaImport(int pos, String imp, String reg) {
		super(pos, imp);
		this.reg = reg;
	}
	
	public String toString(){
		return this.arg + this.reg;
	}
	
	public boolean isPackage(){
		return !this.reg.equals("");
	}
	
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}