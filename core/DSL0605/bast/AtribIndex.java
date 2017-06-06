package bast;

import java.math.BigInteger;
import java.util.Map;

public class AtribIndex extends Node implements Stat {
	public final Index lval;
	public Exp rval;

	public AtribIndex(int pos, Exp lval, Exp rval) {
		super(pos);
		this.lval = (Index)lval;
		this.rval = rval;
	}

	@Override
	public void run(Map<String, BigInteger> vars) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <C, R> R visit(Visitor<C, R> visitor, C ctx) {
		return visitor.visit(this, ctx);
	}

}
