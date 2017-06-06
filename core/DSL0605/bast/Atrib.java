package bast;

import java.math.BigInteger;
import java.util.Map;

public class Atrib extends Node implements Stat {
	public final String lval;
	public Exp rval;
	
	public Atrib(int pos, String lval, Exp rval) {
		super(pos);
		this.lval = lval;
		this.rval = rval;
	}

	@Override
	public void run(Map<String, BigInteger> vars) {
		vars.put(lval, rval.eval(vars));
	}

	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}
}
