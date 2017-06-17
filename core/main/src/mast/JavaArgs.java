package mast;

public abstract class JavaArgs extends Node {

	public final String arg;
	
	public JavaArgs(int pos, String arg) {
		super(pos);
		this.arg = arg;
	}
	
	public abstract String toString();
	
	public abstract <C, R> R visit(Visitor<C, R> visitor, C ctx);
	
	@Override
	public boolean equals(Object o){
	   if(this==o){
	      return true;
	   }
	   if(o instanceof JavaArgs){
		   JavaArgs other = (JavaArgs) o;
	       return this.arg.equals(other.arg);
	   }
	   return false;
	}
	
	@Override
	public int hashCode(){
	   return arg.hashCode();
	}
}
