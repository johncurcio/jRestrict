package bast;

import java.math.BigInteger;
import java.util.Map;

public class Int2Real extends Node implements Exp {
	public final Exp exp;
	
	public Int2Real(Exp exp) {
		super(((Node)exp).pos);
		this.exp = exp;
	}
	
	@Override
	public BigInteger eval(Map<String, BigInteger> vars) {
		return null;
	}

	@Override
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}

}
